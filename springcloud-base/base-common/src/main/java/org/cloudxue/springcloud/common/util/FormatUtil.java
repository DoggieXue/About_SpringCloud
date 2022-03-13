package org.cloudxue.springcloud.common.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @ClassName FormatUtil
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/3/12 下午9:14
 * @Version 1.0
 **/
public class FormatUtil {
    /**
     * 设置数字格式，保留有效小数位数为fractions
     *
     * @param fractions 保留有效小数位数
     * @return 数字格式
     */
    public static DecimalFormat decimalFormat(int fractions) {

        DecimalFormat df = new DecimalFormat("#0.0");
        df.setRoundingMode(RoundingMode.HALF_UP);
        df.setMinimumFractionDigits(fractions);
        df.setMaximumFractionDigits(fractions);
        return df;
    }
}
