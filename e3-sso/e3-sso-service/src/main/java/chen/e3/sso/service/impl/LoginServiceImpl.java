package chen.e3.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import chen.e3.entity.TbUser;
import chen.e3.entity.TbUserExample;
import chen.e3.entity.TbUserExample.Criteria;
import chen.e3.mapper.TbUserMapper;
import chen.e3.sso.service.LoginService;
import chen.e3.utils.E3Result;
import chen.e3.utils.JsonUtils;
import chen.e3.utils.jedis.JedisClient;
@Service
@Transactional
public class LoginServiceImpl implements LoginService {

	@Autowired
	private TbUserMapper userMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${Session_Second}")
	private Integer Session_Second;
	
	public E3Result login(TbUser user) {
		String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		
		TbUserExample example=new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(user.getUsername());
		List<TbUser> list = userMapper.selectByExample(example);
		
		if(list!=null && list.size()>0){
			TbUser tbUser = list.get(0);
			
			if(tbUser.getPassword().equals(password)){
				String token = UUID.randomUUID().toString();
				tbUser.setPassword(null);
				jedisClient.set("Session:"+token, JsonUtils.objectToJson(tbUser));
				jedisClient.expire("Session:"+token, Session_Second);
				
				return E3Result.ok(token);
			}else{
				return E3Result.build(400, "密码错误！！！");
			}
		}else {
			return E3Result.build(400, "用户名错误！！！");
		}
	}

}
