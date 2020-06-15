package com.sym.threadpool.monitor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 为了使用线程池执行任务时，能较多的获取任务的信息，我们不能仅仅使用
 * {@link Runnable}对象，这样能获取的信息实在是少
 *
 * @author shenyanming
 * Created on 2019/9/7.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ToString
abstract class AbstractMonitorRunnable implements Runnable, Serializable {

    private static final long serialVersionUID = -2306975053976880636L;
    private Long id;
    private groupType groupType;
    private String name;
    private String desc;


    /**
     * 组别类型
     */
    enum groupType{
        // 根据业务系统来定义各自的组别类型
        REVIEW,
        COLLECTION,
        TELEMARKETING
    }

    // ...
    // other useful information at your system
}
