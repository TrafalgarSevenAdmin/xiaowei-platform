package com.xiaowei.commondict.dto;

import lombok.Data;

@Data
public class DictionaryDTO {
    /**
     * 字典独立编码
     */
    private String ownCode;
    /**
     * 字典名称
     */
    private String name;
    /**
     * 父级id
     */
    private String parentId;
}
