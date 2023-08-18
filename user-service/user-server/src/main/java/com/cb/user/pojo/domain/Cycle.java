package com.cb.user.pojo.domain;

/**
 * vip周期
 *
 * @author fj0591
 */
public enum Cycle {

    /**
     * 一天
     */
    DAY(1),

    /**
     * 一周
     */
    WEEK(7),

    /**
     * 一月
     */
    MONTH(30),

    /**
     * 一季度
     */
    QUARTER(90),

    /**
     * 半年
     */
    HALF(183),

    /**
     * 一年
     */
    YEAR(365);

    private final Integer value;

    Cycle(Integer value){this.value = value;}

    public Integer getValue() {
        return value;
    }

}
