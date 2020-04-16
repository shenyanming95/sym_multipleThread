package com.sym.other.buffer;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 模拟一个缓冲区
 *
 * @author shenyanming
 * @date 2018-12-06 10:28
 */
@Data
public class OwnBuffer {

    private List<String> data;

    public OwnBuffer(){
        this(10);
    }

    public OwnBuffer(int capacity){
        data = new ArrayList<>(capacity);
    }

    public void put(String str){
        data.add(str);
    }

    public void get(String s){
        data.remove(s);
    }
}
