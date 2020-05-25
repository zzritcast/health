/*
package com.itheima.test;


import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

public class QiNiuTest {
    */
/**
     * 上传空间文件
     *//*

    @org.junit.Test
    public void uploadLoad(){
        //使用七牛云提供的SDK实现将本地图片上传到七牛云服务器
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        //...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "5DADWf84WAQ4hn4xz4X3HOWfshMHlTOs79P3wvbW";
        String secretKey = "fWpOh3LrxGE2tBNoGEquWuOcwb09yKpUhGKd3xyu";
        String bucket = "health-heima-space-1";

        //如果是Windows情况下，格式是 D:\\qiniu\\test.png  存储图片的位置
        String localFilePath = "D:\\图片\\03a36073-a140-4942-9b9b-712cecb144901.jpg";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        //  也可以用uiid
        //String key = "a.jpg";
         String key = null;

        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }
    */
/**
     * 删除空间中的文件
     *//*

    @org.junit.Test
    public void delete(){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        //...其他参数参考类注释

        //...生成上传凭证，然后准备上传
        String accessKey = "5DADWf84WAQ4hn4xz4X3HOWfshMHlTOs79P3wvbW";
        String secretKey = "fWpOh3LrxGE2tBNoGEquWuOcwb09yKpUhGKd3xyu";

        String bucket = "health-heima-space-1";
        //七牛云的图片名字
        String key = "a.jpg";

        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }

}
*/
