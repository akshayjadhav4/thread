package com.clone.threadclone.mapper.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.clone.threadclone.dto.UserDto;
import com.clone.threadclone.mapper.Mapper;
import com.clone.threadclone.model.User;

@Component
public class UserMapperImpl implements Mapper<User, UserDto> {

    private ModelMapper modelMapper;

    public UserMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDto mapTo(User userEntity) {
        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public User mapFrom(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

}
