package com.sym.threadSharing.diffCode;

import lombok.*;

/**
 * 如果每个线程的执行逻辑不一样，就把共享数据独立到外部对象中，所有线程围绕这个外部对象来操作
 * 就可以实现线程共享数据
 * <p>
 * Created by 沈燕明 on 2018/12/22.
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class SharingData {
    private int id;
    private String name;
}
