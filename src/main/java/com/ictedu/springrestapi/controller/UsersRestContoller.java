package com.ictedu.springrestapi.controller;

import java.util.List;
import java.util.Map;

import org.apache.catalina.mapper.Mapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ictedu.springrestapi.controller.docs.UsersRestContollerDocs;
import com.ictedu.springrestapi.service.UsersDto;
import com.ictedu.springrestapi.service.UsersService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UsersRestContoller implements UsersRestContollerDocs {
	
	private final UsersService usersService;
	private final ObjectMapper objectMapper;
	
	/*
	 ObjectMapper의 주요 메소드
		1)맵을 DTO로 변환
		convertValue(맵객체,DTO타입.class);
		2)JSON형식의 문자열을 맵이나 DTO로
		readValue(JSON형식의 문자열,DTO타입.class);
		readValue(JSON형식의 문자열,Map.class);
		3)JSON 배열형식의 문자열을 List<맵>이나 List<DTO>로
		readValue(JSON 배열형식의 문자열,ObjectMapper객체.getTypeFactory().defaultInstanct().constructCollectionType(List.class,DTO타입.class));
		readValue(JSON 배열형식의 문자열,ObjectMapper객체.getTypeFactory().defaultInstanct().constructCollectionType(List.class,Map.class));
	 	4)List<맵>을 List<DTO>로
	 	convertValue(List<맵>객체,ObjectMapper객체.getTypeFactory().defaultInstanct().constructCollectionType(List.class, DTO타입.class));
	 */
	/*
	※사용자 화면 없이 도구를 사용해서 REST API테스트
	-POSTMAN 혹은 TALEND API TESTER  
	
	1.데이타를 key=value쌍으로 받아서 CREATE
		-프론트 엔드에서는 FORM태그를 이용해서 요청(SUBMIT버튼)하거나 ajax(자바스크립트)로 요청
		-ajax(자바스크립트)로 요청시에는 
		 {키:"값",키2:"값2"} JSON 객체 혹은 
		 "키1=값1&키2=값2&.." 문자열 데이타  혹은 
		 new FormData(폼객체)로 입력할 데이타를 서버로 요청 바디에 실어서 보낸다
				
		-요청 형식
		 POST http://localhost:9090/users	
	*/
	@CrossOrigin
	@PostMapping("/users")	
	//1)DTO로 받기
	//public ResponseEntity<UsersDto> signUp(UsersDto dto){//@RequestParam붙이면 400 에러
	//2)Map으로 받기
	public ResponseEntity<UsersDto> signUp(@RequestParam Map map){
		try {
			//맵을 DTO로 변환코드 추가
			UsersDto dto=objectMapper.convertValue(map, UsersDto.class);
			
			
			UsersDto insertedDto=usersService.signUp(dto);
			return ResponseEntity.ok(insertedDto);
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	/*
	1-2.데이타를 JSON으로 받아서 CREATE
		-프론트 엔드에서는 ajax(자바스크립트)로 요청
		-ajax(자바스크립트)로 요청시에는 
		 JSON.stringify({키:"값",키2:"값2"}) 혹은 
		 "{\"키1\":\"값1\",\"키2\":\"값2\"..}" 문자열 데이타  혹은 
		 new FormData(폼객체)를 JSON.stringify({키:"값",키2:"값2"})로 변환해서 데이타를 서버로 요청 바디에 실어서 보낸다
				
		-요청 형식
		 POST http://localhost:9090/users	
	*/
	/*
	@PostMapping("/users")
	//1)DTO로 받기
	//public ResponseEntity<UsersDto> signUp(@RequestBody UsersDto dto){
	//2)Map으로 받기
	public ResponseEntity<UsersDto> signUp(@RequestBody Map map){
		try {
			//맵을 DTO로 변환코드 추가
			UsersDto dto=objectMapper.convertValue(map, UsersDto.class);
			UsersDto insertedDto=usersService.signUp(dto);
			return ResponseEntity.ok(insertedDto);
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}	
	}////////////
	*/
	//2.회원의 데이타 조회-READ
	/*
	  -FORM태그 혹은 A태그 혹은 ajax요청	 
	  -POSTMAN/TALEND API TESTER/브라우저로 테스트
	 */
	//2-1. 모든 회원 조회
	// -요청 형식
	//  GET http://localhost:9090/users
	//  데이타는 필요없다
	@CrossOrigin
	@GetMapping("/users")	
	public ResponseEntity<List<UsersDto>> getUsersAll(){
		try {
			List<UsersDto> usersList=usersService.usersAll();
			return ResponseEntity.status(200).header(HttpHeaders.CONTENT_TYPE, "application/json").body(usersList);
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}///////////////////
	//2-2. URI 파라미터(패스 파라미터)로 회원 조회
	// -요청 형식
	//  GET http://localhost:9090/users/{아이디}
	//  데이타는 필요없다
	@CrossOrigin
	@GetMapping("/users/{username}")	
	public ResponseEntity<UsersDto> getUsersByUsername(@PathVariable String username){
		try {
			UsersDto dto=usersService.usersOneByUsername(username);
			return ResponseEntity.ok(dto);
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}///////////////////
	/*3.데이타를 JSON으로 받아서 수정-UPDATE
		-AJAX로 요청 즉 PUT메소드 사용시 반드시 JSON으로 받아야 한다
		-POSTMAN/TALEND API TESTER으로 테스트
		※PUT이나 DELETE도 데이타는 요청 바디에 싣는다	
		-요청 형식
		PUT http://localhost:9090/users/{username}
		데이타는 아래와 같다		
		{
			"password":"5678",
			"name":"이길동"
		}	
		
	*/
	@CrossOrigin
	@PutMapping("/users/{username}")	
	public ResponseEntity<UsersDto> usersUpdate(@PathVariable String username,@RequestBody UsersDto dto){
		try {
			System.out.println("회원 수정 요청입니다");
			UsersDto updatedDto=usersService.update(username,dto);
			return ResponseEntity.ok(updatedDto);
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}///////////////////
	/*4.키를 URL파라미터로 받아서 삭제-DELETE
		-AJAX로 요청
		-POSTMAN/TALEND으로 테스트
		 
		-요청 형식
		DELETE http://localhost:9090/users/:username
		데이타는 필요없다	
	*/
	@CrossOrigin
	@DeleteMapping("/users/{username}")	
	public ResponseEntity<UsersDto> removeUsersByUsername(@PathVariable String username){
		try {
			UsersDto deletedDto=usersService.removeByUsername(username);
			return ResponseEntity.ok(deletedDto);
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}///////////////////
}
