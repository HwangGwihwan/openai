package com.example.springai.service;

import org.springframework.stereotype.Service;

import com.example.springai.dto.UserDto;
import com.example.springai.mapper.LoginMapper;

@Service
public class LoginService {
	private final LoginMapper loginMapper;
	public LoginService(LoginMapper loginMapper) {
		this.loginMapper = loginMapper;
	}

	// 로그인
	public UserDto login(String id, String pw) {
		return loginMapper.login(id, pw);
	}
	
	// 아이디중복 체크
	public int checkId(String id) {
		return loginMapper.checkId(id);
	}
	
	// 회원가입
	public int signUp(UserDto userDto) {
		return loginMapper.signUp(userDto);
	}
}
