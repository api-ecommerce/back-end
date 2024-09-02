package com.ecommerce.entities.user;

public enum UserRole {

    ADMIN(1),
    USER(0);

    private int role;

    UserRole(int role){
        this.role = role;
    }

    public int getRole(){
        return role;
    }
}
