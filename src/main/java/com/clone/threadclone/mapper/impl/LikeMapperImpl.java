package com.clone.threadclone.mapper.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.clone.threadclone.dto.LikeDto;
import com.clone.threadclone.mapper.Mapper;
import com.clone.threadclone.model.Like;

@Component
public class LikeMapperImpl implements Mapper<Like, LikeDto> {

    private ModelMapper modelMapper;

    public LikeMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public LikeDto mapTo(Like likeEntity) {
        return modelMapper.map(likeEntity, LikeDto.class);

    }

    @Override
    public Like mapFrom(LikeDto likeDto) {
        return modelMapper.map(likeDto, Like.class);

    }

}
