package com.clone.threadclone.mapper.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.clone.threadclone.dto.ReplyDto;
import com.clone.threadclone.dto.ReplyThreadDto;

import com.clone.threadclone.model.Reply;

@Component
public class ReplyMapper {

    private ModelMapper modelMapper;

    public ReplyMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ReplyDto mapTo(Reply replyEntity, boolean includeThreadDetails) {
        ReplyDto dto;
        if (includeThreadDetails) {
            dto = modelMapper.map(replyEntity, ReplyThreadDto.class);
        } else {

            dto = modelMapper.map(replyEntity, ReplyDto.class);
        }
        int likeCount = replyEntity.getLikes() != null ? replyEntity.getLikes().size() : 0;
        dto.setLikeCount(likeCount);
        return dto;
    }

    public Reply mapFrom(ReplyDto replyDto) {
        return modelMapper.map(replyDto, Reply.class);
    }
}
