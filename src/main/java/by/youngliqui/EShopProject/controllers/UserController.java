package by.youngliqui.EShopProject.controllers;

import by.youngliqui.EShopProject.dto.UserDTO;
import by.youngliqui.EShopProject.dto.UsersResponse;
import by.youngliqui.EShopProject.exceptions.UserNotAuthorizeException;
import by.youngliqui.EShopProject.exceptions.UserNotCreatedException;
import by.youngliqui.EShopProject.mappers.UserMapper;
import by.youngliqui.EShopProject.models.User;
import by.youngliqui.EShopProject.services.UserService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Tag(name = "Пользователи", description = "методы для работы с пользователями")
@OpenAPIDefinition(info = @Info(title = "E-SHOP API", version = "v1"))
@SecurityRequirement(name = "basicAuth")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper = UserMapper.MAPPER;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @Operation(summary = "Получение всех пользователей")
    public UsersResponse getUsers() {
        return new UsersResponse(userService.getAll().stream()
                .map(userMapper::fromUser)
                .collect(Collectors.toList())
        );
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PostMapping("/new")
    @Operation(summary = "Добавление нового пользователя")
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

    @PostAuthorize("isAuthenticated() and #username == authentication.principal.username")
    @GetMapping("/{name}/roles")
    @ResponseBody
    @Operation(summary = "Получение ролей пользователя")
    public String getRoles(@Parameter(description = "Имя пользователя") @PathVariable("name") String username) {
        User byName = userService.findByName(username);
        return byName.getRole().name();
    }


    @GetMapping("/profile")
    @Operation(summary = "Получение информации о профиле пользователя")
    public UserDTO profileUser(Principal principal) {
        if (principal == null) {
            throw new UserNotAuthorizeException("You are not authorize");
        }
        User user = userService.findByName(principal.getName());

        return UserDTO.builder()
                .username(user.getName())
                .email(user.getEmail())
                .build();
    }

    @PostMapping("/profile")
    @Operation(summary = "Изменение профиля пользователя")
    public ResponseEntity<String> updateProfileUser(@RequestBody @Valid UserDTO dto,
                                                    BindingResult bindingResult,
                                                    Principal principal) {

        if (principal == null) {
            throw new UserNotAuthorizeException("You are not authorize");
        }
        if (dto.getPassword() != null
                && !dto.getPassword().isEmpty()
                && !Objects.equals(dto.getPassword(), dto.getMatchingPassword())) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Password does not match");
        }

        userService.updateProfile(dto, principal.getName());
        return ResponseEntity.status(HttpStatus.OK).body(String.valueOf(HttpStatus.OK));
    }
}
