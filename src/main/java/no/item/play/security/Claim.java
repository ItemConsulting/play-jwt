package no.item.play.security;

import java.util.HashMap;
import java.util.List;

public class Claim extends HashMap<String, Object> implements Constants {
    public String id(){
        return (String) get(CLAIM_ID);
    }

    public void id(String id){
        put(CLAIM_ID, id);
    }

    public void audience(List<String> audience){
        put(CLAIM_AUDIENCE, audience);
    }
}
