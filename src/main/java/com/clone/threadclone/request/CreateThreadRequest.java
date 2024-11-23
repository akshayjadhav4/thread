package com.clone.threadclone.request;

import java.util.List;

import com.clone.threadclone.model.Media.MediaType;

import lombok.Data;

@Data
public class CreateThreadRequest {
    private String content;

    @Data
    public static class MediaData {
        private String url;
        private MediaType type;
    }

    private List<MediaData> media;
}
