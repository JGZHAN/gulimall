package com.zhan.gulimall.product;

import com.aliyun.oss.OSSClient;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhan.gulimall.product.entity.BrandEntity;
import com.zhan.gulimall.product.service.BrandService;
import com.zhan.gulimall.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/*
* 1、引入oss-starter
* 2、配置key，endpoint等相关信息即可
* 3、使用oosClient进行相关操作
*
*
* */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
class GulimallProductApplicationTests {

    @Autowired
    BrandService brandService;
//
//    @Autowired
//    OSSClient ossClient;

    @Autowired
    CategoryService categoryService;

    @Test
    public void testFindPath(){
        Long[] catelogPath=categoryService.findCatelogPath(225L);
        log.info("完整路径{}", Arrays.asList(catelogPath));
    }



    @Test
    public void testUpload() throws FileNotFoundException {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "oss-cn-guangzhou.aliyuncs.com";
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
//        String accessKeyId = "LTAI4GAPjeg7cCquVTN8fxw7";
//        String accessKeySecret = "J85zLM8uM4Kdm9nUblECD1FDbz5nAR";
//
//        // 创建OSSClient实例。
//        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传文件流。
//        InputStream inputStream = new FileInputStream("C:\\Users\\ZHAN\\Pictures\\信息安全\\watermark.bmp");
//        ossClient.putObject("gulimall-hhello", "watermark.jpg", inputStream);
//
//        // 关闭OSSClient。
//        ossClient.shutdown();
//        System.out.println("上传完成。。。");
    }

    @Test
    void contextLoads() {

//        BrandEntity brandEntity=new BrandEntity();
//        brandEntity.setName("华为");
//        brandService.save(brandEntity);
//        System.out.println("保存成功！");
//        BrandEntity brandEntity=new BrandEntity();
//        brandEntity.setBrandId(1L);
//        brandEntity.setDescript("华为");
//        brandService.updateById(brandEntity);
        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id ", 1L));
        for (BrandEntity brandEntity : list
        ) {
            System.out.println("brandEntity = " + brandEntity);
        }

    }

}
