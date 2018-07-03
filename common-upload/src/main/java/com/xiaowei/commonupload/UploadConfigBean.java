package com.xiaowei.commonupload;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "upload")
@Component
public class UploadConfigBean {

    private String path;
    private String accessUrlRoot;
    private String type;
    private String[] tags;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAccessUrlRoot() {
        return accessUrlRoot;
    }

    public void setAccessUrlRoot(String accessUrlRoot) {
        this.accessUrlRoot = accessUrlRoot;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }
}
