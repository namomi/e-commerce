package com.e_commerce.entity;


import com.e_commerce.constant.Role;
import com.e_commerce.dto.UserInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Table(name = "User")
public class Users extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(unique = true)
	private String username;

	@NotNull
	private String password;

	@Column(unique = true)
	private String email;

	@Enumerated(EnumType.STRING)
	private Role role;

	public static Users toEntity(UserInfo userInfo, PasswordEncoder passwordEncoder) {
		return Users.builder()
			.username(userInfo.username())
			.password(passwordEncoder.encode(userInfo.password()))
			.email(userInfo.email())
			.role(userInfo.role())
			.build();
	}
}
