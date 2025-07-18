package com.example.springai.api;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.springai.dto.ChatHistoryDto;
import com.example.springai.dto.HashTagDto;
import com.example.springai.service.HashTagService;

@RestController
public class HashTagController {
	private final HashTagService hashTagService;
	public HashTagController(HashTagService hashTagService) {
		this.hashTagService = hashTagService;
	}

	// 해시태그 삽입
	@PostMapping("/insertHashTag/{no}")
	public ResponseEntity<?> addTags(@PathVariable("no") int chatNo, @RequestBody List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return ResponseEntity.badRequest().body("해시태그가 비어 있습니다.");
        }

        try {
        	hashTagService.saveTags(chatNo, tags);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("해시태그 저장 실패");
        }
    }
	
	// 해시태그 검색
	@GetMapping("selectTag/{no}")
	public ResponseEntity<String> getTags(@PathVariable int no) {
	    List<String> tags = hashTagService.findTagsByNo(no);
	    return ResponseEntity.ok(String.join(",", tags));
	}
	
	// 인기 해시태그 검색
	@GetMapping("/popular")
	public ResponseEntity<List<HashTagDto>> getPopularTags() {
        List<HashTagDto> popularTags = hashTagService.getPopularTags();
        return ResponseEntity.ok(popularTags);
    }
	
	// 해시태그에 따른 대화 검색
	@GetMapping("/chatByTag/{tag}")
	 public ResponseEntity<List<ChatHistoryDto>> getChatsByTag(@PathVariable String tag) {
        try {
            List<ChatHistoryDto> chats = hashTagService.findChatsByTag(tag);
            return ResponseEntity.ok(chats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }
	
	// 전체 해시태그 검색
	@GetMapping("/tags")
    public List<String> getAllTags() {
        return hashTagService.getAllTags();
    }
	
}
