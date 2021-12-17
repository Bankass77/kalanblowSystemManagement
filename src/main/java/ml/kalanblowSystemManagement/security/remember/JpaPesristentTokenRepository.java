
package ml.kalanblowSystemManagement.security.remember;

import java.util.Date;
import java.util.Set;

import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import lombok.extern.slf4j.Slf4j;
import ml.kalanblowSystemManagement.model.PersistentLogin;

@Repository("persistentTokenRepository")
@Transactional
@Slf4j
public class JpaPesristentTokenRepository implements PersistentTokenRepository {

	private final RememberMeTokenRepository rememberMeTokenRepository;
	
	public JpaPesristentTokenRepository(RememberMeTokenRepository rememberMeTokenRepository) {
		super();
		this.rememberMeTokenRepository = rememberMeTokenRepository;
	}

	@Override
	public void createNewToken(PersistentRememberMeToken token) {
		
		log.info("Creating Token for user : {}", token.getUsername());
		PersistentLogin newToken = new PersistentLogin(token);
		newToken.setLastUsed(token.getDate());
		newToken.setUsername(token.getUsername());
		newToken.setSeries(token.getSeries());
		newToken.setToken(token.getTokenValue());
		this.rememberMeTokenRepository.save(newToken);

	}

	@Override
	public void updateToken(String series, String tokenValue, Date lastUsed) {
		log.info("Updating Token for seriesId : {}", series);
		PersistentLogin token = this.rememberMeTokenRepository.findBySeries(series);
		if (token != null) {

			token.setToken(tokenValue);
			token.setLastUsed(lastUsed);
			token.setUsername(token.getUsername());
	

		}

	}

	@Override
	public PersistentRememberMeToken getTokenForSeries(String seriesId) {
		log.info("Fetch Token if any for seriesId : {}", seriesId);
		PersistentLogin token = this.rememberMeTokenRepository.findBySeries(seriesId);

		return new PersistentRememberMeToken(token.getUsername(), token.getSeries(), token.getToken(),
				token.getLastUsed());
	}

	@Override
	public void removeUserTokens(String username) {
		log.info("Removing Token if any for user : {}", username);
		Set<PersistentLogin> tokens = this.rememberMeTokenRepository.findByUsername(username);

		if (!tokens.isEmpty()) {
			for (PersistentLogin login : tokens) {

				this.rememberMeTokenRepository.delete(login);

			}

		}

	}

}
