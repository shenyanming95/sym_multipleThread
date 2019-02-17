package com.sym.threadLocal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *  解决ThreadLocal一次只能保存一个变量的问题：
 *  可以将变量定义在Data类中，ThreadLocal保存的是Data这个变量
 *
 * Created by 沈燕明 on 2018/12/21.
 */
@AllArgsConstructor
@ToString
public class ThreadData {
    // 模拟需要保存的变量,可以扩展到满足自己的需求
    @Setter
    @Getter
    private int id;
    @Setter
    @Getter
    private String name;
}
