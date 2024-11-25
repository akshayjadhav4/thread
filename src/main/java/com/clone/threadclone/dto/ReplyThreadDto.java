package com.clone.threadclone.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ReplyThreadDto extends ReplyDto {

    private ThreadDto thread;
}
