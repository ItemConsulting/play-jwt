package no.item.play.security;


import no.item.play.security.verifiers.AdminVerifier;
import no.item.play.security.verifiers.UserVerifier;
import play.Play;
import javax.inject.Inject;
import java.util.Map;
import java.util.Optional;

import static play.mvc.Controller.response;
import static play.mvc.Controller.session;

public class JwtSecurity implements Constants {
    private final Signer signer;
    private final AdminVerifier adminVerifier;
    private final UserVerifier userVerifier;

    @Inject
    public JwtSecurity(Signer signer, AdminVerifier adminVerifier, UserVerifier userVerifier) {
        this.signer = signer;
        this.adminVerifier = adminVerifier;
        this.userVerifier = userVerifier;
    }

    public boolean verifyUser() {
        return token().map(userVerifier::isValid).orElse(Boolean.FALSE);
    }
    public boolean verifyAdmin() {
        return token().map(adminVerifier::isValid).orElse(Boolean.FALSE);
    }

    public void removeToken() {
        response().discardCookie(TOKEN);
        //change from session().remove(TOKEN);
    }

    public Map<String, Object> createToken(Claim claim){
        String token = signer.sign(claim);
        response().setCookie(TOKEN, token);
        //session(TOKEN, token);

        return claim;
    }

    private Optional<String> token(){
        //Changed from session.get(TOKEN)
        return Optional.ofNullable(response().cookie(TOKEN).get().value());
    }
}
