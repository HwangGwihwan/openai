package com.example.springai.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.springai.dto.LoginHistoryDto;
import com.example.springai.mapper.LoginHistoryMapper;

@Service
public class LoginHistoryService {
	private final LoginHistoryMapper loginHistoryMapper;
	public LoginHistoryService(LoginHistoryMapper loginHistoryMapper) {
		this.loginHistoryMapper = loginHistoryMapper;
	}

	// 로그인 시간 기록
	public int recordLoginTime(String id) {
		return loginHistoryMapper.recordLoginTime(id);
	}
	
	// 로그아웃 시간 기록
	public int updateLogoutTime(String id) {
		return loginHistoryMapper.updateLogoutTime(id);
	}
	
	// 로그인 이력 조회
	public List<LoginHistoryDto> getLoginHistoryById(String id) {
		return loginHistoryMapper.getLoginHistoryById(id);
	}
}
