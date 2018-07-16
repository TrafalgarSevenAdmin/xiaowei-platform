package com.xiaowei.wechat.consts;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class ServerInfoProperties {

    @Value("${server.host}")
    private String host;

    @Value("${server.pre.index}")
    private String preIndex;

    @Value("${server.pre.bind}")
    private String preBind;

    public String getPreIndex() {
        return host + preIndex;
    }

    public String getPreBind() {
        return host + preBind;
    }
}
