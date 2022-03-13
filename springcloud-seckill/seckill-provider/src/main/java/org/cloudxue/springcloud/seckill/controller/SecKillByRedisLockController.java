package org.cloudxue.springcloud.seckill.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.cloudxue.springcloud.api.dto.SecKillDTO;
import org.cloudxue.springcloud.api.dto.SecKillOrderDTO;
import org.cloudxue.springcloud.common.constants.SessionConstants;
import org.cloudxue.springcloud.common.exception.BusinessException;
import org.cloudxue.springcloud.common.result.RestOut;
import org.cloudxue.springcloud.seckill.service.impl.RedisSecKillServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName SeckillByRedisLockController
 * @Description 秒杀练习 控制层
 * @Author xuexiao
 * @Date 2022/3/6 下午10:03
 * @Version 1.0
 **/
@Api(tags = "秒杀练习 RedisLock版本")
@RestController
@RequestMapping("/api/seckill/redis/")
public class SecKillByRedisLockController {

    @Resource
    RedisSecKillServiceImpl redisSecKillService;

    /**
     *
     * @param exposedKey
     * @param request
     * @return
     */
    @ApiOperation(value = "获取秒杀令牌")
    @RequestMapping(value = "/token/{exposedKey}/v1", method = RequestMethod.GET)
    public RestOut<String> getSecKillToken(@PathVariable String exposedKey, HttpServletRequest request) {
        String userIdentifier = request.getHeader(SessionConstants.USER_IDENTIFIER);
        if (null == userIdentifier) {
            throw BusinessException.builder().errMsg("用户ID不能为空").build();
        }
        String result = redisSecKillService.getSecKillToken(exposedKey, userIdentifier);
        return RestOut.success(result).setRespMsg("获取秒杀令牌成功");
    }

    /**
     * 秒杀下单
     * @param dto
     * @return
     */
    @ApiOperation(value = "秒杀下单")
    @PostMapping("/do/v1")
    public RestOut<SecKillOrderDTO> executeSecKill(@RequestBody SecKillDTO dto) {
        SecKillOrderDTO orderDTO = redisSecKillService.executeSecKill(dto);
        return RestOut.success(orderDTO).setRespMsg("秒杀成功");
    }
}
