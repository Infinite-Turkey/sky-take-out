package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 通用接口
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {

  @Autowired
  private AliOssUtil aliOssUtil;

  /**
   * 文件上传
   * @param file
   * @return
   */

  //图片上传可以，但是无法从阿里云获取图片，大概率是黑马程序员的问题，后续改写为从本地获取图片
  @PostMapping("/upload")
  @ApiOperation("文件上传")
  public Result<String> upload(MultipartFile file){
    log.info("文件上传：{}",file);

    try {
      //原始文件名
      String originalFilename = file.getOriginalFilename();
      //截取原始文件名的后缀 --- .jpg
      String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
      //构建新文件名称
      String objectName =  UUID.randomUUID().toString()+ extension;
      //文件的请求路径
      String filePath = aliOssUtil.upload(file.getBytes(), objectName);
      return Result.success(filePath);
    } catch (IOException e) {
      log.error("文件上传失败：{}", e);
    }

    return Result.error(MessageConstant.UPLOAD_FAILED);
  }

}
