package ru.gb.antonov.j67.entities.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthRequest
{
    private String login;
    private String password;
}
