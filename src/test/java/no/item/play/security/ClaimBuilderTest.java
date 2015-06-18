package no.item.play.security;

import org.junit.Test;

import javax.inject.Inject;
import java.time.Instant;
import java.util.List;

import static no.item.play.security.Audience.ADMIN;
import static no.item.play.security.Audience.USER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static no.item.play.security.Constants.*;
import static org.fest.assertions.Assertions.*;

public class ClaimBuilderTest {


    @Test
    public void testBuild(){

        Long timestamp = Instant.now().getEpochSecond();
        Claim claim = ClaimBuilder.create()
                .id("1")
                .audience(USER, ADMIN)
    .put("iat", timestamp)
    .put("myparam", "myparam")
    .put("aud", 1)
    .build();

    assertTrue(claim.get("id").equals("1"));

    List<String> audience = (List<String>) claim.get(CLAIM_AUDIENCE);
    assertThat(audience).contains(USER.name);
    assertThat(audience).contains(ADMIN.name);

    assertThat(claim.get("iat")).isEqualTo(timestamp);
    assertThat(claim.get("myparam")).isEqualTo("myparam");
}

    @Test(expected = NullPointerException.class)
    public void testBuildMissingId(){
        Claim claim = ClaimBuilder.create()
                .audience(USER, ADMIN)
                .build();
    }


    @Test(expected = IllegalStateException.class)
    public void testBuildMissingAudience(){
        Claim claim = ClaimBuilder.create()
                .id("1")
                .build();
    }
}
