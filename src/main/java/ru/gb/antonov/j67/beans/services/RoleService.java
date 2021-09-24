package ru.gb.antonov.j67.beans.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.antonov.j67.beans.repos.RoleRepo;
import ru.gb.antonov.j67.entities.Role;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService
{
    private final RoleRepo roleRepo;

    public Optional<Role> findByName (String roleName)
    {
        if (roleName == null || roleName.trim ().isEmpty ())
        {
            return Optional.empty ();
        }
        return Optional.of (roleRepo.findByName (roleName));
    }

    public Optional<Role> getRoleUser()
    {
        return findByName ("ROLE_USER");
    }
//1
}
