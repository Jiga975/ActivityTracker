package com.example.activitytracker.dto.requestDto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserDto {
//    @NotNull
//    @NotBlank(message = "First name is required")
//    @NotEmpty
//    @Size(min = 3, max = 15)
    private String first_name;
//    @NotNull @NotBlank(message = "Last name is required") @NotEmpty
//    @Size(min = 3, max = 15)
    private String last_name;
//    @NotNull @NotBlank(message = "Phone Number is required") @NotEmpty
//    @Size(min = 9, max = 11)
    private String phone_number;
    @Column(unique = true)
//    @NotNull @NotBlank(message = "Email is required") @NotEmpty @Email
//    @Size(min = 15, max = 50)
    private String email;
//    @NotNull @NotBlank(message = "password is required") @NotEmpty
//    @Size(min = 8, max = 20)
    private String password;
}
