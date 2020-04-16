package com.sym.util;

import java.io.IOException;

/**
 * @author ym.shen
 * Created on 2020/4/16 11:46
 */
public class ThreadUtil {

    public static void sync(){
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
