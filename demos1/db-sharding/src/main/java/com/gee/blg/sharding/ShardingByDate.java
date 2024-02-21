package com.gee.blg.sharding;

import java.util.ArrayList;
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
public class ShardingByDate extends BaseSharding<Date> {

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
                preciseShardingValue.getLogicTableName() + "-" + getSuffixByDay(preciseShardingValue.getValue()));
    }

    private static String getSuffixByDay(Date date) {
        return DateUtil.format(date, "yyyy_MM_dd");
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
        for (DateTime dateTime : DateUtil.rangeToList(DateUtil.beginOfDay(lowerDate), DateUtil.endOfDay(upperDate), DateField.DAY_OF_YEAR)) {
            String resultTableName = rangeShardingValue.getLogicTableName() + DateUtil.format(dateTime, "_yyyy_MM_dd");
            if (shardingTablesExistsCheck(resultTableName)) {
                tableNameList.add(resultTableName);
            }
        }
        return tableNameList;
    }
}