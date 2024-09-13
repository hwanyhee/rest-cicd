package com.ictedu.springrestapi.controller.docs;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.ictedu.springrestapi.service.UsersDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "사용자 API",description = "사용자 데이타에 대한 REST API입니다")
public interface UsersRestContollerDocs {

	@Operation(summary = "회원 가입",description = "JSON형식의 데이타를 받아 회원가입 처리합니다")
	@Parameter(name = "username",description = "아이디",required = true)
	@Parameter(name = "password",description = "비밀번호",required = true)
	@Parameter(name = "name",description = "이름",required = true)
	public ResponseEntity<UsersDto> signUp(@RequestParam Map map);
	
	@Operation(summary = "모든 회원 조회",description = "모든 회원의 정보를 조회 합니다")
	public ResponseEntity<List<UsersDto>> getUsersAll();
	
	@Operation(summary = "회원 조회",description = "아이디로 회원의 정보를 조회 합니다")
	public ResponseEntity<UsersDto> getUsersByUsername(@PathVariable String username);
	
	@Operation(summary = "회원 수정",description = "회원의 정보를 수정 합니다")
	public ResponseEntity<UsersDto> usersUpdate(@PathVariable String username,@RequestBody UsersDto dto);
	
	@Operation(summary = "회원 삭제",description = "아이디로 회원의 정보를 삭제 합니다")
	public ResponseEntity<UsersDto> removeUsersByUsername(@PathVariable String username);
}
