package com.example.springai.api;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.springai.dto.LoginHistoryDto;
import com.example.springai.dto.UserDto;
import com.example.springai.service.LoginHistoryService;
import com.example.springai.service.LoginService;

import jakarta.servlet.http.HttpSession;

@RestController
public class LoginController {
	private final LoginService loginService;
	private final LoginHistoryService loginHistoryService;
	public LoginController(LoginService loginService, LoginHistoryService loginHistoryService) {
		this.loginService = loginService;
		this.loginHistoryService = loginHistoryService;
	}

	// 로그인
	@PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body, HttpSession session) {
        String id = body.get("username");
        String pw = body.get("password");

        // 서비스 호출
        UserDto user = loginService.login(id, pw);

        if (user != null) {
            // 비밀번호는 응답에서 제거 (보안상 중요!)
            user.setPw(null);
            
            // 로그인 성공 시 세션에 사용자 정보 저장 (key: "loginUser")
            session.setAttribute("loginUser", user);
            // 로그인 시간 기록도 이력 테이블에 저장 (Service 호출 필요)
            loginHistoryService.recordLoginTime(id);
            
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity
                    .status(401)
                    .body(Map.of("message", "아이디 또는 비밀번호가 틀렸습니다."));
        }
    }
	
	// 아이디중복 체크
	@GetMapping("/checkId/{id}")
	public boolean checkId(@PathVariable String id) {
		if (loginService.checkId(id) == 0) {
			return true;
		}
		return false;
	}
	
	// 회원가입
	@PostMapping("/signUp")
	 public ResponseEntity<?> signup(@RequestBody UserDto userDto) {
		try {
	        int isCreated = loginService.signUp(userDto);

	        if (isCreated > 0) {
	            return ResponseEntity.ok().build(); // 200 OK
	        } else {
	            return ResponseEntity.status(400)
	                    .body(Map.of("message", "회원가입에 실패했습니다."));
	        }
	    } catch (Exception e) {
	        // 중복 키 예외 처리
	        if (e.getMessage().contains("Duplicate entry")) {
	            return ResponseEntity.status(409)
	                    .body(Map.of("message", "이미 존재하는 아이디입니다."));
	        }

	        // 기타 예외 처리
	        return ResponseEntity.status(500)
	                .body(Map.of("message", "서버 오류가 발생했습니다."));
	    }
    }
	
	// 로그아웃
	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpSession session) {
	    // 세션에 저장된 사용자 정보 가져오기
	    UserDto user = (UserDto) session.getAttribute("loginUser");

	    if (user != null) {
	        // 로그아웃 시간 기록 (필요하면 user.getId() 같은 식으로 아이디 넘기기)
	        loginHistoryService.updateLogoutTime(user.getId());

	        // 세션 무효화 (로그아웃 처리)
	        session.invalidate();

	        return ResponseEntity.ok(Map.of("message", "성공적으로 로그아웃 되었습니다."));
	    } else {
	        return ResponseEntity.status(400).body(Map.of("message", "로그인 상태가 아닙니다."));
	    }
	}
	
	// 로그인 유저 정보 조회
	@GetMapping("/loginUser")
	public ResponseEntity<?> getLoginUser(HttpSession session) {
	    UserDto loginUser = (UserDto) session.getAttribute("loginUser");
	    if (loginUser != null) {
	        loginUser.setPw(null);
	        return ResponseEntity.ok(loginUser);
	    } else {
	        return ResponseEntity.status(401).body(Map.of("message", "로그인이 필요합니다."));
	    }
	}
	
	// 로그인 이력 조회
	@GetMapping("/loginHistory/{id}")
	public ResponseEntity<List<LoginHistoryDto>> getLoginHistory(@PathVariable String id) {
	    List<LoginHistoryDto> historyList = loginHistoryService.getLoginHistoryById(id);
	    return ResponseEntity.ok(historyList);
	}
	
	// 회원정보 수정
	@PostMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto, HttpSession session) {
        int result = loginService.updateUser(userDto);  // 이메일 업데이트
        
        if (result == 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("비밀번호가 일치하지 않거나 존재하지 않는 계정입니다.");
        }

        // 세션에서 로그인 유저 가져오기
        UserDto loginUser = (UserDto) session.getAttribute("loginUser");
        if (loginUser != null) {
            // 수정된 이메일로 세션 정보 갱신
            loginUser.setEmail(userDto.getEmail());
            session.setAttribute("loginUser", loginUser);
        }

        return ResponseEntity.ok().build();
    }
	
}
