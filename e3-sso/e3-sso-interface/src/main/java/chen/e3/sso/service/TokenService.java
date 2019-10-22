package chen.e3.sso.service;
/**
 * 根据token查询用户信息
 * @author chen
 *
 */

import chen.e3.utils.E3Result;

public interface TokenService {

	E3Result getUserByToken(String token);
}
