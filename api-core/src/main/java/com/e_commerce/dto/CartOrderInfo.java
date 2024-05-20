package com.e_commerce.dto;

import java.util.List;

public record CartOrderInfo(Long cartItemId, List<CartOrderInfo> cartOrderInfoList) {
}
