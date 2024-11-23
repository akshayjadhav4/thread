package com.clone.threadclone.request;

import java.util.List;

import com.clone.threadclone.model.Media.MediaType;

import lombok.Data;

@Data
public class UpdateThreadRequest {

    private String content;

    @Data
    public static class MediaData {
        private String url;
        private MediaType type;
    }

    private List<MediaData> mediaToAdd;

    List<Long> mediaToRemove;
}
