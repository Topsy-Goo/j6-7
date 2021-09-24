package ru.gb.antonov.j67.beans.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gb.antonov.j67.beans.repos.OurUserRepo;
import ru.gb.antonov.j67.entities.OurUser;
import ru.gb.antonov.j67.entities.Role;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OurUserDetailsService implements UserDetailsService
{
    private final OurUserRepo ourUserRepo;
    private final RoleService roleService;
//-------------------------------------------------------------------

    public Optional<OurUser> findByLogin (String login)
    {
        return ourUserRepo.findByLogin (login);
    }

    @Transactional           //AuthController,
    @Override
    public UserDetails loadUserByUsername (String login) /* throws UsernameNotFoundException - непонятно, зачем его пробрасывать, если оно наследуется от RuntimeException   */
    {
        String errMsg = String.format ("Логин (%s) не зарегистрирован.", login);
        OurUser ourUser = findByLogin(login)
                            .orElseThrow(()->new UsernameNotFoundException (errMsg));
                            //^ должно отправлять err.401 клиенту, но не отправляет, а пишет
                            // в консоль IDE. Регистрация в GlobalExceptionHandler не помогла.
                            // В общем, зарегистрированный юзер при перезапуске приложения не
                            // вызывает 401, а вызывает обычное исключение.
                            // И JwtRequestFilter.doFilterInternal ругается.

        //Заполняем и возвращаем спринговую версию юзера (у него есть ещё более подробный
        // конструктор)
        return new User(ourUser.getLogin(),
                        ourUser.getPassword(),
                        mapRolesToAuthorities (ourUser.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities (Collection<Role> roles)
    {
        return roles.stream()
                    .map (role->new SimpleGrantedAuthority(role.getName()))
                    .collect (Collectors.toList());
    }

    @Transactional          //AuthController,
    public Optional<OurUser> createNewOurUser (String login, String password, String email)
    {
        OurUser dummyUser = OurUser.dummyOurUser (login, password, email);
        Optional<Role> optionalRole = roleService.getRoleUser();

        if (optionalRole.isPresent())
        {
            OurUser saved = ourUserRepo.save (dummyUser); //< is always 'not null' when returned
            saved.addRole (optionalRole.get()); //< как оказалось, создать для нового юзера
            // запись в сводной таблице можно просто добавив сущность-роль в список ролей юзера
            return Optional.of (saved);
        }
        return Optional.empty();
    }
}
