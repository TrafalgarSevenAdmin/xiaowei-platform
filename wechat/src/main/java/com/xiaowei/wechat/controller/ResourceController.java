package com.xiaowei.wechat.controller;

import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.commonupload.UploadConfigBean;
import com.xiaowei.commonupload.entity.FileStore;
import com.xiaowei.commonupload.model.FileModel;
import com.xiaowei.commonupload.service.IUploadService;
import com.xiaowei.core.exception.BusinessException;
import com.xiaowei.core.result.Result;
import com.xiaowei.wechat.dto.ResourceUploadDto;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/resource")
public class ResourceController {

    @Autowired
    WxMpService wxMpService;

    @Autowired
    private IUploadService uploadService;

    @Autowired
    private UploadConfigBean uploadConfigBean;

    @ApiOperation(value = "资源文件上传")
    @PostMapping("/{tag}")
    public Result upload(@PathVariable("tag") String tag, @RequestBody  ResourceUploadDto resourceUpload) throws IOException, WxErrorException {
        File file = wxMpService.getMaterialService().mediaDownload(resourceUpload.getMediaId());
        FileModel fileModel = new FileModel();

        String[] tags = uploadConfigBean.getTags();

        try {
            fileModel.setIn(new FileInputStream(file));
        } catch (Exception e) {
            throw new BusinessException("读取上传文件失败!");
        }
        //后缀名
        String extension = FilenameUtils.getExtension(file.getName());
        fileModel.setFileName(UUID.randomUUID().toString() + "." + extension);
        fileModel.setOriginalFilename(file.getName());
        if (!ArrayUtils.contains(tags, tag)) {
            throw new BusinessException("你标记的类型不存在!");
        }
        fileModel.setRelativePath(tag);
        fileModel.setUserId(LoginUserUtils.getLoginUser().getId());

        FileStore fileStore = uploadService.upload(fileModel);
        Map<String, Object> map = new HashMap<>();
        map.put("id", fileStore.getId());
        map.put("path", uploadConfigBean.getAccessUrlRoot() + fileStore.getPath());
        //删除临时文件
        file.delete();
        return Result.getSuccess(map);
    }
}
