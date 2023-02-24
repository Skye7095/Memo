package com.moonlight.memo.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.multipart.MultipartFile;

public class FileManagerService {
	
	// 파일 저장
	public static final String FILE_UPLOAD_PATH = "D:\\java web develop\\springProject\\upload\\memo";
	
	// 파일을 저장하고, 클라언트에서 접근가능한 주소를 만들어서 리턴하는 기능
	public static String saveFile(int userId, MultipartFile file) {
		
		
		// 사용자별로 폴더를 구분한다
		// 사용자별로 폴더를 새로 만든다
		// 폴더이름: userId_현재시간
		// 폴더이름 중 현재시간을 UNIX TIME으로 한다. (UNIX TIME: 1970년 1월 1일부터 흐른 시간(millisecond 1/1000))
		// D:\\java web develop\\springProject\\upload\\memo\\2_142485314\\abcd.jpg
		
		String directoryName = "/" + userId + "_" + System.currentTimeMillis() + "/";
		
		// 디렉토리 생성
		String directoryPath = FILE_UPLOAD_PATH + directoryName;
		File directory = new File(directoryPath);
		if(directory.mkdir() == false) {
			// 디렉토리 생성 실패
			return null;
		}
		
		// 파일 저장
		try {
			byte[] bytes = file.getBytes();
			
			String filePath = directoryPath + file.getOriginalFilename();
			Path path = Paths.get(filePath);
			Files.write(path, bytes);
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
			return null;
		}
		
		// 클라이언트에 접근 가능한 경로를 문자열로 리턴
		// http://localhost:8080/memo/~
		return "/memo" + directoryName + file.getOriginalFilename();
	}
	
	// 파일 삭제 메소드
	public static boolean removeFile(String filePath) {
		// 삭제 경로 /memo를 제거하고
		// 실제 파일 저장 경로에 이어 붙여 준다.
		String realFilePath = FILE_UPLOAD_PATH + filePath.replace("/memo", "");
		Path path = Paths.get(realFilePath);
		
		// 파일이 존재하는지 먼저 확인하고 삭제해야함
		if(Files.exists(path)) {
			try {
				Files.delete(path);
			} catch (IOException e) {
				
				e.printStackTrace();
				
				return false;
			}
		}
		
		// 파일을 저장하는 폴더도 같이 삭제해야함
		// 디렉토리 경로
		Path dirPath = path.getParent();
		// 디렉토리 존재여부
		if(Files.exists(dirPath)) {
			try {
				Files.delete(path);
			} catch (IOException e) {
				
				e.printStackTrace();
				
				return false;
			}
		}
		
		return true;
	}
}
