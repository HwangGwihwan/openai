package com.example.springai.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.springai.dto.UserDto;

@Mapper
public interface LoginMapper {
	// 로그인
	public UserDto login(String id, String pw);
	
	// 아이디중복 체크
	public int checkId(String id);
	
	// 회원가입
	public int signUp(UserDto userDto);
}
