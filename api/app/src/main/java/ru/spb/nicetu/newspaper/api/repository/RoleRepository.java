package ru.spb.nicetu.newspaper.api.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.spb.nicetu.newspaper.api.model.auth.Role;

/**
 * <p>{@code Role} repository.</p>
    * @author Nikita Osiptsov
    * @see {@link Role}
 * @since 1.0
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByName(String name);
    
}
