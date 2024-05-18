package com.e_commerce.service;

import com.e_commerce.dto.CartDetailInfo;
import com.e_commerce.dto.CartItemInfo;
import com.e_commerce.dto.CartOrderInfo;
import com.e_commerce.dto.OrderInfo;
import com.e_commerce.entity.Cart;
import com.e_commerce.entity.CartItem;
import com.e_commerce.entity.Item;
import com.e_commerce.entity.User;
import com.e_commerce.exception.CustomException;
import com.e_commerce.repository.CartItemRepository;
import com.e_commerce.repository.CartRepository;
import com.e_commerce.repository.ItemRepository;
import com.e_commerce.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.e_commerce.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    private final RestHighLevelClient client;

    private final ObjectMapper objectMapper;

    private final OrderService orderService;


    @Transactional
    public void addCart(CartItemInfo cartItemInfo, String username) {
        Item item = itemRepository.findById(cartItemInfo.getItemId())
                .orElseThrow(() -> new CustomException(NO_MATCHING_ITEM));
        User user = getUser(username);
        Cart cart = cartRepository.findByUserId(user.getId());

        if (cart == null) {
            cart = Cart.createCart(user);
            cartRepository.save(cart);
        }

        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        if (savedCartItem != null) {
            savedCartItem.addCount(cartItemInfo.getCount());
        } else {
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemInfo.getCount());
            cartItemRepository.save(cartItem);
        }
    }

    @Transactional(readOnly = true)
    public List<CartDetailInfo> findCartList(Long cartId) throws IOException {
        SearchRequest searchRequest = new SearchRequest("cartitem");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchQuery("cart.id", cartId));
        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        return List.of(searchResponse.getHits().getHits()).stream()
                .map(hit -> {
                    CartItem cartItem = mapToCartItem(hit);
                    String itemNm = cartItem.getItem().getItemNm();
                    ;
                    int price = cartItem.getItem().getPrice();
                    String imgUrl = cartItem.getItem().getItemDetail();
                    return new CartDetailInfo(cartItem.getId(), itemNm, price, cartItem.getCount(), imgUrl);
                }).collect(Collectors.toList());
    }

    @Transactional
    public void orderCartItem(List<CartOrderInfo> cartOrderInfoList, String username) {
        List<OrderInfo> orderInfoList = new ArrayList<>();
        for (CartOrderInfo cartOrderInfo : cartOrderInfoList) {
            CartItem cartItem = cartItemRepository.findById(cartOrderInfo.cartItemId())
                    .orElseThrow(() -> new CustomException(NO_MATCHING_CARTORDER));

            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setItemId(cartItem.getId());
            orderInfo.setCount(cartItem.getCount());
            orderInfoList.add(orderInfo);
        }

        orderService.orders(orderInfoList, username);

        for (CartOrderInfo cartOrderInfo : cartOrderInfoList) {
            CartItem cartItem = cartItemRepository.
                    findById(cartOrderInfo.cartItemId())
                    .orElseThrow(() -> new CustomException(NO_MATCHING_CARTORDER));
            cartItemRepository.delete(cartItem);
        }
    }

    private User getUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(NO_MATCHING_USER));
        return user;
    }

    private CartItem mapToCartItem(SearchHit hit) {
        try {
            return objectMapper.readValue(hit.getSourceAsString(), CartItem.class);
        } catch (IOException e) {
            throw new CustomException(SEARCH_HEAT_MAPPING_FAILED);
        }
    }

    public Boolean validateCartItem(Long cartItemId, String username) {
        User user = getUser(username);
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CustomException(NO_MATCHING_CARTITEM));
        User savedUser = cartItem.getCart().getUser();

        if (!user.getUsername().equals(savedUser.getUsername())) {
            return false;
        }
        return true;
    }
}
