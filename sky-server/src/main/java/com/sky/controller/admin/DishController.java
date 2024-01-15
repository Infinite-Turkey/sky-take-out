package com.sky.controller.admin;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

  /**
   * 菜品分页查询
   * @param dishPageQueryDTO
   * @return
   */
  @GetMapping("/page")
  @ApiOperation("菜品分页查询")
  //DishPageQueryDTO是query格式的，不是json格式，所以不需要@RequestBody
  public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
    log.info("菜品分页查询：{}", dishPageQueryDTO);
    PageResult pageResult= dishService.pageQuery(dishPageQueryDTO);
    return Result.success(pageResult);
  }

  /**
   * 批量删除菜品
   * @param ids
   * @return
   */
  @DeleteMapping
  @ApiOperation("批量删除菜品")
  public Result delete(@RequestParam List<Long> ids){
    log.info("批量删除菜品：{}", ids);
    dishService.deleteBatch(ids);
    return Result.success();
  }

  /**
   * 根据id查询菜品
   * @param id
   * @return
   */
  @GetMapping("/{id}")
  @ApiOperation("根据id查询菜品")
  public Result<DishVO> getById(@PathVariable Long id){
    log.info("根据id查询菜品：{}", id);
    DishVO dishVO = dishService.getByIdWithFlavor(id);
    return Result.success(dishVO);
  }

  /**
   * 修改菜品
   * @param dishDTO
   * @return
   */
  @PutMapping
  @ApiOperation("修改菜品")
  public Result update(@RequestBody DishDTO dishDTO){
    log.info("修改菜品：{}", dishDTO);
    dishService.updateWithFlavor(dishDTO);
    return Result.success();
  }

  /**
   * 根据分类id查询菜品
   * @param categoryId
   * @return
   */
  @GetMapping("/list")
  @ApiOperation("根据分类id查询菜品")
  public Result<List<Dish>> list(Long categoryId){
    List<Dish> list = dishService.list(categoryId);
    return Result.success(list);
  }

  /**
   * 菜品起售停售
   * @param status
   * @param id
   * @return
   */
  @PostMapping("/status/{status}")
  @ApiOperation("菜品起售停售")
  public Result<String> startOrStop(@PathVariable Integer status, Long id){
    dishService.startOrStop(status, id);
    return Result.success();
  }
}
