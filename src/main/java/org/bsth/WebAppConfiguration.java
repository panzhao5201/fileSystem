package org.bsth;

import javax.servlet.Filter;

import org.bsth.util.Tools;
import org.springframework.boot.autoconfigure.web.MultipartProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HttpPutFormContentFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ComponentScan
public class WebAppConfiguration extends WebMvcConfigurerAdapter{
	
	//没有这个过滤器，controller里获取不到put请求的参数
	@Bean
	public Filter httpPutFormContentFilter() {
		return new HttpPutFormContentFilter();
	}
	
	//设置上传文件最大100M
	@Bean
	public MultipartProperties multipartProperties (){
		MultipartProperties mp = new MultipartProperties();
		String MAXFILESIZE = new Tools("resource.properties").getValue("maxFileSize");
		mp.setMaxFileSize(MAXFILESIZE);
		mp.setMaxRequestSize(MAXFILESIZE);
		return mp;
	}
}
