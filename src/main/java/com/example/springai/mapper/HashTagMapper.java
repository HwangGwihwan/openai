package com.example.springai.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.springai.dto.ChatHistoryDto;
import com.example.springai.dto.HashTagDto;

@Mapper
public interface HashTagMapper {
	// 해시태그 저장
	public int saveTags(int no, List<String> tags);
	
	// 해시태그 검색
	public List<String> findTagsByNo(int no);
	
	// 인기 해시태그 검색
	public List<HashTagDto> getPopularTags();
	
	// 해시태그에 따른 대화 검색
	public List<ChatHistoryDto> findChatsByTag(String tag);
	
	// 전체 해시태그 검색
	public List<String> getAllTags();
}
