package com.example.springai.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.springai.dto.ChatHistoryDto;

@Mapper
public interface ChatHistoryMapper {
	// 대화 내용 저장
	int save(ChatHistoryDto chatHistoryDto);
}
