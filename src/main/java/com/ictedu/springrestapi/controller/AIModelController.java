package com.ictedu.springrestapi.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.ictedu.springrestapi.service.VisionDto;

import io.swagger.v3.oas.annotations.tags.Tag;

/*
RestTemplate
	-Spring 3.0부터 지원하는 내장 클래스로 스프링 서버에서 REST한 방식으로 
	 HTTP 통신을 하기위한 API
	-Rest방식으로 다른 서버와 HTTP 통신을 동기 방식으로 쉽게 할수 있는  템플릿
	 (AsyncRestTemplate는 비동기 통신)
	-기본적으로 Connection pool을 사용하지 않아서
	 많은 요청을 하면 TIME_WAIT로 인해 자원이 점점 부족해져 
	  서비스에 어려움이 있다
	
	-내부적으로 java.net.HttpURLConnection 사용
	-요청을 보낼때는 HttpEntity< Map혹은 DTO,HttpHeaders>타입에 요청바디(데이타)와 요청헤더와 설정
	※클라이언트가 보내는 데이타가 Key=Value쌍(application/x-www-form-urlencoded)일때는 
	 반드시 MultiValueMap 사용
	 데이타가 JSON일때는 (application/json)일때는 MultiValueMap 혹은 Map 사용
	-응답을 받을때는 ResponseEntity<Map혹은 DTO>

	-JSON을 자바객체로 변환하기
		https://www.jsonschema2pojo.org
		※단,서버에서 받은 JSON의 키에 _가 포함되어 있는 경우 자동으로 카멜 케이스로 
		  바뀌니까 _로 다시 수정해주자
		구글 비전 REST API 사용(POSTMAN으로 REST요청을 자바코드로 구현하기.단,1 시간용)
		시간에 구애받지 않고 사용하려면 https://cloud.google.com/vision/docs/object-localizer?hl=ko
		의 소스 코드 사용하면 된다

객체 감지
	POST https://vision.googleapis.com/v1/images:annotate

	JSON 요청 본문:
	{
	 "requests": [
		{
		  "image": {
			"content": "BASE64_ENCODED_IMAGE"
		  },
		  "features": [
			{
			  "maxResults": RESULTS_INT,
			  "type": "OBJECT_LOCALIZATION"
			},
		  ]
		}
	 ]
	}
	요청 헤더	
	-H "Authorization: Bearer $(gcloud auth print-access-token)" \
	-H "x-goog-user-project: PROJECT_ID" \
	-H "Content-Type: application/json; charset=utf-8" \
	
	"LABEL_DETECTION": 이미지에 포함된 물체나 개념을 탐지하고 관련된 라벨을 제공.
	"TEXT_DETECTION": 이미지에서 텍스트를 감지하고 추출.
	"DOCUMENT_TEXT_DETECTION": 이미지에 포함된 문서 전체의 텍스트를 감지하고 추출.
	"FACE_DETECTION": 이미지에서 얼굴을 감지하고 얼굴에 대한 특성과 감정을 분석.
	"LOGO_DETECTION": 이미지에서 로고를 감지하고 관련 정보를 제공.
	"LANDMARK_DETECTION": 이미지에서 유명한 랜드마크를 감지하고 식별.
	"IMAGE_PROPERTIES": 이미지의 색상 정보를 분석하고 주요 색상 및 이미지 특성을 제공.
	"SAFE_SEARCH_DETECTION": 이미지에 대한 안전한 검색 결과를 제공하고 불순한 콘텐츠를 필터링.
*/
@RestController
@Tag(name = "구글비젼 API",description = "구글 OPEN REST API로 요청을 보내는 API입니다")
public class AIModelController {

	@Autowired
	private RestTemplate restTemplate;
	
	@PostMapping("/vision")
	@CrossOrigin
	public Map objectDetect(@RequestBody Map params) {
		//401:RestTemplete으로 HTTP 요청시 인증 오류처리는 아래 에러 핸들러 추가
		/*
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
			@Override
			public boolean hasError(ClientHttpResponse response) throws IOException {
				HttpStatus status= (HttpStatus) response.getStatusCode();
				return status.series() == HttpStatus.Series.SERVER_ERROR;
			}			
		});*/
		//1.RestTemplete으로 구글 REST API로 요청시 요청헤더 설정
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer ya29.a0AXooCgunmuOgsD4ngaBHa4KKxMMVP69D_ASDSRqIVOy0Z7g4ATpUvpO1NVdpoq518OokoEXhIHP7sG3DUgddhjBR6j28yelnCWILkVH_FyZzgjwE3bRYY_f9uys-EqJaZOejFSoklFujf9a51xU1v2ag8ABT52N8IkINsiV9zqSZaCgYKAbcSARESFQHGX2Mi3W-IeWMdGksNQwiKdqmuqQ0179");
		headers.add("x-goog-user-project", "my-vision-2024-06027");
		headers.add("Content-Type", "application/json; charset=utf-8");
		
		//2.구글의 요청본문(JSON형태)과 동일한 구조의 DTO로 요청바디 설정
		VisionDto body = new VisionDto();
		VisionDto.Image image = new VisionDto.Image();
		image.setContent(params.get("base64").toString());
		
		VisionDto.Feature feature = new VisionDto.Feature();
		feature.setMaxResults((long)30);
		feature.setType(params.get("model").toString());
		
		VisionDto.Request request = new VisionDto.Request();
		request.setFeatures(Arrays.asList(feature));
		request.setImage(image);
		
		body.setRequests(Arrays.asList(request));
		
		//3.요청 헤더/요청 바디(페이로드)정보등을 담은 HttpEntity객체 생성		
		//DTO혹은 Map에는 구글로 요청시 구글 REST API 서버에 보낼 데이타를 담는다.
		//※데이타가 Key=Value쌍(application/x-www-form-urlencoded)일때는 반드시 MultiValueMap 사용
		//  데이타가 JSON일때는 (application/json)일때는 MultiValueMap 혹은 Map 사용
		//HttpEntity<DTO혹은 Map> entity = new HttpEntity(DTO혹은 Map객체,headers);
		HttpEntity entity = new HttpEntity(body, headers);
		
		//4.RestTemplate으로 요청 보내기
		//String url="한글이 포한된 요청URI";
		//요청 URL에 한글 포함시는 UriComponents로 객체 생성후 사용시는 uri.toString()해서 사용한다
		//UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();
		String url="https://vision.googleapis.com/v1/images:annotate";
		//※외부 OPEN API(구글 REST API)서버로부터 받은 데이타 타입이
		//{}인 경우 Map 혹은 DTO
		//[{},{},....]인 경우 List<Map 혹은 DTO>
		ResponseEntity<Map> response= restTemplate.exchange(
				url, 
				HttpMethod.POST,//요청 메소드
				entity,//HttpEntity(요청바디와 요청헤더) 
				Map.class//응답 데이타가 {}일때(DTO계열.class:응답 데이타가 {}일때,List.class:응답 데이타가 [{},{},....]일때)	
				);
		System.out.println("응답코드:"+response.getStatusCode().value());
		System.out.println("응답헤더:"+response.getHeaders());
		System.out.println("응답바디:"+response.getBody());
		
		return response.getBody();
	}
}