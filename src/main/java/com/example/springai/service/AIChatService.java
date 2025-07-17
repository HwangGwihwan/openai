package com.example.springai.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.springai.dto.ChatHistoryDto;
import com.example.springai.dto.UserDto;
import com.example.springai.mapper.ChatHistoryMapper;

import jakarta.servlet.http.HttpSession;

@Service
public class AIChatService {
	private OpenAiChatModel openAiChatModel;
	private final ChatHistoryMapper chatHistoryMapper;
	public AIChatService(OpenAiChatModel openAiChatModel, ChatHistoryMapper chatHistoryMapper) {
		this.openAiChatModel = openAiChatModel;
		this.chatHistoryMapper = chatHistoryMapper;
	}

	// OpenAI 서버와 통신할 메서드 선언
	// param String userMsg : 사용자 문자열 메세지
	// return String : 오픈챗서버의 응답문자열
	public String generate(String userMsg, HttpSession session) {
		/*
		 * DB 저장될 내용
		 * userMsg
		 * sessiong.getId()
		 * 
		 * ex)
		 * 1(int), 12312...(text), 안녕(text), 그래 ...(text)
		 * 
		 */
		
		List<Message> messageList = (List<Message>)session.getAttribute("chatHistory");
		if (messageList == null) {
			messageList = new ArrayList<Message>();
		}
		
		// SystemMessage, UserMessage
		SystemMessage systemMessage = new SystemMessage("너는 한국어 반말만 답변하는 스타일의 AI 챗봇");
		UserMessage userMessage = new UserMessage(userMsg);
		
		messageList.add(systemMessage);
		messageList.add(userMessage);
		
		OpenAiChatOptions options = OpenAiChatOptions.builder()
	            .model("gpt-3.5-turbo") // 사용하고자 하는 OpenAI 모델(버전)의 이름을 지정
	            .temperature(0.7) // 창의성(무작위성) 정도를 설정(0.0 ~ 2.0)값으로 보통 0~1 사이 사용
	            .build();
		
		// OpenAI 서버에 전달되는 모델 객체(매개값)
		Prompt prompt = new Prompt(messageList, options);
		
		// prompt를 OpenAI 서버에 전달
		ChatResponse res = this.openAiChatModel.call(prompt);
		String aiReply = res.getResult().getOutput().getText();
		
		// AI응답 내용들도 messageList에 누적
		AssistantMessage assistantMessage = new AssistantMessage(aiReply);
		messageList.add(assistantMessage);
		
		// messageList 변경된 내용 session의 messageList속성에도 반영
		session.setAttribute("chatHistory", messageList); // 이전 session chatHistory 속성값을 덮어쓰기
		
		/*
		ChatHistoryDto chatHistoryDto = new ChatHistoryDto();
		chatHistoryDto.setUserId(session.getId());
		chatHistoryDto.setUserChat(userMsg);
		chatHistoryDto.setAiChat(aiReply);
		
		chatHistoryMapper.save(chatHistoryDto);
		*/
		
		return aiReply;
	}
	
	public void saveChatHistory(HttpSession session) {
	    List<Message> messageList = (List<Message>) session.getAttribute("chatHistory");
	    
	    UserDto loginUser = (UserDto) session.getAttribute("loginUser");
	    if (loginUser == null) {
	        // 로그인 안 된 상태거나 세션에 데이터 없음
	        return;
	    }
	    String userId = loginUser.getId();  // getId()는 UserDto의 아이디를 반환하는 메서드
	    
	    if (userId == null || messageList == null || messageList.isEmpty()) return;

	    // user 메시지와 assistant 메시지를 짝으로 저장
	    for (int i = 0; i < messageList.size() - 1; i++) {
	        if (messageList.get(i) instanceof UserMessage && messageList.get(i + 1) instanceof AssistantMessage) {
	            String userMsg = ((UserMessage) messageList.get(i)).getText();
	            String aiMsg = ((AssistantMessage) messageList.get(i + 1)).getText();

	            ChatHistoryDto dto = new ChatHistoryDto();
	            dto.setUserId(userId);
	            dto.setUserChat(userMsg);
	            dto.setAiChat(aiMsg);

	            chatHistoryMapper.save(dto);
	        }
	    }
	    
	    // 저장 완료 후 세션 초기화
	    session.setAttribute("chatHistory", new ArrayList<Message>());
	}
	
	// 대화 내용 전체
	public List<ChatHistoryDto> selectChatAll(String id) {
		return chatHistoryMapper.selectChatAll(id);
	}

	// 즐겨찾기 추가/해제
	public int updateFavorite(String id, int favorite) {
		return chatHistoryMapper.updateFavorite(id, favorite);
	}

	
}
