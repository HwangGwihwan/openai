package com.example.springai.dto;

import lombok.Data;

@Data
public class LoginHistoryDto {
	private int historyId;
	private String userId;
	private String loginTime;
	private String logoutTime;
	private String duration;
}
