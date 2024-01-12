package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DishServiceImpl implements DishService {
  @Autowired
  private DishMapper dishMapper;
  @Autowired
  private DishFlavorMapper dishFlavorMapper;

  /**
   * 新增菜品和对应口味
   * @param dishDTO
   */
  @Transactional
  public void saveWithFlavor(DishDTO dishDTO) {

    Dish dish = new Dish();
    BeanUtils.copyProperties(dishDTO,dish);
    //向菜品表插入1条数据
    dishMapper.insert(dish);

    //获取insert语句生成的主键值
    Long dishId = dish.getId();

    List<DishFlavor> flavors = dishDTO.getFlavors();
    if(flavors != null && flavors.size() >0){
      flavors.forEach(dishFlavor ->{
        dishFlavor.setDishId(dishId);
      } );
      //向口味表插入多条数据
      dishFlavorMapper.insertBatch(flavors);
    }

  }
}
