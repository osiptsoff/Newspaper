package ru.osiptsoff.newspaper.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.osiptsoff.newspaper.api.model.auth.Token;

@Repository
public interface TokenRepository extends CrudRepository<Token, Integer> {
    
}
