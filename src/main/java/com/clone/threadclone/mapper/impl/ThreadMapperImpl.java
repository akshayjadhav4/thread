package com.clone.threadclone.mapper.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.clone.threadclone.dto.ThreadDto;
import com.clone.threadclone.mapper.Mapper;
import com.clone.threadclone.model.Thread;

@Component
public class ThreadMapperImpl implements Mapper<Thread, ThreadDto> {

    private ModelMapper modelMapper;

    public ThreadMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Thread mapFrom(ThreadDto threadDto) {
        return modelMapper.map(threadDto, Thread.class);

    }

    @Override
    public ThreadDto mapTo(Thread threadEntity) {
        return modelMapper.map(threadEntity, ThreadDto.class);
    }

}
