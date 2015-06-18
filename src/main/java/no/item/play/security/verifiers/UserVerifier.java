package no.item.play.security.verifiers;

import com.auth0.jwt.JWTVerifier;
import no.item.play.security.Constants;
import no.item.play.security.annotations.JwtSecret;
import play.Play;

import javax.inject.Inject;
import javax.inject.Named;

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

    public Boolean isValid(String token){
        try {
            return verify(token).containsKey(CLAIM_ID);
        } catch (Exception e) {
            return false;
        }
    }
}
