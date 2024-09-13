package com.ictedu.springrestapi.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ictedu.springrestapi.service.FileService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name="파일 업로드 API", description = "REST API서버로 파일을 업로드하는 API입니다")
public class FileRestController {
	
	
	@Autowired
	private FileService fileService;
	
	//파일 저장위치 주입받기
	@Value("${spring.servlet.multipart.location}")
	private String saveDirectory;
	
	/*1.파일 업로드
	  -key=value 형태로 전송
	  -FORM태그(enctype="multipart/form-data")
	  -AJAX(new FormData()객체)
	   input type="file"요소(UI용),fetch API,FormData API
	  -POSTMAN/TALEND으로 테스트
	   POSTMAN으로 요청시에는 Body탭의 form-data 선택후
	   key와 value입력
	   파일인 경우 key입력시 옆에 file선택 
	  -요청 형식
	   POST http://localhost:9090/files	   
	*/
	
	@CrossOrigin
	@PostMapping(value = "/files",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<List<Map>> fileUpload(@RequestPart List<MultipartFile> files){
		try {
			List<Map> filesInfo=fileService.upload(files, saveDirectory);
			return ResponseEntity.ok(filesInfo);
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(null);
		}
	}
}
