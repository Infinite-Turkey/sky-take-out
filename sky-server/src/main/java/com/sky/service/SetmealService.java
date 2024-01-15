package com.sky.service;

import com.sky.dto.SetmealDTO;

public interface SetmealService {
  /**
   * 新增套餐，同时需要保存套餐和菜品之间的关联关系
   * @param setmealDTO
   */
  void saveWithDish(SetmealDTO setmealDTO);
}
