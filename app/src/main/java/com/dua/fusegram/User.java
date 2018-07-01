package com.dua.fusegram;

class User {

    String Username;
    String Pname;
    String Email;
    String Contact;
    String DOB;
    long createAt;

    public User(){}

    public User(String username, String pname, String email, String contact, String DOB, long createAt) {
        this.Username = username;
        this.Pname = pname;
        this.Email = email;
        this.Contact = contact;
        this.DOB = DOB;
        this.createAt = createAt;
    }

    public String getUsername() {
        return Username;
    }

    public String getPname() {
        return Pname;
    }

    public String getEmail() {
        return Email;
    }

    public String getContact() {
        return Contact;
    }

    public String getDOB() {
        return DOB;
    }
    public long getCreateAt() {
        return createAt;
    }
}

