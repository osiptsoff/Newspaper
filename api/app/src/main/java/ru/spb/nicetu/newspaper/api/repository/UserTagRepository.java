package ru.spb.nicetu.newspaper.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.spb.nicetu.newspaper.api.model.UserTag;
import ru.spb.nicetu.newspaper.api.model.embeddable.UserTagId;

@Repository
public interface UserTagRepository extends CrudRepository<UserTag, UserTagId>{
    
}
