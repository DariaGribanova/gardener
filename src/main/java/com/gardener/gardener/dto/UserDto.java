package com.gardener.gardener.dto;

import com.gardener.gardener.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String userName;
    private String name;
    private String lastName;
    private String password;
    private UserRole role;
}
