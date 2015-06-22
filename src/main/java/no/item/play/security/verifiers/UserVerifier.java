package no.item.play.security.verifiers;

import com.auth0.jwt.JWTVerifier;
import no.item.play.security.Constants;
import no.item.play.security.annotations.JwtSecret;
import play.Play;

import javax.inject.Inject;
import javax.inject.Named;

import java.util.Map;
import java.util.Optional;

import static no.item.play.security.Audience.ADMIN;
import static no.item.play.security.Audience.USER;

public class UserVerifier extends JWTVerifier implements Constants {
    @Inject
    public UserVerifier(@JwtSecret String secret) {
        this(secret, USER.name);
    }

    public UserVerifier(String secret, String audience) {
        super(secret, audience);
    }


    public Optional<Map<String, Object>> claim(String token){
        try {
            return Optional.of(verify(token));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Boolean isValid(String token){
        return claim(token)
                .map(t -> t.containsKey(CLAIM_ID))
                .orElse(Boolean.FALSE);
    }
}
