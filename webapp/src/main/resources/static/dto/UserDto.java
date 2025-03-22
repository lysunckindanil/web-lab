package org.example.weblab.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link org.example.weblab.entity.User}
 */

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserDto implements Serializable {
    @Size(min = 4, max = 20, message = "Username should be between 4 and 20 characters.")
    @NotEmpty(message = "Username should not be empty.")
    private String username;

    @Size(min = 4, max = 20, message = "Password should be between 4 and 20 characters.")
    @NotEmpty(message = "Password should not be empty.")
    private String password;
}