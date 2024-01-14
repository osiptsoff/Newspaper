package ru.osiptsoff.newspaper.api.dto;

import java.util.Set;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.osiptsoff.newspaper.api.model.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {
    private String name;
    private Set<SingleValueDto<String>> roles;

    public static UserInfoDto from(User user) {
        UserInfoDto result = new UserInfoDto();
        
        result.setName(user.getLogin());
        result.setRoles(user.
                            getRoles()
                            .stream()
                            .map( r -> new SingleValueDto<String>(r.getAuthority()) )
                            .collect(Collectors.toSet())
                         );

        return result;
    }
}