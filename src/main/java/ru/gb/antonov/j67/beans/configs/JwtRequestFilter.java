package ru.gb.antonov.j67.beans.configs;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.gb.antonov.j67.beans.services.OurUserDetailsService;
import ru.gb.antonov.j67.beans.utils.JwtokenUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter
{
    private final JwtokenUtil jwtokenUtil;
    private final OurUserDetailsService ourUserDetailsService;

/*  Этот класс, кажется, служит для того, чтобы не давать стандартным фильтрам получать
    запросы клиентов «как есть». Т.е. мы тут сами разбираем токены клиентов, а стандартным
    фильтрам скармливаем только ту информацию, которую они могут переварить. Подробности
    как-то забылись, а пересматривать двухчасовое видео желание пока не появилось. Методички
    к этому уроку нет.

    Наш фильтр вставялется в начало цепочки фильтров в самом конце SecurityConfig.configure().

    (При использовании JSID токен UsernamePasswordAuthenticationToken создаётся фильтром
    UsernamePasswordAuthenticationFilter при получении POST-запроса от клиента. Подробности —
    в схеме к уроку 11.)
*/
    @Override
    protected void doFilterInternal (HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain) throws ServletException, IOException
    {   String login = null;
        String jwt   = null;
        String prefixBearer = "Bearer ";

    //Ищем в запросе заголовок «Authorization», убеждаемся, что содержимое заголовка начинается
    // с префикса «Bearer », и извлекаем из него логин:
        String authHeader = request.getHeader ("Authorization");

        if (authHeader != null && authHeader.startsWith (prefixBearer))
        {
            jwt = authHeader.substring (prefixBearer.length ());
            try
            {   login = jwtokenUtil.getLoginFromToken (jwt);
            }
            catch (ExpiredJwtException e)
            {
                log.debug ("The token is expired");
            }
        }
    //Вычисляем токен для СпрингСекюрити-контекста:
        if (login != null && SecurityContextHolder.getContext ().getAuthentication () == null)
        {
    /*  Путь первый — помещаем юзера в СпрингСекюрити-контекст на каждом приходящем запросе!
    Проверяется только подпись токена. Работает быстро. Отрицательный момент — информация
    о юзере не сравнивается с базой.: */

            //UsernamePasswordAuthenticationToken token = trustYourUser (login, jwt);

    /*  Путь второй — похож на первый, но токен сверяется с информацией в БД.
    Очевидный минус — с каждым пришедшим запросом приходится запрашивать БД. Поскольку наша БД
    находится в памяти, идём вторым путём.:    */

            UsernamePasswordAuthenticationToken token = trustDatabaseOnly (login, jwt, request);

    //Помещаем вычисленный токен в СпрингСекюрити-контекст:
            SecurityContextHolder.getContext ().setAuthentication (token);
        }
        filterChain.doFilter (request, response);
    }

    private UsernamePasswordAuthenticationToken trustYourUser (String login, String jwt)
    {
        Collection<GrantedAuthority> gaCollection =
            jwtokenUtil.getRoles (jwt)
                       .stream()
                       .map (SimpleGrantedAuthority::new)
                       .collect (Collectors.toList ());

        //На основании инф-ции из присланного jwt формируем токен для СпрингСекюрити-контекста:
        UsernamePasswordAuthenticationToken token =
            new UsernamePasswordAuthenticationToken (login, null, gaCollection);

        return token;
    }

    private UsernamePasswordAuthenticationToken trustDatabaseOnly (
                                        String login, String jwt, HttpServletRequest request)
    {
        UserDetails userDetails = ourUserDetailsService.loadUserByUsername (login);

        //формируем токен для СпрингСекюрити-контекста на основании инф-ции, полученной из базы:
        UsernamePasswordAuthenticationToken token =
            new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities());

        token.setDetails (new WebAuthenticationDetailsSource().buildDetails (request));
        return token;
    }
}
