package no.item.play.security;


public enum Audience {
    USER("user"),
    ADMIN("admin");

    public final String name;

    Audience(String name) {
        this.name = name;
    }
}
