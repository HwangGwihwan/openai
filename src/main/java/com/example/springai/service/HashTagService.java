package com.example.springai.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.springai.dto.ChatHistoryDto;
import com.example.springai.dto.HashTagDto;
import com.example.springai.mapper.HashTagMapper;

@Service
public class HashTagService {
	private final HashTagMapper hashTagMapper;	
	public HashTagService(HashTagMapper hashTagMapper) {
		this.hashTagMapper = hashTagMapper;
	}

	// 해시태그 저장
	public int saveTags(int no, List<String> tags) {
		return hashTagMapper.saveTags(no, tags);
	}
	
	// 해시태그 검색
	public List<String> findTagsByNo(int no) {
		return hashTagMapper.findTagsByNo(no);
	}
	
	// 인기 해시태그 검색
	public List<HashTagDto> getPopularTags() {
		return hashTagMapper.getPopularTags();
	}
	
	// 해시태그에 따른 대화 검색
	public List<ChatHistoryDto> findChatsByTag(String tag) {
		return hashTagMapper.findChatsByTag(tag);
	}
	
	// 전체 해시태그 검색
	public List<String> getAllTags() {
		return hashTagMapper.getAllTags();
	}
	
}
