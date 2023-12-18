package by.youngliqui.EShopProject.services;

import by.youngliqui.EShopProject.dto.UserDTO;
import by.youngliqui.EShopProject.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    boolean save(UserDTO userDTO);
    List<User> getAll();
    User findByName(String name);
    void updateProfile(UserDTO userDTO);
}
