# play-jwt
Json Web Token support for Play Framework 2.4.x

##

```java
        
        //I gotta love those injects
        @Inject
        private JwtSecurity security;
        
       
        
        /*what is a claim, and why use it.
        A claim is a HashMap that consists of 3 parts; 
        1, The user-id, a String, which you retrieve from a database or w/e
        2, The audience,a list, of either one or two ENUM's 
            USER and/or ADMIN, this is to give the user a limitation of access
        3, The optional time-stamp, in epoch seconds, of when the session is created
           
        Use it because it's an easy way of creating a correct claim
        without doing the dirty work yourself, just use the ClaimBuilder.
        Remember that an admin is ADMIN and USER, but a regular user is 
        only USER*/
           
        //Create a token with the ClaimBuilder
        Long timestamp = Instant.now().getEpochSecond();
        Claim claim = ClaimBuilder.create()
            .id("1")
            .audience(USER, ADMIN) 
            .put("iat", timestamp)
            .build();
                
        security.createToken(claim)
            
        //Logout or remove token
        security.removeToken();
            
        /*Wow, that's easy! How do I verify a token?
        it's realy easy too :) 
        */
            
        //Verify that the user is an admin
        if(security.verifyAdmin()) {
            //Do something
        } else {
            //Do something else
        }
            
        //Another way of verifying that the user is an admin
        @Security.Authenticated(AdminOnly.class)
        public Result index() {
            return ok();
        }
        //Change to UserOnly.class to check if the user only has user access
            
  ```      
        
