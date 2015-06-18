package no.item.play.security.annotations;

import com.auth0.jwt.JWTVerifier;
import com.fasterxml.jackson.databind.node.ObjectNode;
import no.item.play.security.Claim;
import no.item.play.security.Constants;
import no.item.play.security.verifiers.AdminVerifier;
import play.Play;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import java.util.Map;

public class AdminOnly extends Security.Authenticator implements Http.Status, Constants {
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

    @Override
    public Result onUnauthorized(Http.Context context) {
        ObjectNode error = Json.newObject();
        error.put("success", false);
        error.put("code", UNAUTHORIZED);
        error.put("status", "Du har ikke rettighetene til å utføre denne operasjonen.");
        return unauthorized(error);
    }

    private Map<String, Object> verify(String token) throws Exception{
        return verifier().verify(token);
    }

    private JWTVerifier verifier(){
        return Play.application().injector().instanceOf(AdminVerifier.class);
    }
}
