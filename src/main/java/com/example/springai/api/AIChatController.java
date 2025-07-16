package com.example.springai.api;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.springai.service.AIChatService;

import jakarta.servlet.http.HttpSession;


@RestController
public class AIChatController {
	private final AIChatService aiChatService; 
    public AIChatController(AIChatService aiChatService) {
		this.aiChatService = aiChatService;
	}

	@PostMapping("/chat")
    public String chat(@RequestBody Map<String, String> body, HttpSession session) { // session 속성안에 message List를 만들어 이전대화를 누적
		// {"userMsg":"hello"} JSON 문자열 -> 자바 DTO 객체(@RequestBody)
		String userMsg = body.get("userMsg");
    	return aiChatService.generate(userMsg, session);
    }
}
