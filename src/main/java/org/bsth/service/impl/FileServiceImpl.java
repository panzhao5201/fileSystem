package org.bsth.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.codec.binary.Base64;
import org.bsth.service.FileService;
import org.bsth.util.Configs;
import org.bsth.util.FileCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;


@Component
public class FileServiceImpl implements FileService {

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Override
	public HashMap<String, Object> uploadFiles(MultipartFile[] files,
			Boolean encryption) {
		//加密存储
		List<HashMap<String, Object>> result = encryptSaveFiles(files);
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("files", result);
		
		return map;
	}

	@Override
	public List<HashMap<String, Object>> encryptSaveFiles(MultipartFile[] files) {
		String name, newName;
		InputStream fis;
		FileOutputStream fos;
		List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map;
		
		for (int i = 0; i < files.length; i++) {
			try {
				// 文件重命名
				name = files[i].getOriginalFilename();
				newName = UUID.randomUUID() + ".crypto";
				
				fis = files[i].getInputStream();
				fos = new FileOutputStream(Configs.ROOTPATH + newName);
				
				//生成随机密匙
				String bytKey = new Md5PasswordEncoder().encodePassword(
						new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()), 
						name + UUID.randomUUID());
				// 加密
				byte[] bs = FileCrypt.encryptByDES(files[i].getBytes(), bytKey.getBytes());
				fos.write(Base64.encodeBase64(bs));
				
				//将密匙和原文件名压入redis
				JSONObject redisVal = new JSONObject();
				redisVal.put("bytKey", bytKey);
				redisVal.put("name", name);
				redisTemplate.opsForValue().set(newName, redisVal.toJSONString());
				
				map = new HashMap<String, Object>();
				map.put("name", name);
				map.put("size", files[i].getSize());
				map.put("url", "/fileSystem/files/"+newName);
				//map.put("thumbnailUrl", "");
				
				result.add(map);
				
				fis.close();
				fos.flush();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
				map = new HashMap<String, Object>();
				map.put("status", -1);
				result.add(map);
			}
		}
		return result;
	}

	@Override
	public void findByName(String name, HttpServletResponse response) {
		
		String suffix = name.substring(name.lastIndexOf(".") , name.length());
		//文件解密
		if(suffix.equals(".crypto")){
			//从redis 里拿出密匙和原文件名
			JSONObject redisVal = JSONObject.parseObject(redisTemplate.opsForValue().get(name));
			String bytKey = redisVal.getString("bytKey");
			String originaName = redisVal.getString("name");
			try {
				File file = new File(Configs.ROOTPATH + name);
				FileInputStream fis = new FileInputStream(file);
				byte[] bs = new byte[(int)file.length()];
				
				fis.read(bs);
				bs = FileCrypt.decryptByDES(Base64.decodeBase64(bs), bytKey.getBytes());
				
				OutputStream fos = response.getOutputStream();
				
				//真实文件类型
				String realSuffix = originaName.substring(originaName.lastIndexOf(".") + 1, originaName.length());
				//头信息
				response.setContentType(Configs.CONTENTTYPES.getString(realSuffix));
				response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(originaName, "UTF-8") );
				fos.write(bs);
				
				fis.close();
				fos.flush();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
