package com.my.db.entities;

import java.io.Serializable;

public class User implements Serializable {

    private int id;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String localeName;
    private int roleId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLocaleName() {
        return localeName;
    }

    public void setLocaleName(String localeName) {
        this.localeName = localeName;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleID) {
        this.roleId = roleID;
    }

    @Override
    public String toString() {
        return id + " " + login + " " + firstName + " " + lastName + " " + localeName + " " + roleId;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)) {
            return false;
        }
        User anotherUser = (User) obj;
        if (!(id == anotherUser.getId())) {
            return false;
        }
        if (login != null && !login.equals(anotherUser.getLogin())) {
            return false;
        } else {
            if (login == null && anotherUser.getLogin() == null) {
                return true;
            }
        }
        if (password != null && !password.equals(anotherUser.getPassword())) {
            return false;
        } else {
            if (password == null && anotherUser.getPassword() == null) {
                return true;
            }
        }
        if (!firstName.equals(anotherUser.getFirstName())) {
            return false;
        }
        if (!lastName.equals(anotherUser.getLastName())) {
            return false;
        }
        if (!localeName.equals(anotherUser.getLocaleName())) {
            return false;
        } else {
            if (localeName == null){
                return true;
            }
        }
        return roleId == anotherUser.getRoleId();
    }
}
