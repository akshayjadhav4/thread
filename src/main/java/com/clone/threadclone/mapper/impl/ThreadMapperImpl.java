package com.clone.threadclone.mapper.impl;

import com.clone.threadclone.service.RedisLikeService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.clone.threadclone.dto.ThreadDto;
import com.clone.threadclone.mapper.Mapper;
import com.clone.threadclone.model.Thread;

@Component
public class ThreadMapperImpl implements Mapper<Thread, ThreadDto> {

    private final ModelMapper modelMapper;
    private final RedisLikeService redisLikeService;

    public ThreadMapperImpl(ModelMapper modelMapper, RedisLikeService redisLikeService) {
        this.modelMapper = modelMapper;
        this.redisLikeService = redisLikeService;
    }

    @Override
    public Thread mapFrom(ThreadDto threadDto) {
        return modelMapper.map(threadDto, Thread.class);

    }

    @Override
    public ThreadDto mapTo(Thread threadEntity) {
        ThreadDto dto = modelMapper.map(threadEntity, ThreadDto.class);
        int likeCount = redisLikeService.getThreadLikesCount(dto.getId());
        dto.setLikeCount(likeCount);
        return  dto;
    }

}
