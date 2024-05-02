package com.e_commerce.dto;

import com.e_commerce.constant.Role;

public record UserInfo(String username, String password, String email, Role role) {
}
