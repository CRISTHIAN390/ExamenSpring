package com.ingweb.springboot.web.app.repositories;
import com.ingweb.springboot.web.app.entity.User;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository  extends JpaRepository<User,Integer> {
    User findById(int id);
    List<User> findByEstado(boolean estado);
}
