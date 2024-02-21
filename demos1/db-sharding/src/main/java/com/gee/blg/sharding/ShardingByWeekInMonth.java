package com.gee.blg.sharding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Range;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

/**
 * 分表实现类：日期分表策略
 */
public class ShardingByWeekInMonth extends BaseSharding<Date> {

    /**
     * 获取 指定分表
     */
    /**
     * @param availableTargetNames 表名t_blog什么的
     * @param preciseShardingValue logicTableName=t_blog, columnName=create_time, value=Date,就是入库的值
     * @return
     */
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Date> preciseShardingValue) {
        return shardingTablesCheckAndCreatAndReturn(preciseShardingValue.getLogicTableName(),
//                preciseShardingValue.getLogicTableName() + "-" + getSuffixByDay(preciseShardingValue.getValue()));
                preciseShardingValue.getLogicTableName() + "_" + getSuffixByWeek(preciseShardingValue.getValue()));
    }


    private static String getSuffixByWeek(Date date){
        String[] ymw = getWeekOrderInMonth(date);
        return ymw[0] + "_" + ymw[1] + "_" + ymw[2];
    }

    //获取传入的日期天为当月的第几周 返回年月周   每月第一个星期一为第一周，每周一为该周的第一天
    private static String[] getWeekOrderInMonth(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        //第几周
        int week = cal.get(Calendar.WEEK_OF_MONTH);
        int first = getFirst(sdf.format(cal.getTime()));
        if (first != 1) {
            week = week-1;
        }
        String[] split = sdf.format(cal.getTime()).split("-");
        split[2] = week+"";
        return split;
        //System.out.println("所在周星期一的日期：" + sdf.format(cal.getTime()) +"第几周"+week);
    }



    //每月一号是周几
    private static int getFirst(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        String[] split = str.split("-");
        String dateStr = split[0]+"-"+split[1]+"-1";
        Date date1 = null;
        try {
            date1 = sdf.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(dateStr, e);
        }
        calendar.setTime(date1);
        //第几天，从周日开始
        int day = calendar.get(Calendar.DAY_OF_WEEK)-1;
        if (0 == day) {
            day = 7;
        }
        return day;
    }

    /**
     * 获取 范围分表
     */
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Date> rangeShardingValue) {
        Range<Date> valueRange = rangeShardingValue.getValueRange();
        Date lowerDate = valueRange.lowerEndpoint();
        Date upperDate = valueRange.upperEndpoint();
        List<String> tableNameList = new ArrayList<>();
        List<DateTime> dateTimes = DateUtil.rangeToList(DateUtil.beginOfDay(lowerDate), DateUtil.endOfDay(upperDate), DateField.DAY_OF_YEAR);
        for (DateTime dateTime : dateTimes) {
            String resultTableName = rangeShardingValue.getLogicTableName() + "_" + getSuffixByWeek(dateTime);
            if (shardingTablesExistsCheck(resultTableName)) {
                tableNameList.add(resultTableName);
            }
        }
        return tableNameList;
    }
}