package com.e_commerce.service;

import com.e_commerce.dto.OrderHistInfo;
import com.e_commerce.dto.OrderInfo;
import com.e_commerce.dto.OrderItemInfo;
import com.e_commerce.entity.*;
import com.e_commerce.exception.CustomException;
import com.e_commerce.repository.ItemImgRepository;
import com.e_commerce.repository.ItemRepository;
import com.e_commerce.repository.OrderRepository;
import com.e_commerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.e_commerce.exception.ErrorCode.NO_MATCHING_ITEM;
import static com.e_commerce.exception.ErrorCode.NO_MATCHING_USER;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final OrderRepository orderRepository;

    private final ItemImgRepository itemImgRepository;

    @Transactional
    public void order(OrderInfo orderInfo, String username) {
        Item item = itemRepository.findById(orderInfo.getItemId())
                .orElseThrow(() -> new CustomException(NO_MATCHING_ITEM));
        User user = getUser(username);

        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createItem(item, orderInfo.getCount());
        orderItemList.add(orderItem);

        Order order = Order.createOrder(user, orderItemList);
        orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Page<OrderHistInfo> getOrderList(String username, Pageable pageable) {

        List<Order> orders = orderRepository.findOrders(username, pageable);
        Long totalCount = orderRepository.countOrder(username);

        List<OrderHistInfo> orderHistInfos = new ArrayList<>();

        for (Order order : orders) {
            OrderHistInfo orderHistInfo = new OrderHistInfo(order);
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn(orderItem.getItem().getId(), "Y");
                OrderItemInfo orderItemInfo = new OrderItemInfo(orderItem, itemImg.getImgUrl());
                orderHistInfo.addOrderItemInfo(orderItemInfo);
            }
            orderHistInfos.add(orderHistInfo);
        }
        return new PageImpl<OrderHistInfo>(orderHistInfos, pageable, totalCount);
    }

    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(NO_MATCHING_ITEM));
        order.cancelOrder();
    }

    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String username) {
        User user = getUser(username);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(NO_MATCHING_ITEM));
        User savedUser = order.getUser();

        if (!user.getUsername().equals(savedUser.getUsername())) {
            return false;
        }
        return true;
    }


    public void orders(List<OrderInfo> orderInfoList, String username) {
        User user = getUser(username);
        List<OrderItem> orderItemList = new ArrayList<>();

        for (OrderInfo orderInfo : orderInfoList) {
            Item item = itemRepository.findById(orderInfo.getItemId())
                    .orElseThrow(() -> new CustomException(NO_MATCHING_ITEM));

            OrderItem orderItem = OrderItem.createItem(item, orderInfo.getCount());
            orderItemList.add(orderItem);
        }

        Order order = Order.createOrder(user, orderItemList);
        orderRepository.save(order);

    }

    private User getUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(NO_MATCHING_USER));
        return user;
    }
}
