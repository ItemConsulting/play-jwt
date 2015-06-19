package no.item.play.security.annotations;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import no.item.play.security.Claim;
import no.item.play.security.Constants;
import no.item.play.security.verifiers.AdminVerifier;
import play.Logger;
import play.Play;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Map;
import java.util.Optional;

public class AdminOnly extends Security.Authenticator implements Http.Status, Constants {
    private static final String NO_USERNAME = null;

    @Override
    public String getUsername(Http.Context context) {
        return context.response().cookie(TOKEN)
                .map(Http.Cookie::value)
                .flatMap(this::verify)
                .map(Claim::id)
                .orElse(NO_USERNAME);
    }

    @Override
    public Result onUnauthorized(Http.Context context) {
        ObjectNode error = Json.newObject();
        error.put("success", false);
        error.put("code", UNAUTHORIZED);
        error.put("status", "Du har ikke rettighetene til å utføre denne operasjonen.");
        return unauthorized(error);
    }

    private Optional<Claim> verify(String token) {
        try {
            Claim claim = new Claim();
            claim.putAll(verifier().verify(token));
            return Optional.of(claim);
        } catch (Exception e) {
            Logger.info("Rejected login", e);
            return Optional.empty();
        }
    }

    private JWTVerifier verifier(){
        return Play.application().injector().instanceOf(AdminVerifier.class);
    }
}
