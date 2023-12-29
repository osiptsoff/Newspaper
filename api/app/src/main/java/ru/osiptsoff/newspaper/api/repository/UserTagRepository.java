package ru.osiptsoff.newspaper.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.osiptsoff.newspaper.api.model.UserTag;
import ru.osiptsoff.newspaper.api.model.embeddable.UserTagId;

@Repository
public interface UserTagRepository extends CrudRepository<UserTag, UserTagId>{
    
}
