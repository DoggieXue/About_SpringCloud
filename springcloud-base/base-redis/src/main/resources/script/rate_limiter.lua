---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by xuexiao.
--- DateTime: 2022/3/13 上午9:08
--- Description: 此脚本的环境： redis 内部，不是运行在 nginx 内部。该脚本可以在Nginx网关层完成限流和秒杀

---方法：申请令牌
--- -1 failed
--- 1 success
-- @param key key 限流关键字
-- @param apply  申请的令牌数量
local function acquire(key, apply)
    local times = redis.call('TIME');
    -- times[1] 秒数   -- times[2] 微秒数
    local curr_mill_second = times[1] * 1000000 + times[2];
    curr_mill_second = curr_mill_second / 1000;

    local cacheInfo = redis.pcall("HMGET", key, "last_mill_second", "curr_permits", "max_permits", "rate")
    --- 局部变量：上次申请的时间
    local last_mill_second = cacheInfo[1];
    --- 局部变量：之前的令牌数
    local curr_permits = tonumber(cacheInfo[2]);
    --- 局部变量：桶的容量
    local max_permits = tonumber(cacheInfo[3]);
    --- 局部变量：令牌的发放速率
    local rate = tonumber(cacheInfo[4]);
    --- 局部变量：本次的令牌数
    local local_curr_permits = 0;

    if (type(last_mill_second) ~= 'boolean' and last_mill_second ~= nil) then
        -- 计算时间段内的令牌数
        local reverse_permits = math.floor(((curr_mill_second - last_mill_second) / 1000) * rate);
        -- 令牌总数
        local expect_curr_permits = reverse_permits + curr_permits;
        -- 可以申请的令牌总数
        local_curr_permits = math.min(expect_curr_permits, max_permits);
    else
        -- 第一次获取令牌
        redis.pcall("HSET", key, "last_mill_second", curr_mill_second)
        local_curr_permits = max_permits;
    end

    local result = -1;
    -- 有足够的令牌可以申请
    if (local_curr_permits - apply >= 0) then
        -- 保存剩余的令牌
        redis.pcall("HSET", key, "curr_permits", local_curr_permits - apply);
        -- 为下次的令牌获取，保存时间
        redis.pcall("HSET", key, "last_mill_second", curr_mill_second)
        -- 返回令牌获取成功
        result = 1;
    else
        -- 返回令牌获取失败
        result = -1;
    end
    return result
end

---方法：初始化限流 Key
--- 1 success
-- @param key key
-- @param max_permits  桶的容量
-- @param rate  令牌的发放速率
local function init(key, max_permits, rate)
    ---     local rate_limit_info = redis.pcall("HMGET", key, "last_mill_second", "curr_permits", "max_permits", "rate")
    redis.pcall("HMSET", key, "max_permits", max_permits, "rate", rate, "curr_permits", max_permits)
    return 1;
end

---方法：删除限流 Key
local function delete(key)
    redis.pcall("DEL", key)
    return 1;
end


local key = KEYS[1]
local method = ARGV[1]
if method == 'acquire' then
    return acquire(key, ARGV[2])
elseif method == 'init' then
    return init(key, ARGV[2], ARGV[3])
elseif method == 'delete' then
    return delete(key)
else
    --ignore
end
