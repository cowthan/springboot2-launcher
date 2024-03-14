package org.danger.utils.mybatis;

import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 分页辅助类，基于github page helper
 */
public final class PageUtils {

    private PageUtils() {
    }

    /**
     * 开始分页，调用此方法之后，不允许出现随意的select查询，只能查主数据且必须查
     *
     * @param page page
     * @param size size
     */
    public static void startPage(int page, int size) {
        String orderBy = "";
        PageHelper.startPage(page, size, orderBy);
    }

    /**
     * 开始分页，调用此方法之后，不允许出现随意的select查询，只能查主数据且必须查
     *
     * @param pageCriteria pageCriteria
     */
    public static void startPage(PageCriteria pageCriteria) {
        String orderBy = "";
        PageHelper.startPage(pageCriteria.getPage(), pageCriteria.getSize(), orderBy);
    }

    /**
     * 将github分页插件的返回转成PageData
     *
     * @param list list
     * @param <T>  元素类型
     * @return 结果
     */
    public static <T> PageData<T> buildTo(List<T> list) {
        PageInfo<T> pageInfo = new PageInfo(list);
        PageData<T> data = new PageData<T>(pageInfo.getList(), pageInfo.getTotal(), pageInfo.getPageSize());
        return data;
    }

}
