package com.sky.controller.admin;


import com.sky.dto.DishDTO;
import com.sky.result.Result;
import com.sky.service.DishService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {

  @Autowired
  private DishService dishService;

  /**
   * 新增菜品
   * @param dishDTO
   * @return
   */
  @PostMapping
  @ApiOperation("新增菜品")
  public Result save(DishDTO dishDTO){
    log.info("新增菜品：{}", dishDTO);
    return Result.success();
  }
}
