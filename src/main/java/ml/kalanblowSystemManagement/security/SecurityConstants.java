
package ml.kalanblowSystemManagement.security;

public interface SecurityConstants {

    String SECRET = "SecretKeyToGenJWTs";
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
    String SIGN_UP_URL = "/users/signup";
    long EXPIRATION_TIME = 300; // 5mn

}
