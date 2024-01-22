package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

  @Autowired
  private ShoppingCartMapper shoppingCartMapper;
  @Autowired
  private DishMapper dishMapper;
  @Autowired
  private SetmealMapper setmealMapper;

  /**
   * 添加到购物车
   * @param shoppingCartDTO
   */
  public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
    //判断当前加入购物车的商品是否已经存在了
    ShoppingCart shoppingCart = new ShoppingCart();
    BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
    Long userId = BaseContext.getCurrentId();
    shoppingCart.setUserId(userId);

    List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
    //如果已经存在，只需要数量+1
    if(list != null && list.size() >0){
      ShoppingCart cart = list.get(0);
      cart.setNumber(cart.getNumber()+1); //update shopping_cart set number = ? where id = ?
      shoppingCartMapper.updateNumberById(cart);
    }else {
      //如果不存在，需要插入一条数据
      //判断本次添加到购物车的是菜品还是套餐
      Long dishId = shoppingCartDTO.getDishId();
      if (dishId != null){
        //本次添加到购物车的是菜品
        Dish dish = dishMapper.getById(dishId);
        shoppingCart.setName(dish.getName());
        shoppingCart.setImage(dish.getImage());
        shoppingCart.setAmount(dish.getPrice());
      }else {
        //本次添加到购物车的是套餐
        Long setmealId = shoppingCartDTO.getSetmealId();

        Setmeal setmeal = setmealMapper.getById(setmealId);
        shoppingCart.setName(setmeal.getName());
        shoppingCart.setImage(setmeal.getImage());
        shoppingCart.setAmount(setmeal.getPrice());
      }
      shoppingCart.setNumber(1);
      shoppingCart.setCreateTime(LocalDateTime.now());
      shoppingCartMapper.insert(shoppingCart);

    }
  }


  /**
   * 展示购物车
   * @return
   */
  public List<ShoppingCart> showShoppingCart() {
    //获取当前微信用户的id
    Long userId = BaseContext.getCurrentId();
    ShoppingCart shoppingCart = ShoppingCart.builder().userId(userId).build();
    List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
    return list;
  }

  /**
   * 清空购物车
   */
  public void cleanShoppingCart() {
    Long userId = BaseContext.getCurrentId();
    shoppingCartMapper.deleteByUserId(userId);
  }
}
