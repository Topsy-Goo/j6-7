package ru.gb.antonov.j67.beans.configs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.gb.antonov.j67.beans.services.OurUserDetailsService;

@EnableWebSecurity  //< «включатель» правил безопасности, описанных в нижеописанном классе
@RequiredArgsConstructor
@Slf4j  //< логгирование
/*@EnableGlobalMethodSecurity //< включает защиту на уровне методов
    (prePostEnabled = true,  //< (не)разрешает исп-ние @PreAuthorize и @PostAuthorize
     securedEnabled = true,  //< (не)разрешает исп-ние @Secured
     jsr250Enabled = true)   //< (не)разрешает исп-ние @Role(s)Allowed*/
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    private final OurUserDetailsService ourUserDetailsService;
    private final JwtRequestFilter      jwtRequestFilter;

//Комментарий (2)
    //  более гибкий вариант:
    //@PreAuthorize ("hasRole('ROLE_ADMIN') and hasRole('ROLE_USER')") == нужны обе роли
    //@PreAuthorize ("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')") == хотябы одна из ролей
    //@PreAuthorize ("!hasRole('ROLE_GUEST') and hasRole('ROLE_ADMIN')") == админ и НЕ гость
    //
    //  менее гибкий вариант (кажется, основан на static-правилах):
    //@Secured ({"ROLE_USER", "ROLE_ADMIN") == хотябы одна из ролей
    //
    //  @PostAuthorize - кажется, это отложенная авторизация (метод выполнится, но результаты будут доступны только после авторизации)
    //  также см. https://www.baeldung.com/spring-security-method-security

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean () throws Exception
    {
        return super.authenticationManagerBean();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder()  {   return new BCryptPasswordEncoder();   }
    //NoOpPasswordEncoder (Deprecated) не кодирует пароль.

    @Override
    protected void configure (HttpSecurity httpSec) throws Exception
    {
        httpSec
        //что-то запрещаем:
               .csrf().disable()
               .authorizeRequests()
        //разрешаем доступ к консоли (в учебных целях):
               .antMatchers ("/h2_console/**").permitAll()
        //сообщаем, что работать нужно в STATELESS-режиме (без сессий авторизации, с токенами в каждом запросе):
               .and()
               .sessionManagement().sessionCreationPolicy (SessionCreationPolicy.STATELESS)
        //ещё что-то запретили:
               .and()
               .headers().frameOptions().disable()
        //при попытке войти в запретную зону клиент получит сообщение UNAUTHORIZED:
               .and()
               .exceptionHandling()
               .authenticationEntryPoint (new HttpStatusEntryPoint (HttpStatus.UNAUTHORIZED))
               ;
        //Вот здесь мы вставляем наш фильтр перед фильтром, проверяющим валидность юзера:
        httpSec.addFilterBefore (jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

/*      //Это используется при контроле доступа основанном на JSID.
        //Мы можем защищать отдельные группы страниц:
               .antMatchers("/auth_page/**").authenticated()    //< для авторизованных юзеров (ROLE_USER+)
               .antMatchers("/user_info").authenticated()       //< нфо о юзере (ROLE_USER+)
               .antMatchers("/admin/**").hasAnyRole("ADMIN", "SUPERADMIN") //< админка (ROLE_ADMIN+)
               .anyRequest().permitAll()    //< все остальные запросы доступны всем включая гостей
            //создание формы авторизации при авторизации:
               .and()
               .formLogin() //< метод отдаёт страницу; в Rest'е его не будет, т.к. там нам не нужна посторонняя страница, там обмениваются JSON'ами.
            //настройка действий для выхода пользователя из учётки:
               .and()
               .logout()
               .invalidateHttpSession(true)    //< true == закрыть сессию при выходе пользователя из системы
               .deleteCookies("JSESSIONID")    //< стираем JSID
            //Настройка управления сессиями:
               .and()
               .sessionManagement()
               .maximumSessions(1)  //< количество одновременных сессий для данного юзера
               .maxSessionsPreventsLogin (true)  //< отказ в регистрации, если кол-во сессий превысило указанный выше предел. (Умолчальное значение: false. Кажется, оно означает, что при каждой дополнительной авторизации сверх установленной выше нормы, одна из открытых ранее сессий будет закрываться. Похоже на принцип вытеснения, при котором предпочтение отдаётся новым сессиям.)
               ;*/
    }

/*  //Это используется при контроле доступа основанном на JSID.
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider()
    {
        DaoAuthenticationProvider dap = new DaoAuthenticationProvider();
        dap.setPasswordEncoder (passwordEncoder()); //< раз мы не можем расшифровать пароль, взятый из базы, то зашифруем присланный юзером пароль и сравним две абракадабры.
        dap.setUserDetailsService (ourUserDetailsService); //< дадим Auth-Provider'у инструмент для извлечения данных пользователя из базы.
        return dap;
    }*/
}
