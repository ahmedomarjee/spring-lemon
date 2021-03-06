package com.naturalprogrammer.spring.lemonreactive.util;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.naturalprogrammer.spring.lemon.commons.security.JwtService;
import com.naturalprogrammer.spring.lemon.commons.util.LecUtils;
import com.naturalprogrammer.spring.lemon.exceptions.VersionException;
import com.naturalprogrammer.spring.lemonreactive.domain.AbstractDocument;
import com.naturalprogrammer.spring.lemonreactive.domain.AbstractMongoUser;
import com.nimbusds.jwt.JWTClaimsSet;

/**
 * Useful helper methods
 * 
 * @author Sanjay Patel
 */
public class LerUtils {
	
	private static final Log log = LogFactory.getLog(LerUtils.class);
	
	/**
	 * Throws BadCredentialsException if 
	 * user's credentials were updated after the JWT was issued
	 */
	public static <U extends AbstractMongoUser<ID>, ID extends Serializable>
	void ensureCredentialsUpToDate(JWTClaimsSet claims, U user) {
		
		long issueTime = (long) claims.getClaim(JwtService.LEMON_IAT);

		log.debug("Ensuring credentials up to date. Issue time = "
				+ issueTime + ". User's credentials updated at" + user.getCredentialsUpdatedMillis());
		
		LecUtils.ensureCredentials(issueTime >= user.getCredentialsUpdatedMillis(),
				"com.naturalprogrammer.spring.obsoleteToken");
	}

	/**
	 * Throws a VersionException if the versions of the
	 * given entities aren't same.
	 * 
	 * @param original
	 * @param updated
	 */
	public static <ID extends Serializable>
	void ensureCorrectVersion(AbstractDocument<ID> original, AbstractDocument<ID> updated) {
		
		if (original.getVersion() != updated.getVersion())
			throw new VersionException(original.getClass().getSimpleName(), original.getId().toString());
	}
}
