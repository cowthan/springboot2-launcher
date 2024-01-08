package com.ddy.dyy.mybatis.page;

import java.util.List;

import com.ddy.dyy.mybatis.models.PageList;
import com.ddy.dyy.mybatis.models.PageRequest;
import com.github.pagehelper.Page;
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
     * @param pageRequest .
     */
    public static void startPage(PageRequest pageRequest) {
        String orderBy = "";
        PageHelper.startPage(pageRequest.getPage(), pageRequest.getSize(), orderBy);
    }

    /**
     * 将github分页插件的返回转成PageData
     *
     * @param list list
     * @param <T>  元素类型
     * @return 结果
     */
    public static <T> PageList<T> parse(List<T> list) {
        PageInfo<T> pageInfo = new PageInfo(list);
        PageList<T> data = new PageList<T>(pageInfo.getList(), pageInfo.getTotal(), pageInfo.getPageSize());
        return data;
    }


    /**
     * 将github分页插件的返回转成PageData
     *
     * @param list list
     * @param <T>  元素类型
     * @return 结果
     */
    public static <T> PageList<T> newInstance(PageList<?> list) {
        PageList<T> data = new PageList<T>(null, list.getTotalCount(), list.getPageSize());
        return data;
    }

    public interface ListSelector<T> {
        List<T> select();
    }


    public static <T> PageList<T> selectPage(int page, int pageSize, ListSelector<T> selector){
        PageHelper.startPage(page, pageSize);
        try {

            List<T> list = selector.select();
            Page<T> listPage = (Page<T>) list;
            PageList<T> vo = new PageList<>();
            vo.setPages(listPage.getPages());
            vo.setPageSize(listPage.getPageSize());
            vo.setTotalCount((int) listPage.getTotal());
            vo.setList(listPage.getResult());
            return vo;
        }finally {
            // 非必须，只要保证startPage之后必然有个查询就行，查询会消费掉分页参数
            PageHelper.clearPage();
        }
    }
}
