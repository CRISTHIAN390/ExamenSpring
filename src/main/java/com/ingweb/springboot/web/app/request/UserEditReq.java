package com.ingweb.springboot.web.app.request;
import lombok.Data;

@Data
public class UserEditReq {
    String username;
    String email;
    String password;
    int rol;
}
