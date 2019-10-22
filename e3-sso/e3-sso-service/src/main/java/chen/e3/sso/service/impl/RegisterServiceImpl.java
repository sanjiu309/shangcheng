package chen.e3.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import chen.e3.entity.TbUser;
import chen.e3.entity.TbUserExample;
import chen.e3.entity.TbUserExample.Criteria;
import chen.e3.mapper.TbUserMapper;
import chen.e3.sso.service.RegisterService;
import chen.e3.utils.E3Result;
@Service
@Transactional
public class RegisterServiceImpl implements RegisterService {

	@Autowired
	private TbUserMapper userMapper;
	
	@Override
	public E3Result checkData(String param, int type) {
		TbUserExample example=new TbUserExample();
		Criteria criteria=example.createCriteria();
		if(type == 1){
			criteria.andUsernameEqualTo(param);
		}else if (type == 2) {
			criteria.andPhoneEqualTo(param);
		}else if (type == 3) {
			criteria.andEmailEqualTo(param);
		}else{
			return E3Result.ok(false);
		}
		
		List<TbUser> list = userMapper.selectByExample(example);
		if(list!=null && list.size()>0){
			return E3Result.ok(false);
		}else {
			return E3Result.ok(true);
		}
	}

	@Override
	public E3Result register(TbUser user) {
		if(!StringUtils.isNotBlank(user.getUsername()) || !StringUtils.isNotBlank(user.getPassword()) || !StringUtils.isNotBlank(user.getPhone())){
			return E3Result.build(200, "输入数据错误");
		}
		E3Result result = checkData(user.getUsername(), 1);
		if(!(boolean) result.getData()){
			return E3Result.build(200, "用户名重复");
		}
		result=checkData(user.getPhone(), 2);
		if(!(boolean) result.getData()){
			return E3Result.build(200, "手机号重复");
		}
		
		user.setCreated(new Date());
		user.setUpdated(new Date());
		
		String md5 = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5);
		
		userMapper.insert(user);
		return E3Result.ok();
	}

}
