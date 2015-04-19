package org.bsth.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import org.bsth.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/fileSystem")
public class FileController {
	
	@Autowired
	private FileService imageService;
	
	/**
	 * 上传
	 */
	@RequestMapping(value="/files" ,method = RequestMethod.POST)
	public HashMap<String, Object> upload(@RequestParam MultipartFile[] file){
		//加密所有文件
		return imageService.uploadFiles(file, true);
	}
	
	/**
	 * 图片压缩
	 */
	@RequestMapping(value = "/image/thumbnail" , method = RequestMethod.POST)
	public HashMap<String, Object> thumbnail(){
		return null;
	}
	
	/**
	 * 获取文件
	 */
	@RequestMapping(value="/files/{name:.*}", method = RequestMethod.GET)
	public void findByName(@PathVariable String name, HttpServletResponse response){
		System.out.println(name);
		imageService.findByName(name, response);
	}
}
