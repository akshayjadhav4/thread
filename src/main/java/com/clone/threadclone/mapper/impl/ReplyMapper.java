package com.clone.threadclone.mapper.impl;

import com.clone.threadclone.service.RedisLikeService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.clone.threadclone.dto.ReplyDto;
import com.clone.threadclone.dto.ReplyThreadDto;

import com.clone.threadclone.model.Reply;

@Component
public class ReplyMapper {

    private final ModelMapper modelMapper;
    private final RedisLikeService redisLikeService;

    public ReplyMapper(ModelMapper modelMapper, RedisLikeService redisLikeService) {
        this.modelMapper = modelMapper;
        this.redisLikeService = redisLikeService;
    }

    public ReplyDto mapTo(Reply replyEntity, boolean includeThreadDetails) {
        ReplyDto dto;
        if (includeThreadDetails) {
            dto = modelMapper.map(replyEntity, ReplyThreadDto.class);
        } else {

            dto = modelMapper.map(replyEntity, ReplyDto.class);
        }
        int likeCount = redisLikeService.getReplyLikesCount(dto.getId());
        dto.setLikeCount(likeCount);
        return dto;
    }

    public Reply mapFrom(ReplyDto replyDto) {
        return modelMapper.map(replyDto, Reply.class);
    }
}
