package com.dua.fusegram.Utils;

class User {

    public String user_id;
    public String username;
    public String pname;
    public String email;
    public String contact;
    public String dob;

    public User(){}

    public User(String contact, String dob, String email, String pname, String user_id, String username) {
        this.user_id=user_id;
        this.username = username;
        this.pname = pname;
        this.email = email;
        this.contact = contact;
        this.dob = dob;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    @Override
    public String toString() {
        return "User{" +
                "contact='" + contact + '\'' +
                ", dob='" + dob+ '\'' +
                ", email='" + email + '\'' +
                ", name='" + pname + '\'' +
                ", user_id='" + user_id + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}

