package no.item.play.security;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class ClaimBuilder implements Constants {
    public String id;
    public Map<String, Object> attributes = new HashMap<>();
    public List<String> audience = new ArrayList<>();

    public static ClaimBuilder create(){
        return new ClaimBuilder();
    }

    public ClaimBuilder id(String id){
        this.id = id;
        return this;
    }

    public ClaimBuilder audience(Audience... audience){
        List<String> data = Arrays.stream(audience)
                .map(a -> a.name)
                .collect(Collectors.toList());
        this.audience.addAll(data);

        return this;
    }

    public ClaimBuilder put(String id, Object obj){
        if(Objects.equals(id, CLAIM_AUDIENCE)){
            addAudience(obj);
        } else {
            attributes.put(id, obj);
        }
        return this;
    }

    public Claim build(){
        checkNotNull(id, CLAIM_ID);
        checkState(!audience.isEmpty());

        Claim claim = new Claim();
        claim.id(id);
        claim.audience(audience);
        claim.putAll(attributes);
        return claim;
    }

    private void addAudience(Object obj){
        if(obj instanceof String){
            audience.add((String) obj);
        } else if(obj instanceof List) {
            audience.addAll((List) obj);
        } else {
            throw new IllegalStateException();
        }
    }
}
