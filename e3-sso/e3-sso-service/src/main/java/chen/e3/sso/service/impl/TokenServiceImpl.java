package chen.e3.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import chen.e3.entity.TbUser;
import chen.e3.sso.service.TokenService;
import chen.e3.utils.E3Result;
import chen.e3.utils.JsonUtils;
import chen.e3.utils.jedis.JedisClient;
@Service
@Transactional
public class TokenServiceImpl implements TokenService {

	@Autowired
	private JedisClient jedisClient;
	@Value("${Session_Second}")
	private Integer Session_Second;
	
	@Override
	public E3Result getUserByToken(String token) {
		//根据token取redis查询用户信息
		String json = jedisClient.get("Session:"+token);
		//取不到，返回登录已经过期
		if(StringUtils.isBlank(json)){
			return E3Result.build(400, "登录已过期，请重新登录。");
		}
		//取到。更新token过期时间
		jedisClient.expire("Session:"+token, Session_Second);
		//返回用户信息
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
		return E3Result.ok(user);
	}

}
