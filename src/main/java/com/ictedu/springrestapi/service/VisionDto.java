package com.ictedu.springrestapi.service;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VisionDto {
	private List<Request> requests;
	
	@Getter
	@Setter
	public static class Request {
		private Image image;
		private List<Feature> features;
	}
	@Getter
	@Setter
	public static class Image {
		private String content;
	}
	@Getter
	@Setter
	public static class Feature {
		@JsonIgnore
		private Long maxResults;//객체 탐지시 사용.OCR는 불필요
		private String type;
	}
}
/*
class Root {
	private List<Request> requests;
}
class Request {
private Image image;
private List<Feature> features;
}
class Image {
private String content;
}
class Feature {
private Long maxResults;
private String type;
}
*/