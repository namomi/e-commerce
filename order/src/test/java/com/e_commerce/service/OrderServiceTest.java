package com.e_commerce.service;

import com.e_commerce.constant.ItemSellStatus;
import com.e_commerce.constant.OrderStatus;
import com.e_commerce.constant.ProductStatus;
import com.e_commerce.dto.OrderInfo;
import com.e_commerce.entity.Item;
import com.e_commerce.entity.Order;
import com.e_commerce.entity.OrderItem;
import com.e_commerce.entity.User;
import com.e_commerce.repository.ItemRepository;
import com.e_commerce.repository.OrderRepository;
import com.e_commerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private Item item;
    private User user;

    private Order order;

    private OrderItem orderItem;

    @BeforeEach
    void setUp() {
        item = new Item(1L, "Test Item", 100, 10, "test", ProductStatus.NEW, ItemSellStatus.SELL); // 가상의 Item 객체 생성
        user = new User(1L, "testUser", "password", "testUser@example.com", null, null, null); // 가상의 User 객체 생성
        orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setItem(item);
        orderItem.setCount(10);
        orderItem.setOrderPrice(1000);
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);
        order = new Order(1L, user, LocalDateTime.now(), OrderStatus.ORDER, orderItems);
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
    }

    @Test
    void orderSuccessTest() {
        // given
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setItemId(orderInfo.getItemId());
        orderInfo.setCount(2);
        String username = user.getUsername();

        when(itemRepository.findById(orderInfo.getItemId())).thenReturn(Optional.of(item));
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // when
        assertDoesNotThrow(() -> orderService.order(orderInfo, username));

        // then
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void cancelOrderSuccessTest() {
        // given
        Long orderId = order.getId();

        // when
        assertDoesNotThrow(() -> orderService.cancelOrder(orderId));

        // then
        verify(orderRepository).findById(orderId);
        assertEquals(OrderStatus.CANCEL, order.getOrderStatus());
    }

}