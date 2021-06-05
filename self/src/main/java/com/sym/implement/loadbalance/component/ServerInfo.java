package com.sym.implement.loadbalance.component;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shenyanming
 * Created on 2020/6/12 18:18
 */
@Data
@Accessors(chain = true)
public class ServerInfo {
    private int id;
    private String name;
    private URL url;

    @SneakyThrows
    public static List<ServerInfo> initList(int size) {
        List<ServerInfo> retList = new ArrayList<>(size);
        ServerInfo info;
        for (int i = 0; i < size; i++) {
            info = new ServerInfo();
            info.setId(i)
                    .setName("test_" + i)
                    .setUrl(new URL("http://www.baidu.com/" + i));
            retList.add(info);
        }
        return retList;
    }
}
