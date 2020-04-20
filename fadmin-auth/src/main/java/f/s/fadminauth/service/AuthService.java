package f.s.fadminauth.service;

import f.s.fadminauth.util.user.JwtAuthenticationRequest;
public interface AuthService {
    String login(JwtAuthenticationRequest authenticationRequest) throws Exception;
    String refresh(String oldToken) throws Exception;
    void validate(String token) throws Exception;
}
