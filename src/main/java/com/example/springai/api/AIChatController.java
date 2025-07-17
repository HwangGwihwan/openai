package com.example.springai.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.messages.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.springai.dto.ChatHistoryDto;
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
	
	// 대화내용 저장
	@PostMapping("/chat/save")
    public ResponseEntity<String> saveChat(HttpSession session) {
        aiChatService.saveChatHistory(session);
        return ResponseEntity.ok("대화내용이 저장되었습니다.");
    }
	
	// 대화내용 초기화
	@PostMapping("/chat/clear")
	public String clearChatHistory(HttpSession session) {
	    session.setAttribute("chatHistory", new ArrayList<Message>());
	    return "success";
	}
	
	// 대화 내용 전체
	@GetMapping("/chatHistory/{id}")
	public ResponseEntity<List<ChatHistoryDto>> getChatHistoryByUserId(@PathVariable String id) {
        List<ChatHistoryDto> chatList = aiChatService.selectChatAll(id);

        if (chatList == null || chatList.isEmpty()) {
            return ResponseEntity.noContent().build();  // 204 No Content
        }

        return ResponseEntity.ok(chatList);  // 200 OK + 데이터
	}
	
	// 즐겨찾기 추가 해제
	@PostMapping("/chatHistory/favorite/{id}")
	public ResponseEntity<String> updateFavorite(@PathVariable String id,
			@RequestBody Map<String, Integer> body) {
		// {"userMsg":"hello"} JSON 문자열 -> 자바 DTO 객체(@RequestBody)
		int favorite = body.get("favorite");
    	
		int updated = aiChatService.updateFavorite(id, favorite);

	    if (updated > 0) {
	        return ResponseEntity.ok("즐겨찾기 상태가 업데이트되었습니다.");
	    } else {
	        return ResponseEntity.status(404).body("해당 채팅 기록을 찾을 수 없습니다.");
	    }
		
    }
	
}
