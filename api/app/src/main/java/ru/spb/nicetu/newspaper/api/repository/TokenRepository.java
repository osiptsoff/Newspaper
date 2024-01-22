package ru.spb.nicetu.newspaper.api.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.spb.nicetu.newspaper.api.model.auth.Token;

@Repository
public interface TokenRepository extends CrudRepository<Token, Long> {
    Long deleteByValue(String value);
    Optional<Token> findByValue(String value);
    Boolean existsByValue(String value);
}
