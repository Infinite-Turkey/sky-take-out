package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

  @Autowired
  private OrderMapper orderMapper;
  @Autowired
  private UserMapper userMapper;

  /**
   * 统计指定时间区间内的营业额数据
   * @param begin
   * @param end
   * @return
   */
  public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
     //当前集合用于存放从begin到end范围内的每天的日期
    List<LocalDate> dateList = new ArrayList<>();

    dateList.add(begin);

    while(!begin.equals(end)){
      //日期计算，计算指定日期的后一天对应的日期
      begin = begin.plusDays(1);
      dateList.add(begin);
    }

    //存放每日营业额
    List<Double> turnoverList = new ArrayList<>();
    for(LocalDate date: dateList){
      //查询date日期对用的营业额数据，营业额是指：状态为"已完成"的订单金额总计
      LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
      LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

      //select sum(amount) from orders where order_time > beginTime and order_time < endTime and status =5
      Map map = new HashMap();
      map.put("begin",beginTime);
      map.put("end",endTime);
      map.put("status", Orders.COMPLETED);
      Double turnover = orderMapper.sumByMap(map);
      turnover = turnover == null? 0.0:turnover;
      turnoverList.add(turnover);
    }

    //封装返回结果
    return TurnoverReportVO
            .builder()
            .dateList(StringUtils.join(dateList, ","))
            .turnoverList(StringUtils.join(turnoverList,","))
            .build();
  }

  /**
   * 统计指定时间区间内的用户数据
   * @param begin
   * @param end
   * @return
   */
  public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
    //当前集合用于存放从begin到end范围内的每天的日期
    List<LocalDate> dateList = new ArrayList<>();

    dateList.add(begin);

    while(!begin.equals(end)){
      //日期计算，计算指定日期的后一天对应的日期
      begin = begin.plusDays(1);
      dateList.add(begin);
    }

    //存放每天的新增用户数量 select count(id) from user where create_time < ? and create_time > ?
    List<Integer> newUserList = new ArrayList<>();
    //存放每天的总用户数量 select count(id) from user where create_time < ?
    List<Integer> totalUserList = new ArrayList<>();

    for (LocalDate date: dateList){
      //查询date日期对用的用户数据
      LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
      LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

      Map map = new HashMap();
      map.put("end",endTime);

      //总用户数量
      Integer totalUser = userMapper.countByMap(map);

      map.put("begin",beginTime);
      //新增用户数量
      Integer newUser = userMapper.countByMap(map);

      totalUserList.add(totalUser);
      newUserList.add(newUser);
    }

    //封装结果数据
    return UserReportVO.builder()
            .dateList(StringUtils.join(dateList,","))
            .totalUserList(StringUtils.join(totalUserList,","))
            .newUserList(StringUtils.join(newUserList,","))
            .build();
  }

  /**
   * 统计指定时间区间内的订单数据
   * @param begin
   * @param end
   * @return
   */
  public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
    //当前集合用于存放从begin到end范围内的每天的日期
    List<LocalDate> dateList = new ArrayList<>();

    dateList.add(begin);

    while(!begin.equals(end)){
      //日期计算，计算指定日期的后一天对应的日期
      begin = begin.plusDays(1);
      dateList.add(begin);
    }

    //存放每天的有效订单数和订单总数
    List<Integer> orderCountList = new ArrayList<>();
    List<Integer> validOrderCountList = new ArrayList<>();

    //遍历dateList集合，查询每天有效订单数和订单总数
    for(LocalDate date: dateList){
      LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
      LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

      //查询每天订单总数 select count(id) from orders where order_time > beginTime and order_time < endTime
      Integer orderCount = getOrderCount(beginTime, endTime, null);
      orderCountList.add(orderCount);
      //查询每天的有效订单数 select count(id) from orders where order_time > ? and order_time < ? and status = 5
      Integer validOrderCount = getOrderCount(beginTime, endTime, Orders.COMPLETED);
      validOrderCountList.add(validOrderCount);
    }

    //计算时间区间内的订单总数量
    Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();

    //计算时间区间内有效订单数量
    Integer validOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();

    //订单完成率
    Double orderCompletionRate = 0.0;
    if(totalOrderCount != 0){
      orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
    }
    //封装返回结果
    return OrderReportVO
            .builder()
            .dateList(StringUtils.join(dateList, ","))
            .orderCountList(StringUtils.join(orderCountList, ","))
            .validOrderCountList(StringUtils.join(validOrderCountList, ","))
            .totalOrderCount(totalOrderCount)
            .validOrderCount(validOrderCount)
            .orderCompletionRate(orderCompletionRate)
            .build();
  }

  /**
   * 根据条件统计订单数量
   * @param begin
   * @param end
   * @param status
   * @return
   */
  private Integer getOrderCount(LocalDateTime begin, LocalDateTime end, Integer status){
    Map map = new HashMap();
    map.put("begin",begin);
    map.put("end",end);
    map.put("status",status);

    return orderMapper.countByMap(map);
  }

  /**
   * 统计指定时间区间内的销量排名top10
   * @param begin
   * @param end
   * @return
   */
  public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
    LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
    LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

    List<GoodsSalesDTO> salesTop10 = orderMapper.getSalesTop10(beginTime, endTime);

    List<String> names = salesTop10.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
    String nameList = StringUtils.join(names,",");

    List<Integer> numbers = salesTop10.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());
    String numberList = StringUtils.join(numbers,",");

    return SalesTop10ReportVO.builder()
            .numberList(numberList)
            .nameList(nameList)
            .build();
  }
}
