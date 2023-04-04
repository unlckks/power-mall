package com.mingyun.controller;


import com.alibaba.fastjson.JSON;
import com.mingyun.config.QiniuConfig;
import com.mingyun.constant.BusinessEnum;
import com.mingyun.model.Result;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;

import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;


/**
 * 七牛云工具类
 * @author linfen
 * @since 2020-04-8
 */
@RestController
@Api(tags = "文件管理接口")
@RequestMapping("admin/file")
public class QiNiuController {
 
    @Resource
    private QiniuConfig qiniuConfig;
 
    /**
     * 七牛云文件上传
     *
     * @param file 文件
     * @return
     */
    @PostMapping("upload/element")
    @ApiOperation("文件上传")
    public Result<String> uploadFile(MultipartFile file) {


        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.huanan());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "qZlSCD-9qcrYjP-qJoJRzOAhhONyLpFJt4c3ct_s";
        String secretKey = "rY6kZHyCf_cLyK2kwSagCATHxw2UAb0PSpd9OEo0";
        String bucket = "wh2211-mall";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            StringMap stringMap = new StringMap();
            stringMap.put("x-qn-meta-tag", "1.0");
            Response response = uploadManager.put(file.getInputStream(), key, upToken, stringMap, null);
            //解析上传成功的结果
            DefaultPutRet defaultPutRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
            // 返回一个地址 名字  rskx7xddy.hn-bkt.clouddn.com/defaultPutRet.hash
            return Result.success("http://rskx7xddy.hn-bkt.clouddn.com/" + defaultPutRet.hash);
        } catch (Exception ex) {
            return Result.fail(BusinessEnum.SERVER_INNER_ERROR);
        }


    /*    //构造一个带指定 Region 对象的配置类
       qiniuConfig.setRegion(Region.huanan());
        //...其他参数参考类注释
        qiniuConfig.uploadManager(cfg);
        String key = null;
        Auth auth = Auth.create(qiniuConfig.getAccessKey(), qiniuConfig.getSecretKey());
        String upToken = auth.uploadToken(qiniuConfig.bucketManager().getBucketInfo());
        StringMap stringMap = new StringMap();
        stringMap.put("x-qn-meta-tag", "1.0");
        qiniuConfig.uploadManager(file,stringMap);
        */



    }
 

}