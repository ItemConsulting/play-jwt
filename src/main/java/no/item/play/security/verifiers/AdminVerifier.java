package no.item.play.security.verifiers;

import no.item.play.security.annotations.JwtSecret;
import play.Play;

import javax.inject.Inject;
import javax.inject.Named;

import static no.item.play.security.Audience.ADMIN;

public class AdminVerifier extends UserVerifier {
    @Inject
    public AdminVerifier(@JwtSecret String secret) {
        super(secret, ADMIN.name);
    }
}