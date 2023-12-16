package by.youngliqui.EShopProject.services;

import by.youngliqui.EShopProject.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    boolean save(UserDTO userDTO);
}
