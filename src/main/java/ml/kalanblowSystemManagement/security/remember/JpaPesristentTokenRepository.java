
package ml.kalanblowSystemManagement.security.remember;

import java.util.Date;
import java.util.Set;

import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ml.kalanblowSystemManagement.model.PersistentLogin;
import ml.kalanblowSystemManagement.repository.RememberMeTokenRepository;

@Repository("persistentTokenRepository")
@Transactional
public class JpaPesristentTokenRepository implements PersistentTokenRepository {

	private final RememberMeTokenRepository rememberMeTokenRepository;

	public JpaPesristentTokenRepository(RememberMeTokenRepository rememberMeTokenRepository) {
		super();
		this.rememberMeTokenRepository = rememberMeTokenRepository;
	}

	@Override
	public void createNewToken(PersistentRememberMeToken token) {
		PersistentLogin newToken = new PersistentLogin(token);
		this.rememberMeTokenRepository.save(newToken);

	}

	@Override
	public void updateToken(String series, String tokenValue, Date lastUsed) {

		PersistentLogin token = this.rememberMeTokenRepository.findBySeries(series);
		if (token != null) {

			token.setToken(tokenValue);
			token.setLastUsed(lastUsed);
			token.setUser(token.getUser());
			token.setFullName(token.getFullName());
			token.setIpAdresse(token.getIpAdresse());
			token.setUser(token.getUser());
		}

	}

	@Override
	public PersistentRememberMeToken getTokenForSeries(String seriesId) {

		PersistentLogin token = this.rememberMeTokenRepository.findBySeries(seriesId);

		return new PersistentRememberMeToken(token.getUser().getFullName(), token.getSeries(), token.getToken(),
				token.getLastUsed());
	}

	@Override
	public void removeUserTokens(String username) {
		Set<PersistentLogin> tokens = this.rememberMeTokenRepository.findByFullName(username);

		if (!tokens.isEmpty()) {
			for (PersistentLogin login : tokens) {

				this.rememberMeTokenRepository.delete(login);

			}

		}

	}

}
