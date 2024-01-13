package com.sky.controller.admin;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
  public Result save(@RequestBody DishDTO dishDTO){
    log.info("新增菜品：{}", dishDTO);
    dishService.saveWithFlavor(dishDTO);
    return Result.success();
  }

  @GetMapping("/page")
  @ApiOperation("菜品分页查询")
  //DishPageQueryDTO是query格式的，不是json格式，所以不需要@RequestBody
  public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
    log.info("菜品分页查询：{}", dishPageQueryDTO);
    PageResult pageResult= dishService.pageQuery(dishPageQueryDTO);
    return Result.success(pageResult);
  }

}
