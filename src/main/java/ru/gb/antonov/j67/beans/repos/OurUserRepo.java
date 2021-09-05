package ru.gb.antonov.j67.beans.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.gb.antonov.j67.entities.OurUser;

import java.util.Optional;

@Repository
public interface OurUserRepo extends CrudRepository <OurUser, Long>
{
    Optional<OurUser> findByLogin (String login);
}
