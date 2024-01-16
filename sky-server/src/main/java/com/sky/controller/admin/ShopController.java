package com.sky.controller.admin;

import com.sky.result.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {

  @Autowired
  private RedisTemplate redisTemplate;

  public static final String KEY = "SHOP_STATUS";

  /**
   * 设置店铺的营业状态
   * @param status
   * @return
   */
  @PutMapping("/{status}")
  @ApiOperation("设置店铺的营业状态")
  public Result setStatus(@PathVariable Integer status){
    log.info("设置店铺的营业状态: {}", status == 1? "营业中" : "打烊中");
    redisTemplate.opsForValue().set(KEY,status);
    return Result.success();
  }

  /**
   * 获取店铺营业状态
   * @return
   */
  @GetMapping("/status")
  @ApiOperation("获取店铺的营业状态")
  public Result<Integer> getStatus(){
    Integer shopStatus = (Integer) redisTemplate.opsForValue().get(KEY);
    log.info("获取到的店铺营业状态为：{}", shopStatus == 1? "营业中": "打烊中");
    return Result.success(shopStatus);
  }
}
