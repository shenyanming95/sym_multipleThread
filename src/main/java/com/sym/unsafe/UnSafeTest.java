package com.sym.unsafe;

import org.junit.Before;
import org.junit.Test;
import sun.misc.Unsafe;

/**
 * @author shenym
 * @date 2020/2/28 22:15
 */

public class UnSafeTest {

    private Unsafe unsafe;

    @Before
    public void init() {
        unsafe = UnSafeUtil.getUnsafe();
    }

    @Test
    public void firstTest() {
        System.out.println(unsafe);
    }
}
