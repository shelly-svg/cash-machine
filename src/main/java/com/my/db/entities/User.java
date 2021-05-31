package com.my.db.entities;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {

    private int id;
    private String login;
    private String password;
    private String salt;
    private String firstName;
    private String lastName;
    private String email;
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

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && roleId == user.roleId && Objects.equals(login, user.login) && Objects.equals(password, user.password) && Objects.equals(salt, user.salt) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email) && Objects.equals(localeName, user.localeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, salt, firstName, lastName, email, localeName, roleId);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", localeName='" + localeName + '\'' +
                ", roleId=" + roleId +
                '}';
    }
}
