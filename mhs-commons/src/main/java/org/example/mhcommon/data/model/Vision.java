package org.example.mhcommon.data.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Vision {
    private String title;
    private String content;
    private String src;
}