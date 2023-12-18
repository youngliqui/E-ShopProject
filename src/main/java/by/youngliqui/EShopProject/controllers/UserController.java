package by.youngliqui.EShopProject.controllers;

import by.youngliqui.EShopProject.dto.UserDTO;
import by.youngliqui.EShopProject.dto.UsersResponse;
import by.youngliqui.EShopProject.exceptions.UserErrorResponse;
import by.youngliqui.EShopProject.exceptions.UserNotCreatedException;
import by.youngliqui.EShopProject.models.User;
import by.youngliqui.EShopProject.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public UsersResponse getUsers() {
        return new UsersResponse(userService.getAll().stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList())
        );
    }


    @PostMapping("/new")
    public ResponseEntity<HttpStatus> saveUser(@RequestBody @Valid UserDTO userDTO,
                                               BindingResult bindingResult) {
        StringBuilder errorMsg = new StringBuilder();

        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg
                        .append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }

            throw new UserNotCreatedException(errorMsg.toString());
        }

        if (userService.save(userDTO)) {
            return ResponseEntity.ok(HttpStatus.OK);
        } else {
            throw new UserNotCreatedException(errorMsg.toString());
        }
    }

    @GetMapping("/profile")
    public UserDTO profileUser(Principal principal) {
        if (principal == null) {
            throw new RuntimeException("You are not authorize"); // add notAuthorizeException
        }
        User user = userService.findByName(principal.getName());

        return UserDTO.builder()
                .username(user.getName())
                .email(user.getEmail())
                .build();
    }

    @PostMapping("/profile")
    public ResponseEntity<UserDTO> updateProfileUser(@RequestBody @Valid UserDTO dto,
                                                        BindingResult bindingResult,
                                                        Principal principal) {
        if (principal == null || !Objects.equals(principal.getName(), dto.getUsername())) {
            throw new RuntimeException("You are not authorize"); // create notAuthorizeException
        }
        if (dto.getPassword() != null
                && !dto.getPassword().isEmpty()
                && !Objects.equals(dto.getPassword(), dto.getMatchingPassword())) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(dto); // add message
        }

        userService.updateProfile(dto);
        return ResponseEntity.ok(dto);
    }


    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotCreatedException e) {
        UserErrorResponse response = new UserErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private User convertToUser(UserDTO userDTO) {
        return null;
    }

    private UserDTO convertToUserDTO(User user) {
        return UserDTO.builder()
                .username(user.getName())
                .email(user.getEmail())
                .build();
    }
}
