package com.example.shopapp.mappers;

import com.example.shopapp.models.dto.UserDto;
import com.example.shopapp.models.entity.Role;
import com.example.shopapp.models.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;


@Mapper(componentModel = "spring")
public interface UserMapper {


    @Mapping(source = "password", target = "password", ignore = true)
    @Mapping(source = "roles", target = "roles", ignore = true)
    User toEntity(UserDto userDto);
    @Mapping(source = "roles", target = "roles", qualifiedByName = "roleListToStringList")
    @Mapping(source = "password", target = "password", ignore = true)
    UserDto toDto(User user);

    @Named("roleListToStringList")
    static List<String> roleListToStringList(List<Role> roleList) {
        return roleList.stream()
                .map(Role::getName)
                .toList();
    }
}
