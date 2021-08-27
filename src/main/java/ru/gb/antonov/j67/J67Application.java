package ru.gb.antonov.j67;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication (scanBasePackages = "ru.gb.antonov.j67.beans")
public class J67Application
{

    public static void main (String[] args)
    {
        SpringApplication.run (J67Application.class, args);
    }//1
}
