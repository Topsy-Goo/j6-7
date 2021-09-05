package ru.gb.antonov.j67.beans.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.gb.antonov.j67.entities.Role;

@Repository
public interface RoleRepo extends CrudRepository<Role, Integer>
{
    Role findByName (String name);
}//1
