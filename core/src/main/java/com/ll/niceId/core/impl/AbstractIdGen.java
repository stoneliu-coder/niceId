package com.ll.niceId.core.impl;

/**
 * id生成器抽象基类
 * @author tomliu
 * @create: 2022-01-12 17:00
 */
abstract class AbstractIdGen {

    /**
     * @description: 业务参数配置
     * @author: Tomliu
     * @create: 2021-12-30 11:23
     **/
    protected static class BizConfig {
        /**
         * 随机值部分长度
         */
        public final static long RANDOM_PART_WIDTH = 7L;

        /**
         * 机器号部分长度（允许最大1024台机器）
         */
        public final static long MACHINE_ID_WIDTH = 10L;

        /**
         * 最大机器号
         */
        public final static int MAX_MACHINE_ID = 1024;


        /**
         * 最大回拨时间差
         */
        public final static long MAX_TIME_DIFF_MIL = 5 * 1000L;
    }

}
