package com.xiaowei.wechat.controller;

import com.alibaba.fastjson.JSONObject;
import com.xiaowei.account.utils.ConfigUtils;
import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.commonupload.UploadConfigBean;
import com.xiaowei.commonupload.entity.FileStore;
import com.xiaowei.commonupload.model.FileModel;
import com.xiaowei.commonupload.service.IUploadService;
import com.xiaowei.core.exception.BusinessException;
import com.xiaowei.core.result.Result;
import com.xiaowei.wechat.consts.MagicValueStore;
import com.xiaowei.wechat.dto.InvitationInfoDto;
import com.xiaowei.wechat.dto.ResourceUploadDto;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.util.fs.FileUtils;
import me.chanjar.weixin.common.util.http.HttpResponseProxy;
import me.chanjar.weixin.common.util.http.apache.ApacheHttpClientBuilder;
import me.chanjar.weixin.common.util.http.apache.DefaultApacheHttpClientBuilder;
import me.chanjar.weixin.common.util.http.apache.InputStreamResponseHandler;
import me.chanjar.weixin.common.util.http.apache.Utf8ResponseHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/resource")
public class ResourceController {

    @Autowired
    WxMpService wxMpService;

    @Autowired
    private IUploadService uploadService;

    @Autowired
    private UploadConfigBean uploadConfigBean;

    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;

    @ApiOperation(value = "资源文件上传")
    @PostMapping("/{tag}")
    public Result upload(@PathVariable("tag") String tag, @RequestBody  ResourceUploadDto resourceUpload) throws IOException, WxErrorException {
        File file = wxMpService.getMaterialService().mediaDownload(resourceUpload.getMediaId());
        log.info(file.getAbsolutePath());
        FileModel fileModel = new FileModel();

        String[] tags = uploadConfigBean.getTags();

        try {
            fileModel.setIn(new FileInputStream(file));
        } catch (Exception e) {
            log.error(e.toString(),e);
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
        map.put("path", fileStore.getPath());
        //删除临时文件
        file.delete();
        return Result.getSuccess(map);
    }

    @ApiOperation(value = "获取邀请二维码图片")
    @GetMapping("/invitation/qr")
    public void createTmpTicket(InvitationInfoDto invitationInfo, HttpServletResponse httpResponse) throws IOException, WxErrorException {
        //由于换取临时二维码时，场景字符串限制长度为64，因此邀请信息只能保存到reids中
        Integer expireSeconds = Integer.valueOf(ConfigUtils.getConfigValueOrDefault(MagicValueStore.WechatInvitationExpireSecondsSetKey, "3600"));
        String sceneStr = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(sceneStr, invitationInfo, expireSeconds + 60, TimeUnit.SECONDS);
        WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(sceneStr, expireSeconds);//获取临时关注二维码
        String url = wxMpQrCodeTicket.getUrl();
        HttpGet httpGet = new HttpGet(url);
        try (CloseableHttpResponse response = DefaultApacheHttpClientBuilder.get().build().execute(httpGet);
             InputStream inputStream = InputStreamResponseHandler.INSTANCE.handleResponse(response)) {
            Header[] contentTypeHeader = response.getHeaders("Content-Type");
            IOUtils.copy(inputStream, httpResponse.getOutputStream());
            response.setHeader(contentTypeHeader[0]);
        } finally {
            httpGet.releaseConnection();
        }
    }
}
