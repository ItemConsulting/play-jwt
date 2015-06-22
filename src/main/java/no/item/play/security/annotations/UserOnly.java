package no.item.play.security.annotations;

import com.auth0.jwt.JWTVerifier;
import no.item.play.security.Claim;
import no.item.play.security.Constants;
import no.item.play.security.verifiers.UserVerifier;
import play.Logger;
import play.Play;
import play.mvc.Http;
import play.mvc.Security;

import java.util.Map;
import java.util.Optional;

public class UserOnly extends Security.Authenticator implements Http.Status, Constants {
    private static final String NO_USERNAME = null;

    @Override
    public String getUsername(Http.Context context) {
            String token = context.request().cookie(TOKEN).value();

            return verify(token).map(Claim::id).orElse(NO_USERNAME);
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
        return Play.application().injector().instanceOf(UserVerifier.class);
    }
}
