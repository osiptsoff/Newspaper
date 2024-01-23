package ru.spb.nicetu.newspaper.api.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.spb.nicetu.newspaper.api.model.User;

/**
 * <p>Data Transfer Object for {@code User}.</p>
 *
 * <p>Contains information about user: login, name, last name and roles.</p>
 * <p>This class also provides static method for generating DTO from {@code User} instance</p>
    * @author Nikita Osiptsov
    * @see {@link User}
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class UserInfoDto {
    private String login;
    private String name;
    private String lastName;
    private Set<SingleValueDto<String>> roles;

    public static UserInfoDto from(User user) {
        UserInfoDto result = new UserInfoDto();
        
        result.setLogin(user.getLogin());
        result.setName(user.getName());
        result.setLastName(user.getLastName());
        result.setRoles(user.getRoles()
            .stream()
            .map( r -> new SingleValueDto<String>(r.getAuthority()) )
            .collect(Collectors.toSet()));

        return result;
    }
}
