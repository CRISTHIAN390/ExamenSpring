package com.ingweb.springboot.web.app.controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ingweb.springboot.web.app.services.UserService;
import com.ingweb.springboot.web.app.ApiResponse.*;
import com.ingweb.springboot.web.app.request.*;
import com.ingweb.springboot.web.app.entity.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> list() {
        List<User> users = userService.listAll();
        ApiResponse<List<User>> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Lista de usuarios exitosamente");
        response.setData(users);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> delete(@PathVariable int id) {
        userService.delete(id);
        ApiResponse<User> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Usuario eliminado exitosamente");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> edit(@PathVariable int id, @RequestBody UserEditReq userRequest) {
        User user = userService.update(id, userRequest);
        user.setEstado(true);
        ApiResponse<User> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("El usuario se actualizó exitosamente");
        response.setData(user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}