package com.e_commerce.service;

import com.e_commerce.constant.ItemSellStatus;
import com.e_commerce.constant.ProductStatus;
import com.e_commerce.dto.CartItemInfo;
import com.e_commerce.entity.Cart;
import com.e_commerce.entity.CartItem;
import com.e_commerce.entity.Item;
import com.e_commerce.entity.User;
import com.e_commerce.repository.CartItemRepository;
import com.e_commerce.repository.CartRepository;
import com.e_commerce.repository.ItemRepository;
import com.e_commerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartService cartService;

    private Item item;
    private User user;
    private Cart cart;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        item = new Item(1L, "Test Item", 100, 10, "test", ProductStatus.NEW, ItemSellStatus.SELL);
        user = new User(1L, "testUser", "password", "testUser@example.com", null, null, null);
        cart = new Cart(1L, user);
        cartItem = new CartItem(1L, cart, item, 2);
    }

    @Test
    void addCartSuccessTest() {
        // given
        CartItemInfo cartItemInfo = new CartItemInfo();
        cartItemInfo.setItemId(item.getId());
        cartItemInfo.setCount(2);
        String username = user.getUsername();

        when(itemRepository.findById(cartItemInfo.getItemId())).thenReturn(Optional.of(item));
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(user.getId())).thenReturn(cart);
        when(cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId())).thenReturn(cartItem);

        // when
        assertDoesNotThrow(() -> cartService.addCart(cartItemInfo, username));

        // then
        verify(itemRepository, times(1)).findById(cartItemInfo.getItemId());
        verify(userRepository, times(1)).findByUsername(username);
        verify(cartRepository, times(1)).findByUserId(user.getId());
        verify(cartItemRepository, times(1)).findByCartIdAndItemId(cart.getId(), item.getId());
        verify(cartItemRepository, never()).save(any(CartItem.class));
        assertEquals(4, cartItem.getCount());
    }


}