package com.ddy.dyy.web.models.biz;

import lombok.Data;

@Data
public class LongVo {
    private long r;

    public static LongVo of(long r){
        return new LongVo(r);
    }
    public static LongVo of(boolean r){
        return new LongVo(r ? 1 : 0);
    }

    public LongVo(long r) {
        this.r = r;
    }

    public LongVo() {
    }
}
