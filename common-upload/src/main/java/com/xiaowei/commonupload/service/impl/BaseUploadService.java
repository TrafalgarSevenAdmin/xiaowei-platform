package com.xiaowei.commonupload.service.impl;


import com.xiaowei.commonupload.entity.FileStore;
import com.xiaowei.commonupload.model.FileModel;
import com.xiaowei.commonupload.model.SchemeModel;
import com.xiaowei.commonupload.service.IUploadService;
import com.xiaowei.core.exception.BusinessException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author mocker
 */
public class BaseUploadService implements IUploadService {

    /**
     * byte数组转换成16进制字符串
     *
     * @param src
     * @return
     */
    public String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public void validateType(FileModel fileModel, SchemeModel schemeModel) {
        /**
         * 用文件头判断。直接读取文件的前几个字节。 常用文件的文件头如下： JPEG (jpg)，文件头：FFD8FF PNG (png)，文件头：89504E47
         * GIF (gif)，文件头：47494638 TIFF (tif)，文件头：49492A00 Windows Bitmap (bmp)，文件头：424D
         * CAD (dwg)，文件头：41433130 Adobe Photoshop (psd)，文件头：38425053 Rich Text Format
         * (rtf)，文件头：7B5C727466 XML (xml)，文件头：3C3F786D6C HTML (html)，文件头：68746D6C3E
         * Email [thorough only] (eml)，文件头：44656C69766572792D646174653A Outlook Express
         * (dbx)，文件头：CFAD12FEC5FD746F Outlook (pst)，文件头：2142444E MS Word/Excel
         * (xls.or.doc)，文件头：D0CF11E0 MS Access (mdb)，文件头：5374616E64617264204A
         * WordPerfect (wpd)，文件头：FF575043 Postscript.
         * (eps.or.ps)，文件头：252150532D41646F6265 Adobe Acrobat (pdf)，文件头：255044462D312E
         * Quicken (qdf)，文件头：AC9EBD8F Windows Password (pwl)，文件头：E3828596 ZIP Archive
         * (zip)，文件头：504B0304 RAR Archive (rar)，文件头：52617221 Wave (wav)，文件头：57415645 AVI
         * (avi)，文件头：41564920 Real Audio (ram)，文件头：2E7261FD Real Media (rm)，文件头：2E524D46
         * MPEG (mpg)，文件头：000001BA MPEG (mpg)，文件头：000001B3 Quicktime (mov)，文件头：6D6F6F76
         * Windows Media (asf)，文件头：3026B2758E66CF11 MIDI (mid)，文件头：4D546864
         */

        //预先抽象好这种验证模式，后续需要再次基础扩展
        //暂时不需要使用安全等级高的上传，未测试如果出现错误 基本是十六进制字符串错了，网上有图片现成类型的十六进制
        //需要注意别再此方法之前使用流，否则会出现错误，如果使用了需要重置IO流
        switch (schemeModel.getFileType()) {
            case IMG:
                try {
                    byte[] b = new byte[4];
                    fileModel.getIn().read(b, 0, b.length);
                    fileModel.getIn().reset();
                    String type = bytesToHexString(b).toUpperCase();
                    if (!(type.contains("FFD8FF") || type.contains("89504E47") || type.contains("47494638")
                            || type.contains("49492A00") || type.contains("424D"))) {
                        throw new BusinessException("文件类型错误!");
                    }
                } catch (IOException e) {
                    throw new BusinessException("上传文件出错!");
                }
        }
    }

    public void validateMaxSize(FileModel fileModel, SchemeModel schemeModel) {
        /**
         * 未测试如果出现错误 可能就是就是kb计算出错
         */
        try {
            if (schemeModel.getMaxSize() < fileModel.getIn().available() / 1024) {
                throw new BusinessException("文件超过限制下载大小!");
            }
        } catch (IOException e) {
            throw new BusinessException("上传文件出错!");
        }
    }

    public void validate(FileModel fileModel, SchemeModel schemeModel) {
        if (schemeModel != null) {
            if (schemeModel.getFileType() != null) {
                validateType(fileModel, schemeModel);
            } else if (schemeModel.getMaxSize() != null) {
                validateMaxSize(fileModel, schemeModel);
            }
        }

    }

    @Override
    public FileStore upload(FileModel fileModel) {
        return null;
    }

    @Override
    public FileStore upload(FileModel fileModel, SchemeModel schemeModel) {
        return null;
    }

    @Override
    public InputStream getFile(String filePath) throws FileNotFoundException {
        return null;
    }
}
