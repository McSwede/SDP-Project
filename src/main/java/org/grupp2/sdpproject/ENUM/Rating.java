package org.grupp2.sdpproject.ENUM;

public enum Rating {

    G("General Audience"),
    PG("Parental Guidance Suggested"),
    PG_13("Parents Strongly Cautioned"),
    R("Restricted"),
    NC_17("Adults Only");

    private final String description;

    Rating(String description){
        this.description = description;
    }

}
