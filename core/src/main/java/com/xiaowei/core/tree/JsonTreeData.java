package com.xiaowei.core.tree;

import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 树 json model 数据
 * @author glq
 *
 */
public class JsonTreeData implements Serializable {

    public String id;       //json id
    public String text;     //json 显示文本
    public String state;    //json 'open','closed'
    public boolean checked;
    public Object data;
    public List<JsonTreeData> children;       //
    private boolean disabled;

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getState() {
        if(!CollectionUtils.isEmpty(children)){
            return "open";
        }else{
            return "close";
        }
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setState(String state) {
        this.state = state;
    }
    public List<JsonTreeData> getChildren() {
        return children;
    }
    public void setChildren(List<JsonTreeData> children) {
        this.children = children;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
