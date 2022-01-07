package com.ll.niceId.core;

import com.ll.niceId.core.impl.LongIdGen;
import com.ll.niceId.core.impl.StringIdGen;
import org.springframework.stereotype.Service;

/**
 * @description: com.ll.niceId.NiceId
 * @author: Tomliu
 * @create: 2021-12-28 21:20
 **/
@Service
public class NiceIdGen {

    private static LongIdGen longIdGernerator;

    private static StringIdGen stringIdGenerator;


    public NiceIdGen(LongIdGen longIdGernerator, StringIdGen stringIdGenerator) {
        NiceIdGen.longIdGernerator = longIdGernerator;
        NiceIdGen.stringIdGenerator = stringIdGenerator;
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
