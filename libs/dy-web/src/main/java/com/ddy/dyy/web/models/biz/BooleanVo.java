package com.ddy.dyy.web.models.biz;

public class BooleanVo {

    public int r;

    private BooleanVo(int r) {
        this.r = r;
    }

    public static BooleanVo of(boolean r) {
        return r ? TRUE : FALSE;
    }

    public static BooleanVo TRUE = new BooleanVo(1);
    public static BooleanVo FALSE = new BooleanVo(0);

    private BooleanVo() {
    }
}
