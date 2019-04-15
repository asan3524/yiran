package com.yiran.base.authorization.token;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import com.yiran.redis.cache.RedisCacheComponent;

@Component
@ConditionalOnBean(name = "redisProperties")
public class RedisTokenRepositoryImpl implements PersistentTokenRepository {

	@Autowired
	private RedisCacheComponent cacheComponent;

	private Integer tokenValiditySeconds = 300;

	public PersistentTokenRepository validitySeconds(int tokenValiditySeconds) {
		this.tokenValiditySeconds = tokenValiditySeconds;
		return this;
	}

	@Override
	public synchronized void createNewToken(PersistentRememberMeToken token) {
		// TODO Auto-generated method stub
		PersistentRememberMeToken current = cacheComponent.get(token.getSeries(), PersistentRememberMeToken.class);

		if (current != null) {
			throw new DataIntegrityViolationException("Series Id '" + token.getSeries() + "' already exists!");
		}

		cacheComponent.set(token.getSeries(), token, tokenValiditySeconds);

		@SuppressWarnings("unchecked")
		ArrayList<String> userTokens = cacheComponent.get(token.getUsername(), ArrayList.class);

		if (null != userTokens) {
			userTokens.add(token.getSeries());
		} else {
			userTokens = new ArrayList<String>();
			userTokens.add(token.getSeries());
		}
		cacheComponent.set(token.getUsername(), userTokens);
	}

	@Override
	public synchronized void updateToken(String series, String tokenValue, Date lastUsed) {
		// TODO Auto-generated method stub
		PersistentRememberMeToken token = getTokenForSeries(series);

		PersistentRememberMeToken newToken = new PersistentRememberMeToken(token.getUsername(), series, tokenValue,
				new Date());

		// Store it, overwriting the existing one.
		cacheComponent.set(token.getSeries(), newToken, tokenValiditySeconds);
	}

	@Override
	public synchronized PersistentRememberMeToken getTokenForSeries(String seriesId) {
		// TODO Auto-generated method stub
		return cacheComponent.get(seriesId, PersistentRememberMeToken.class);
	}

	@Override
	public synchronized void removeUserTokens(String username) {
		// TODO Auto-generated method stub
		if (null != username) {
			@SuppressWarnings("unchecked")
			ArrayList<String> userTokens = cacheComponent.get(username, ArrayList.class);

			if (null != userTokens) {

				cacheComponent.delete(userTokens);

				cacheComponent.delete(username);
			}
		}
	}
}
