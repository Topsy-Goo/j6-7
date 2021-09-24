package ru.gb.antonov.j67.entities;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.gb.antonov.j67.beans.errorhandlers.UserCreatingException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

@Entity
//@Data
@Table(name="ourusers")
public class OurUser
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name="id")
    private Long id;

    @Column(name="login", nullable=false, unique=true)
    private String login;

    @Column(name="password", nullable=false)
    private String password;

    @Column(name="email", nullable=false, unique=true)
    private String email;

    @ManyToMany
    @JoinTable (name="ourusers_roles",
                joinColumns        = @JoinColumn (name="user_id"),
                inverseJoinColumns = @JoinColumn (name="role_id"))
    private Collection<Role> roles;

    @CreationTimestamp
    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Column(name="updated_at", nullable=false)
    private LocalDateTime updatedAt;


    protected OurUser(){}

//Здесь мы создаём не сущность, а шаблон для добавления юзера в базу. А вот из БД мы получаем настоящую сущность.
    public static OurUser dummyOurUser (String login, String password, String email)
    {
        OurUser u = new OurUser();
        if (!u.setLogin (login) || !u.setPass (password) || !u.setEmail (email))
        {
            throw new UserCreatingException (String.format (
                "Недопустимый набор значений:\r    логин = %s,\r    пароль = %s,\r    почта = %s\r",
                login, password, email));
        }
        u.roles = new HashSet<>(); /*< без этого не работает… Хотя, казалось бы, что мешает при сохранении
        проверить и инициализировать подходящим Set'ом, чтобы простому юзеру не ломать голову, какая
        колллекция в это время года используется Спрингом для хранения связей?   */
        return u;
    }
//--------------------- Другие методы ---------------------------------------

    public boolean addRole (Role role)  {   return (role != null) && roles.add (role);   }

    public static String  validateString (String s, int minLen, int maxLen)
    {
        if (s != null && minLen > 0 && minLen <= maxLen)
        {
            s = s.trim();
            int len = s.length();
            if (len >= minLen && len <= maxLen)
            {
                return s;
            }
        }
        return null;
    }

    public static boolean hasEmailFormat (String email)
    {
/*        boolean ok = false;
        int at = email.indexOf ('@');
        if (at > 0 && email.indexOf ('@', at +1) < 0)
        {
            int point = email.indexOf ('.', at);
            ok = point >= at +2;
        }
        return ok;*/return true;
    }
//----------------------- Геттеры и сеттеры -------------------------------------

    public Long getId() {   return id;   }
    private void setId (Long id) {   this.id = id;   }

    public String getLogin() {   return login;   }
    private boolean setLogin (String login)
    {
        String s = validateString (login, 3, 32);
        boolean ok = s != null;
        if (ok)
            this.login = s;
        return ok;
    }

    public String getPassword() {   return password;   }
    private void setPassword (String password) {   this.password = password;   }

/*  Отдельный метод для установки пароля вручную, чтобы иметь возможность сообщать юзеру о некорректно
заданном пароле и при этом выводить в сообщении пароль, а не хэш пароля.
*/
    private boolean setPass (String password)
    {
        String s = validateString (password, 3, 128);
        boolean ok = s != null;
        if (ok)
            setPassword (new BCryptPasswordEncoder().encode(s));
        return ok;
    }

    public String getEmail() {   return email;   }
    private boolean setEmail (String email)
    {
        String s = validateString (email, 5, 64);
        boolean ok = s != null && hasEmailFormat (email);
        if (ok)
            this.email = s;
        return ok;
    }

    public Collection<Role> getRoles() {   return roles;   }
    private void setRoles (Collection<Role> roles)    {   this.roles = roles;   }

    public LocalDateTime getCreatedAt() {   return createdAt;   }
    private void setCreatedAt (LocalDateTime ldt)
    {
        //Исходим из предположения, что за корректностью значений следит БД.
        this.createdAt = ldt;
    }

    public LocalDateTime getUpdatedAt() {   return updatedAt;   }
    private void setUpdatedAt (LocalDateTime ldt)
    {
        //Исходим из предположения, что за корректностью значений следит БД.
        this.updatedAt = ldt;
    }
}
