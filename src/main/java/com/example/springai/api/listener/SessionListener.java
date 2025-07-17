package com.example.springai.api.listener;

import org.springframework.stereotype.Component;

import com.example.springai.dto.UserDto;
import com.example.springai.service.LoginHistoryService;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@Component
public class SessionListener implements HttpSessionListener{
	private final LoginHistoryService loginHistoryService;
	public SessionListener(LoginHistoryService loginHistoryService) {
		this.loginHistoryService = loginHistoryService;
	}
	
	@Override
    public void sessionDestroyed(HttpSessionEvent se) {
        // 세션에서 로그인 유저 정보 가져오기
        UserDto user = (UserDto) se.getSession().getAttribute("loginUser");
        
        if (user != null) {
            String id = user.getId(); // UserDto에서 ID 가져오기
            
            // 로그아웃 시간 DB에 기록
            loginHistoryService.updateLogoutTime(id);
        }
    }
}
