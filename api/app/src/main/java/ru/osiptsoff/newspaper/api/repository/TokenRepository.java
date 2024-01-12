package ru.osiptsoff.newspaper.api.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.osiptsoff.newspaper.api.model.auth.Token;

@Repository
public interface TokenRepository extends CrudRepository<Token, Integer> {
    Long deleteByValue(String value);
    Optional<Token> findByValue(String value);
    Boolean existsByValue(String value);
}
