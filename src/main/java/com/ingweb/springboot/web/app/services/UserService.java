package com.ingweb.springboot.web.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ingweb.springboot.web.app.entity.User;
import com.ingweb.springboot.web.app.entity.Rol;
import com.ingweb.springboot.web.app.request.UserEditReq;
import com.ingweb.springboot.web.app.repositories.UserRepository;
import com.ingweb.springboot.web.app.repositories.RolRepository;
import lombok.RequiredArgsConstructor;
import java.util.List;
@Service
@RequiredArgsConstructor
public class UserService {
       @Autowired
    private UserRepository userRepository;
    private final RolRepository roleRepository;

    public List<User> listAll() {
        return userRepository.findByEstado(true);
    }

    public User getById(int id) {
        return userRepository.findById(id);
    }

    public void delete(int id) {
        User user = userRepository.findById(id);
        user.setEstado(false);
        userRepository.save(user);
    }

    public User update(int id, UserEditReq resource){
        Rol role = roleRepository.findById(resource.getRol()).orElseThrow(() -> new IllegalArgumentException("Default role not found"));
        if (userRepository.existsById(id)) {
            User user = userRepository.findById(id);
            user.setEmail(resource.getEmail());
            user.setRol(role);
            if(resource.getPassword()!=""){
                user.setPassword(resource.getPassword());
            }
            return userRepository.save(user);
        } else
            return null;
    }
}
