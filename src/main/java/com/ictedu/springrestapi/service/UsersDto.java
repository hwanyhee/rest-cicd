package com.ictedu.springrestapi.service;


import java.time.LocalDateTime;

import com.ictedu.springrestapi.respository.Users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UsersDto {
	//엔터티의 필드중 데이타 전달에 필요한 필드만 지정(ROLE 필드은 제외)
	private String username;
	private String password;
	private String name;
	private LocalDateTime regiDate;
	//DTO를 ENTITY로 변환하는 메소드
	public Users toEntity() {
		return Users.builder()
				.name(name)
				.username(username)
				.password(password)
				.regiDate(regiDate)
				.build();
	}
	//ENTITY를 DTO로 변환하는 메소드
	public static UsersDto toDto(Users users) {
		return UsersDto.builder()
				.name(users.getName())
				.username(users.getUsername())
				.password(users.getPassword())
				.regiDate(users.getRegiDate())
				.build();
	}
	
}
