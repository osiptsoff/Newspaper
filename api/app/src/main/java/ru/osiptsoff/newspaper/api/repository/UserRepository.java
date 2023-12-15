package ru.osiptsoff.newspaper.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.osiptsoff.newspaper.api.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    
}
