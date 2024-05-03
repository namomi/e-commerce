package com.e_commerce.entity;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.e_commerce.constant.Role;
import com.e_commerce.dto.UserInfo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String username;

	@NotNull
	private String password;

	@Column(unique = true)
	private String email;

	@Enumerated(EnumType.STRING)
	private Role role;

	public static User toEntity(UserInfo userInfo, PasswordEncoder passwordEncoder) {
		return User.builder()
			.username(userInfo.username())
			.password(passwordEncoder.encode(userInfo.password()))
			.email(userInfo.email())
			.role(userInfo.role())
			.build();
	}
}
