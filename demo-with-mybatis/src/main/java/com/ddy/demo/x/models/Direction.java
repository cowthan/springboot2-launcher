package com.ddy.demo.x.models;


import java.util.HashMap;
import java.util.Map;

public enum Direction {
    IN(0, "进"),
    OUT(1, "出");

    private int code;
    private String msg;
    private static final Map<Object, Direction> RESOLVE_MAP = new HashMap();

    private Direction(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public static Direction resolver(Object key) {
        Direction provider = (Direction) RESOLVE_MAP.get(key);
        return null == provider ? valueOf(String.valueOf(key)) : provider;
    }

    static {
        Direction[] var0 = values();
        int var1 = var0.length;

        for (int var2 = 0; var2 < var1; ++var2) {
            Direction provider = var0[var2];
            RESOLVE_MAP.put(provider.getCode(), provider);
            RESOLVE_MAP.put(String.valueOf(provider.getCode()), provider);
            RESOLVE_MAP.put(provider.name(), provider);
        }

    }
}
