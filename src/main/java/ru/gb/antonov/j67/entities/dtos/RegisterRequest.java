package ru.gb.antonov.j67.entities.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class RegisterRequest
{
    @NotNull (message="задайте логин (3…32 латинских сиволов и/или цифр)\r")
    @Length (min=3, max=32, message="длина логина — [3…32] латинских символов и/или цифр\r")
    private String login;

    @NotNull (message="задайте пароль (3…128 символов)\r")
    @Length (min=3, max=128, message="длина пароля — [3…128] символов\r")
    private String password;

    @NotNull (message="укажите адрес электронной почты (до 64 символов)\r")
    @Length (min=5, max=64, message="длина адреса — до 64 символов\r")
    private String email;
}//1
