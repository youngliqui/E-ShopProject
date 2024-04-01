package by.youngliqui.EShopProject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Информация о пользователе")
public class UserDTO {
    @Schema(description = "Имя пользователя")
    private String username;
    private String password;
    private String matchingPassword;
    @Schema(description = "Электронная почта пользователя")
    private String email;
}
