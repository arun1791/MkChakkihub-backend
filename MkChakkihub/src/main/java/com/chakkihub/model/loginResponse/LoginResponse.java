package com.chakkihub.model.loginResponse;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Data
@NoArgsConstructor
public class LoginResponse {
    private String token;
    private String username;
    private String role;
    private String loginTime;
    private Long userId;
    private String email;

    // getters and setters
}
