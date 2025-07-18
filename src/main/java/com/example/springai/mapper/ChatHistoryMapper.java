package com.example.springai.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.springai.dto.ChatHistoryDto;

@Mapper
public interface ChatHistoryMapper {
	// 대화 내용 저장
	int save(ChatHistoryDto chatHistoryDto);
	
	// 대화 내용 전체
	List<ChatHistoryDto> selectChatAll(String id);
	
	// 즐겨찾기 추가/해제
	int updateFavorite(int no, int favorite);
	
	// 선택한 대화내용 삭제
	int deleteByIds(List<Integer> selectedIds);
}
