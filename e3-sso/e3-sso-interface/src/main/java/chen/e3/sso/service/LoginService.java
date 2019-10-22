package chen.e3.sso.service;

import chen.e3.entity.TbUser;
import chen.e3.utils.E3Result;

public interface LoginService {

	E3Result login(TbUser user);
}
