package com.example.springai.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HashTagMapper {
	// 해시태그 저장
	public int saveTags(int no, List<String> tags);
	
	// 해시태그 검색
	public List<String> findTagsByNo(int no);
}
