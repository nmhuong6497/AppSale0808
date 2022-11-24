package com.example.appsale.data.remote.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDTO {


private String email;

private String name;

private String phone;

private String token;

public String getEmail() {
return email;
}

public void setEmail(String email) {
this.email = email;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getPhone() {
return phone;
}

public void setPhone(String phone) {
this.phone = phone;
}

public String getToken() {
return token;
}

public void setToken(String token) {
this.token = token;
}

}