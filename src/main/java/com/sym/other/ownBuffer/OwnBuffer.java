package com.sym.other.ownBuffer;

import java.util.ArrayList;
import java.util.List;

/**
 * 模拟一个缓冲区
 *
 * @Auther: shenym
 * @Date: 2018-12-06 10:28
 */
public class OwnBuffer {

    private List<String> data;

    public OwnBuffer(){
        data = new ArrayList<>(10);
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public void put(String str){
        data.add(str);
    }

    public void get(String s){
        data.remove(s);
    }
}
