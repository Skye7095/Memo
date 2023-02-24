package com.moonlight.memo.post;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.moonlight.memo.post.bo.PostBO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/post")
public class PostRestController {

	@Autowired
	private PostBO postBO;
	
	// 메모 입력 API
	@PostMapping("/create")
	public Map<String, String>postCreate(
			@RequestParam("title") String title
			, @RequestParam("content") String content
			, @RequestParam("file") MultipartFile file
			, HttpServletRequest request) {
		
		// 로그인된 사용자의 user 테이블 id 컬럼 값
		HttpSession session = request.getSession();
		
		int userId= (Integer)session.getAttribute("userId");
		
		int count = postBO.addPost(userId, title, content, file);
		
		Map<String, String> result = new HashMap<>();
		
		if(count == 1) {
			result.put("result", "success");
		}else {
			result.put("result", "fail");
		}
		
		return result;
	}
	
	// post 수정기능
	@PostMapping("/update")
	public Map<String, String> modifyMemo(
			@RequestParam("postId") int postId
			, @RequestParam("title") String title
			, @RequestParam("content") String content) {
		
		int count = postBO.updatePost(postId, title, content);
		
		Map<String, String> result = new HashMap<>();
		
		if(count == 1) {
			result.put("result", "success");
		}else {
			result.put("result", "fail");
		}
		
		return result;
	}
	
	// post 삭제기능
	@GetMapping("/delete")
	public Map<String, String> deleteMemo(@RequestParam("postId") int postId){
		int count = postBO.deletePost(postId);
		
		Map<String, String> result = new HashMap<>();
		
		if(count == 1) {
			result.put("result", "success");
		}else {
			result.put("result", "fail");
		}
		
		return result;
	}

}
