package chen.e3.sso.service;

import chen.e3.entity.TbUser;
import chen.e3.utils.E3Result;

public interface RegisterService {

	E3Result checkData(String param,int type);

	E3Result register(TbUser user);
}
