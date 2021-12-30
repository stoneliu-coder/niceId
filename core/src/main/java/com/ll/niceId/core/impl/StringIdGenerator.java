package com.ll.niceId.core.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @description: 字符串型id生成器
 * @author: Tomliu
 * @create: 2021-12-29 15:42
 **/
@Component
public class StringIdGenerator {

    @Autowired
    private LongIdGernerator longIdGernerator;

    /**
     * 可用的字符
     */
    private final static char[] DEFAULT_AVAILABLE_CHARS
            = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    };

    /**
     * 生成一个新的id
     *
     * @param machineId 机器号
     * @return 返回新的字符型id
     */
    public String newId(int machineId) {
        //获取10进制的id
        long id = longIdGernerator.newId(machineId);

        //将10进制id进行进制转换
        return tenToRadix(id);
    }

    /**
     * 10进制转当前进制
     *
     * @return 目标进制结果
     */
    private String tenToRadix(long number) {
        StringBuilder sb = new StringBuilder();
        char[] availableChars = DEFAULT_AVAILABLE_CHARS;
        int toRadix = availableChars.length;
        long currentNumber = number;
        while (currentNumber != 0) {
            sb.append(availableChars[(int) (currentNumber % toRadix)]);
            currentNumber = currentNumber / toRadix;
        }
        return sb.reverse().toString();
    }
}
