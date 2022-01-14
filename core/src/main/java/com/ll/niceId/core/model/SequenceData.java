package com.ll.niceId.core.model;

import lombok.Data;

/**
 * 序列号对象
 */
@Data
public class SequenceData {

    /**
     * 序号
     */
    private short sequence;

    /**
     * 当前sequence的时间戳
     */
    private long timeMillis;

}
