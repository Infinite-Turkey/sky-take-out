package com.sky.service.impl;

import com.sky.dto.SetmealDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.service.SetmealService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

  @Autowired
  private SetmealMapper setmealMapper;
  @Autowired
  private SetmealDishMapper setmealDishMapper;
  @Autowired
  private DishMapper dishMapper;

  /**
   * 新增套餐，同时需要保存套餐和菜品之间的关联关系
   * @param setmealDTO
   */
  @Transactional
  public void saveWithDish(SetmealDTO setmealDTO) {
    Setmeal setmeal = new Setmeal();
    BeanUtils.copyProperties(setmealDTO,setmeal);

    //向套餐表插入数据
    setmealMapper.insert(setmeal);
    //获取生成的套餐id
    Long setmealId = setmeal.getId();

    List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
    setmealDishes.forEach(setmealDish -> {
      setmealDish.setSetmealId(setmealId);
    });
    //保存菜品与套餐的关联关系
    setmealDishMapper.insertBatch(setmealDishes);
  }
}
