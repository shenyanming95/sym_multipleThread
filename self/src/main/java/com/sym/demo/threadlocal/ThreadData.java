package com.sym.demo.threadlocal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * 解决ThreadLocal一次只能保存一个变量的问题：
 * 可以将变量定义在外部类中，ThreadLocal保存的是外部类这个对象
 * <p>
 * Created by 沈燕明 on 2018/12/21.
 */
@AllArgsConstructor
@ToString
@Data
class ThreadData {
    // 模拟需要保存的变量,可以扩展到满足自己的需求
    private int id;
    private String name;
}
