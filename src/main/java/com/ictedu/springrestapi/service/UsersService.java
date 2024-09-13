package com.ictedu.springrestapi.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ictedu.springrestapi.respository.Users;
import com.ictedu.springrestapi.respository.UsersRepository;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsersService {

	private final UsersRepository usersRepository;
	private final ObjectMapper objectMapper;
	@Transactional
	public UsersDto signUp(UsersDto dto) {
		System.out.println("서비스의 signUp()함수 호출:"+dto);
		return UsersDto.toDto(usersRepository.save(dto.toEntity()));		
	}//////////////
	@Transactional(readOnly = true)
	public List<UsersDto> usersAll() {	
		List<Users> usersEntityList= usersRepository.findAll();
		//1)스트림 사용
		//return usersEntityList.stream().map(user->UsersDto.toDto(user)).collect(Collectors.toList());
		//2)ObjectMapper 사용
		return objectMapper.convertValue(usersEntityList, objectMapper.getTypeFactory().defaultInstance().constructCollectionType(List.class,UsersDto.class));
	}////////////////
	@Transactional(readOnly = true)
	public UsersDto usersOneByUsername(String username) {		
		return UsersDto.toDto(usersRepository.findByUsername(username).get());
	}////////////////////////
	@Transactional
	public UsersDto update(String username, UsersDto dto) {
		Users users=usersRepository.findByUsername(username).get();
		users.setName(dto.getName());
		users.setPassword(dto.getPassword());
		return UsersDto.toDto(usersRepository.save(users));
		
	}
	
	@Transactional
	public UsersDto removeByUsername(String username) {
		UsersDto deletedDto=UsersDto.toDto(usersRepository.findByUsername(username).get());
		usersRepository.deleteByUsername(username);		
		return deletedDto;
	}//////////////
	
	
}
