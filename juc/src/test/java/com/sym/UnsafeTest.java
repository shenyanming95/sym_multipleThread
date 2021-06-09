package com.sym;

import com.sym.juc.unsafe.UnSafeUtil;
import org.junit.Test;
import sun.misc.Unsafe;

/**
 * @author shenyanming
 * Created on 2021/6/9 17:48
 */
public class UnsafeTest {

    @Test
    public void test01() throws NoSuchFieldException {
        Unsafe unsafe = UnSafeUtil.getUnsafe();
        long offset = unsafe.objectFieldOffset(Demo.class.getDeclaredField("value"));

        Demo demo = new Demo();
        demo.value = "666";

        // 相当于 Demo.value 是被 volatile 修饰一样.
        System.out.println(unsafe.getObjectVolatile(demo, offset));
        System.out.println(unsafe.getObject(demo, offset));
    }

    static class Demo{
        String value;
    }
}
