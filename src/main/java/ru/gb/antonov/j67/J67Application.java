package ru.gb.antonov.j67;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication (scanBasePackages = "ru.gb.antonov.j67.beans")
@EnableAspectJAutoProxy //< для AOP
public class J67Application
{

    public static void main (String[] args)
    {
        SpringApplication.run (J67Application.class, args);
    }
}
