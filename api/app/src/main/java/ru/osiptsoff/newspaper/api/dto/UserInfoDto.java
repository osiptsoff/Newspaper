package ru.osiptsoff.newspaper.api.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.osiptsoff.newspaper.api.model.User;

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
        result.setRoles(user.
                            getRoles()
                            .stream()
                            .map( r -> new SingleValueDto<String>(r.getAuthority()) )
                            .collect(Collectors.toSet())
                         );

        return result;
    }
}
