package com.sym.util;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 利用反射大法获取{@link Unsafe}实例
 *
 * @author shenym
 * @date 2020/2/28 22:10
 */

public class UnSafeUtil {

    private static Unsafe UNSAFE;

    static {
        Class<Unsafe> cls = Unsafe.class;
        try {
            Field field = cls.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            UNSAFE = (Unsafe)field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Unsafe getUnsafe(){
        return UNSAFE;
    }
}
