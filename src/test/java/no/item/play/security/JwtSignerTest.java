package no.item.play.security;


import static no.item.play.security.Audience.ADMIN;
import static no.item.play.security.Audience.USER;

import com.auth0.jwt.JWTVerifyException;
import no.item.play.security.verifiers.AdminVerifier;
import static org.fest.assertions.Assertions.*;
import static org.fest.assertions.MapAssert.entry;
import static org.junit.Assert.*;

import no.item.play.security.verifiers.UserVerifier;
import org.fest.assertions.MapAssert;
import org.junit.Test;
import play.Logger;
import play.libs.Json;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JwtSignerTest {

    @Test
    public void testRoundAdmin() throws SignatureException, NoSuchAlgorithmException, JWTVerifyException, InvalidKeyException, IOException {
        Signer signer = new Signer("hej");
        AdminVerifier aVerifier = new AdminVerifier("hej");

        Claim claim = ClaimBuilder.create()
                        .id("1")
                        .audience(ADMIN)
                        .build();

        String token = signer.sign(claim);
        Map<String, Object> verified = aVerifier.verify(token);
        Logger.info(Json.toJson(verified).toString());
        List<String> adminList = new ArrayList<>();
        adminList.add("admin");

        assertNotNull(token);

        assertThat(verified).includes(entry("id", "1"));



        assertThat(verified.get("aud")).isEqualTo(adminList);

    }

    @Test
    public void testRoundUser() throws SignatureException, NoSuchAlgorithmException, JWTVerifyException, InvalidKeyException, IOException {
        Signer signer = new Signer("hej");
        UserVerifier sVerifier = new UserVerifier("hej");

        Claim claim = ClaimBuilder.create()
                .id("1")
                .audience(USER)
                .build();

        String token = signer.sign(claim);
        Map<String, Object> verified = sVerifier.verify(token);
        Logger.info(Json.toJson(verified).toString());
        List<String> userList = new ArrayList<>();
        userList.add(USER.name);

        assertNotNull(token);

        assertThat(verified).includes(entry("id", "1"));

        assertThat(verified.get("aud")).isEqualTo(userList);
    }

    @Test(expected = SignatureException.class)
    public void testSignVerifyNotSameSecret() throws SignatureException, NoSuchAlgorithmException, JWTVerifyException, InvalidKeyException, IOException {
        Signer signer = new Signer("hej");
        UserVerifier sVerifier = new UserVerifier("hello");

        Claim claim = ClaimBuilder.create()
                .id("1")
                .audience(USER)
                .build();

        String token = signer.sign(claim);
        sVerifier.verify(token);
    }

    @Test
    public void testManyAttributes() throws SignatureException, NoSuchAlgorithmException, JWTVerifyException, InvalidKeyException, IOException {
        Signer signer = new Signer("hej");
        AdminVerifier aVerifier = new AdminVerifier("hej");

        Claim claim = ClaimBuilder.create()
                .id("1")
                .id("2")
                .audience(USER)
                .audience(ADMIN)
                .put("mykey", "myvalue")
                .put("mykey2", "myvalue2")
                .build();
        Logger.info(Json.toJson(claim).toString());
        String token = signer.sign(claim);
        Map<String, Object> verified = aVerifier.verify(token);

        assertThat(token).isNotNull();
        List<String> audience = new ArrayList<>();
        audience.add(USER.name);
        audience.add(ADMIN.name);

        assertThat(verified).includes(entry("id", "2"), entry("mykey", "myvalue"),
                            entry("mykey2", "myvalue2"), entry("aud", audience));

    }
}
