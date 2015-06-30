package no.item.play.security;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import no.item.play.security.verifiers.AdminVerifier;
import no.item.play.security.verifiers.UserVerifier;
import play.Logger;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.*;

import static java.util.Locale.filter;
import static play.mvc.Controller.request;
import static play.mvc.Controller.response;
import static play.mvc.Http.Status.UNAUTHORIZED;
import static play.mvc.Results.noContent;
import static play.mvc.Results.unauthorized;

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
    }

    public Map<String, Object> createToken(Claim claim){
        String token = signer.sign(claim);
        response().setCookie(TOKEN, token);

        return claim;
    }

    public Optional<String> id() {
        return value("id", String.class);
    }

    public boolean verify(Audience audience){
        return audience().stream()
                .filter(a -> Objects.equals(a, audience.name))
                .findFirst()
                .isPresent();
    }

    @SuppressWarnings("unchecked")
    private List<String> audience() {
        return value("aud").map(aud -> (List<String>) aud)
                .orElse(Lists.newArrayList());
    }

    public <T> Optional<T> value(String key, Class<T> cls) {
        try {
            return value(key).map(cls::cast);
        } catch(ClassCastException e){
            Logger.error("Can't cast Object to {}", cls.getSimpleName(), e);
            return Optional.empty();
        }
    }

    public Optional<Object> value(String key) {
        return userClaim().map(claim -> claim.get(key));
    }

    public Optional<Map<String, Object>> userClaim() {
        return token().flatMap(userVerifier::claim);
    }

    private Optional<String> token(){
        return Optional.ofNullable(request().cookie(TOKEN))
                .map(Http.Cookie::value);
    }
}
