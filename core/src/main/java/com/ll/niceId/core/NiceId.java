package com.ll.niceId.core;

import com.ll.niceId.core.impl.LongIdGernerator;
import com.ll.niceId.core.impl.StringIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: com.ll.niceId.NiceId
 * @author: Tomliu
 * @create: 2021-12-28 21:20
 **/
@Component
public class NiceId {

    private static LongIdGernerator longIdGernerator;

    private static StringIdGenerator stringIdGenerator;

    @Autowired
    public NiceId(LongIdGernerator longIdGernerator, StringIdGenerator stringIdGenerator) {
        NiceId.longIdGernerator = longIdGernerator;
        NiceId.stringIdGenerator = stringIdGenerator;
    }

    /**
     * 获取一个新的id
     * @return
     */
    public static long newId() {
        return longIdGernerator.newId(58);
    }

    /**
     * 获取一个新的String类型id
     * @return
     */
    public static String newStringId() {
        return stringIdGenerator.newId(58);
    }
}
