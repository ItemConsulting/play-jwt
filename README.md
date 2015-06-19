# Play jwt
Json Web Token support for [Play Framework 2.4.x](https://playframework.com/)

## Log in

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

 A claim is a HashMap that consists of 3 parts; 
 1. The user-id, a String, which you retrieve from a database or w/e
 2. The audience, a list, of either one or two ENUM's 
    USER and/or ADMIN, this is to give the user a limitation of access
 3. The optional timestamp, in epoch seconds, of when the session is created

Use the [ClaimBuilder](src/main/java/no/item/play/security/ClaimBuilder.java) to create a Claim. Remember that an admin is ADMIN and USER, but a regular user is 
only USER

## Log out


```java
public Result logout(){
    security.removeToken();
    return ok();
}           
``` 

## Authentication

Verify programatically.

```java
//Verify that the user is an admin
if(security.verifyAdmin()) {
    //Do something
} else {
    //Do something else
}
```      
  
Verify by annotation.
  
```java
@Security.Authenticated(AdminOnly.class)
public Result index() {
  return ok();
}
//Change to UserOnly.class to check if the user only has user access
```      
        
