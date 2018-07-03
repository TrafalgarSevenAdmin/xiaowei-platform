package com.xiaowei.accountweb.utils;

import com.xiaowei.commonupload.UploadConfigBean;
import com.xiaowei.commonupload.service.IFileStoreService;
import org.springframework.stereotype.Component;

@Component
public class FileUtils {

    private static IFileStoreService fileStoreService;

    private static UploadConfigBean uploadConfigBean;

//    /**
//     * 上传图片
//     *
//     * @return
//     */
//    public static List<Map<String,Object>> uploadPic(String url, MultipartFile[] multipartFiles) throws Exception {
//        if (fileStoreService == null) {
//            fileStoreService = ContextUtils.getApplicationContext().getBean(IFileStoreService.class);
//        }
//        if(uploadConfigBean==null){
//            uploadConfigBean = ContextUtils.getApplicationContext().getBean(UploadConfigBean.class);
//        }
//        String userId = LoginUserUtils.getLoginUser().getId();
//        if (multipartFiles == null || multipartFiles.length == 0) {
//            return null;
//        }
//        //文件名集合
//        List<Map<String,Object>> imgs = new LinkedList<>();
//        for (MultipartFile multipartFile : multipartFiles) {
//            byte[] bytes = multipartFile.getBytes();
//            String name = UUID.randomUUID().toString() + ".jpg";
//            File file = new File(url);
//            //如果文件不存在,创建文件夹
//            if (!file.exists()) {
//                file.mkdirs();
//            }
//            try (OutputStream outputStream = new FileOutputStream(new File(file, name))) {
//                //输出文件
//                outputStream.write(bytes);
//            }
//            FileStore fileStore = new FileStore();
//            fileStore.setOriginalFilename(multipartFile.getOriginalFilename());
//            fileStore.setUserId(userId);
//            fileStore.setPath(name);
//            fileStore.setSize(multipartFile.getSize());
//            fileStore.setCreatedTime(new Date());
//            fileStore = fileStoreService.save(fileStore);
//            Map<String,Object> map = new HashMap<>();
//            map.put("id",fileStore.getId());
//            map.put("path",uploadConfigBean.getServerPath() + uploadConfigBean.getCompanyLogo() + "/" + fileStore.getPath());
//            imgs.add(map);
//        }
//
//
//        return imgs;
//
//    }
}
