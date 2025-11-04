package com.example.demo.mappers;

import com.example.demo.dtos.RegisterUserRequest;
import com.example.demo.dtos.UpdateUserRequest;
import com.example.demo.dtos.UserDto;
import com.example.demo.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(RegisterUserRequest request);
    void update(UpdateUserRequest request, @MappingTarget User user);
}
