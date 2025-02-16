package org.example.mhcommon.data.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Social {
    private String type;
    private String src;
    private String icon;
}
