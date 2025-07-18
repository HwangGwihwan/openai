package com.example.springai.dto;

import lombok.Data;

@Data
public class ChatHistoryDto {
	private int no;
	private String userId;
	private String userChat;
	private String aiChat;
	private int favorite;
	private String createAt;
	private String tags;
}
