package com.xiaowei.commonupload;

import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.commonupload.entity.FileStore;
import com.xiaowei.commonupload.model.FileModel;
import com.xiaowei.commonupload.service.IFileStoreService;
import com.xiaowei.commonupload.service.IUploadService;
import com.xiaowei.core.exception.BusinessException;
import com.xiaowei.core.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Api(tags = "通用上传")
@RequestMapping("/api/upload")
@ResponseBody
public class BaseUploadController {
    @Autowired
    private IUploadService uploadService;
    @Autowired
    private IFileStoreService fileStoreService;

    @Autowired
    private UploadConfigBean uploadConfigBean;

    @ApiOperation(value = "文件上传")
    @PostMapping("/{tag}")
    public Result upload(@PathVariable("tag") String tag, @RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        FileModel fileModel = new FileModel();

        String[] tags = uploadConfigBean.getTags();

        try {
            fileModel.setIn(file.getInputStream());
        } catch (IOException e) {
            throw new BusinessException("读取上传文件失败!");
        }
        //后缀名
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        fileModel.setFileName(UUID.randomUUID().toString() + "." + extension);
        fileModel.setOriginalFilename(file.getOriginalFilename());
        fileModel.setIn(file.getInputStream());
        if (!ArrayUtils.contains(tags, tag)) {
            throw new BusinessException("你标记的类型不存在!");
        }
        fileModel.setRelativePath(tag);
        fileModel.setUserId(LoginUserUtils.getLoginUser().getId());


        FileStore fileStore = uploadService.upload(fileModel);
        Map<String, Object> map = new HashMap<>();
        map.put("id", fileStore.getId());
        map.put("path", uploadConfigBean.getAccessUrlRoot() + fileStore.getPath());

        return Result.getSuccess(map);
    }
    @ApiOperation(value = "根据id数组获取文件")
    @GetMapping("/byid")
    public Result upload(@RequestParam("ids") String[] ids){
        if(ids.length==0){
            return null;
        }
        Set<String> fileIds = new HashSet<>();
        for (String id : ids) {
            fileIds.addAll(Arrays.stream(id.split(";")).collect(Collectors.toSet()));
        }
        final List<FileStore> files = fileStoreService.findByIdIn(fileIds);
        Map<String,List> stringListMap = new HashMap<>();
        for (String id : ids) {
            String[] split = id.split(";");
            List fileList = files.stream().filter(fileStore -> ArrayUtils.contains(split,fileStore.getId())).collect(Collectors.toList());
            stringListMap.put(id,fileList);
        }
        return Result.getSuccess(stringListMap);
    }

}
