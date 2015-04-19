package org.bsth.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	//图片上传
	public HashMap<String, Object> uploadFiles(MultipartFile[] files, Boolean encryption);
	
	//文件加密存储
	public List<HashMap<String, Object>> encryptSaveFiles(MultipartFile[] files);
	
	//获取文件
	public void findByName(String name, HttpServletResponse response);
}
