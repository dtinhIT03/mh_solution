package org.example.mhcommon.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.experimental.Accessors;
import org.example.mhcommon.data.model.paging.Order;

import java.util.List;

import static org.example.mhcommon.data.model.paging.Pageable.*;


@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SearchRequest {
    private int page;
    private int pageSize = 10;
    private String keyword;
    private List<Order> sorts;
    private List<Filter> filters;

    @JsonIgnore
    public Integer getOffset() {
        return Math.max((page - 1) * pageSize, 0);
    }

    public int getPageSize() {
        if (this.pageSize < 0) return MAXIMUM_PAGE_SIZE;
        return pageSize;
    }
}
