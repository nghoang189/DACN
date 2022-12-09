package com.edu.hutech.major.service.impl;

import com.edu.hutech.major.dto.OrderDTO;
import com.edu.hutech.major.model.Order;
import com.edu.hutech.major.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OrderServiceImpl {
    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }//findAll

    public void updateOrder(Order Order) {
        orderRepository.save(Order);
    }//add or update (tuy vao pri-key)

    public void removeOrderById(long id) {
        orderRepository.deleteById(id);
    }//delete dua vao pri-key

    public Optional<Order> getOrderById(long id) {
        return orderRepository.findById(id);
    }//search theo id

//    public void aaa(OrderDTO dto) {
//        dto
//    }
    //not done yet
}
