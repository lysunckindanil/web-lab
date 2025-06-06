package org.example.webapp.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.example.webapp.util.validation.UniqueUsername;

import java.io.Serializable;

/**
 * DTO for {@link org.example.webapp.model.User}
 */

@Data
@Builder
public class UserDto implements Serializable {
    @Size(min = 4, max = 20, message = "Логин должен быть от 4 до 20 символов")
    @UniqueUsername(message = "Логин занят другим пользователем")
    private String username;

    @Size(min = 4, max = 20, message = "Пароль должен быть от 4 до 20 символов")
    private String password;
}