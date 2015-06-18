package no.item.play.security;

import com.auth0.jwt.JWTSigner;
import no.item.play.security.annotations.JwtSecret;
import play.Play;

import javax.inject.Inject;

public class Signer extends JWTSigner {
    @Inject
    public Signer(@JwtSecret String secret) {
        super(secret);
    }
}
