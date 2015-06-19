# Play jwt
Json Web Token support for [Play Framework 2.4.x](https://playframework.com/)

## Secret

Bind the secret you want to use 

```java
import com.google.inject.AbstractModule;
import no.item.play.security.annotations.JwtSecret;
import play.Configuration;
import play.Environment;

public class DefaultModule extends AbstractModule {

    private final Environment environment;
    private final Configuration config;

    public DefaultModule(Environment environment, Configuration config) {
        this.environment = environment;
        this.config = config;
    }

    @Override
    protected void configure() {
        bindConstant().annotatedWith(JwtSecret.class).to(config.getString("play.crypto.secret"));
    }
}

```

## Log in

Initiating [JwtSecurity](src/main/java/no/item/play/security/JwtSecurity.java) by Injection

Basic steps to create a claim:
1. Initiate the ClaimBuilder
2. set Id
3. set audience
4. build, returns a correct claim
```java
class System extends Controller {
    @Inject
    private JwtSecurity security;
    
    public Result login(){
        Claim claim = ClaimBuilder.create()
            .id("1")
            .audience(USER, ADMIN) 
            .build();
                
        security.createToken(claim);
        return ok(claim);
    }
}
   
```
  
### Claim

 A claim is a HashMap that consists of 4 parts; 
 1. The user-id, a String, which you retrieve from a database or w/e
 2. The [audience](src/main/java/no/item/play/security/Audience.java), a list, 
    USER and/or ADMIN, this is to give a user limitation of access
 3. The optional timestamp, in epoch seconds, of when the session is created
 4. Optional inputs using the put-method on ClaimBuilder, e.g. email

Use the [ClaimBuilder](src/main/java/no/item/play/security/ClaimBuilder.java) to create a Claim. Remember that an admin is ADMIN and USER, but a regular user is 
only USER

## Log out

Deletes the token
```java
public Result logout(){
    security.removeToken();
    return ok();
}           
``` 

## Authentication

Verify programmatically.

Verify that the user is an admin
```java

if(security.verifyAdmin()) {
    //Do something
} else {
    //Do something else
}
```   
Change to .verifyUser, to check if the user only has user access
  
Verify by annotation. 
```java
@Security.Authenticated(AdminOnly.class)
public Result index() {
  return ok();
}

```  
Change to UserOnly.class to check if the user only has user access
        
