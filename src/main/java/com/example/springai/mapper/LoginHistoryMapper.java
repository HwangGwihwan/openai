package com.example.springai.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.springai.dto.LoginHistoryDto;

@Mapper
public interface LoginHistoryMapper {
	// 로그인 시간 기록
	public int recordLoginTime(String id);
	
	// 로그아웃 시간 기록
	public int updateLogoutTime(String id);
	
	// 로그인 이력 조회
	public List<LoginHistoryDto> getLoginHistoryById(String id);
}
