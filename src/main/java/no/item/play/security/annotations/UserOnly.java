package no.item.play.security.annotations;

import com.auth0.jwt.JWTVerifier;
import no.item.play.security.Claim;
import no.item.play.security.Constants;
import no.item.play.security.verifiers.UserVerifier;
import play.Play;
import play.mvc.Http;
import play.mvc.Security;

import java.util.Map;

public class UserOnly extends Security.Authenticator implements Http.Status, Constants {
    @Override
    public String getUsername(Http.Context context) {
        try {
            String token = context.session().get(TOKEN);
            Claim claim = new Claim();
            claim.putAll(verify(token));
            return claim.id();
        } catch(Exception e){
            return null;
        }
    }

    private Map<String, Object> verify(String token) throws Exception{
        return verifier().verify(token);
    }

    private JWTVerifier verifier(){
        return Play.application().injector().instanceOf(UserVerifier.class);
    }
}
