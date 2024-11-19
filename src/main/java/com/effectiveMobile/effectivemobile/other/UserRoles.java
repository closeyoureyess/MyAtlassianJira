package com.effectiveMobile.effectivemobile.other;

public enum UserRoles {

    ADMIN("ADMIN"),
    USER("USER");

    private String userRoles;

    UserRoles(String userRoles) {
        this.userRoles = userRoles;
    }

    public String getUserRoles() {
        return userRoles;
    }
}
