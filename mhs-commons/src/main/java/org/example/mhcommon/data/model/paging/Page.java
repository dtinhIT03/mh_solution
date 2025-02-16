package org.example.mhcommon.data.model.paging;

import org.example.mhcommon.data.model.SearchRequest;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class Page<T> {
    private String key; //khóa duy nhất cho trang
    private Long total;
    private Integer page;
    private List<T> items;
    private Boolean loadMoreAble; // check xem có thể tải thêm dữ liệu hay không
    private Boolean preLoadAble; //check xem có thể tải trước dữ liệu hay không

    public Page() {
    }

    public Page(Long total, Integer page, List<T> items) {
        this.page = page;
        this.total = total;
        this.items = items;
    }

    public Page(Long total, Pageable pageable, List<T> items) {
        this.total = total;
        this.page = pageable.getPage();
        this.items = items;
        this.loadMoreAble = total != null &&
                (total.intValue() > (pageable.getOffset() + pageable.getPageSize()));
    }

    public Page(Pageable pageable, List<T> items) {
        this.items = items;
        this.total = pageable.getTotal();
        this.page = pageable.getPage();
        this.loadMoreAble = pageable.getTotal() > (pageable.getOffset() + pageable.getPageSize());
    }

    public Page(Long total, SearchRequest searchRequest, List<T> items) {
        this.total = total;
        this.page = searchRequest.getPage();
        this.items = items;
        this.loadMoreAble = total != null &&
                (total.intValue() > (searchRequest.getOffset() + searchRequest.getPageSize()));
    }

}