package com.ll.niceId.core.config;


import com.google.common.base.Strings;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.ll.niceId.core.config.Constants.*;

/**
 * NiceIdGen配置参数
 * @author: Tomliu
 * @create: 2021-12-30 23:08
 **/
@Data
@RequiredArgsConstructor
public class NiceIdGenConfig {


    /**
     * 机器编号
     * <p>
     *     机器编号需要保证在整体集群中唯一，否则可能造成生成的id重复
     * </p>
     */
    private short machineId = 1;

    /**
     * id时间部分的起始时间
     */
    private Date startTime = getDefaultStartTime();


    /**
     * 设置id时间部分的起始时间
     * @param startTimeFomattedString 起始时间的字符串，格式如 yyyy-MM-dd
     */
    public void setStartTime(String startTimeFomattedString) {
        if (!Strings.isNullOrEmpty(startTimeFomattedString)) {
            this.startTime = getDateFromFomattedString(startTimeFomattedString);
        }
        //startTime为null 时,自动使用默认值
    }

    /**
     * 获取默认起始时间
     * @return 返回默认起始时间
     */
    public static Date getDefaultStartTime() {
        return getDateFromFomattedString(DEFAULT_START_TIME);
    }

    /**
     * 从指定格式的时间字符串获取时间类型
     * @param dateFormattedString 指定格式的时间字符串，格式如 yyyy-MM-dd HH:mm:ss
     * @return 返回对应的时间类型
     */
    private static Date getDateFromFomattedString(String dateFormattedString){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DEFAULT_START_TIME_FORMAT);
        Date defaultStartDate = null;
        try {
            defaultStartDate = simpleDateFormat.parse(dateFormattedString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return defaultStartDate;
    }
}
