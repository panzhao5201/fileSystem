package org.bsth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

//启动时检查环境
@Component
public class StartupCheck implements CommandLineRunner{
	
	@Autowired
	private StringRedisTemplate redisTemplate;

	@Override
	public void run(String... arg0) throws Exception {
		try {
			redisTemplate.opsForValue().get("aa");
		} catch (Exception e) {
			System.err.println("redis没有正常启动");
			System.exit(0);
		}
	}
}
