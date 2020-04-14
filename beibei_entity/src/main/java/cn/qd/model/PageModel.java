package cn.qd.model;

import java.io.Serializable;
import java.util.List;

public class PageModel<T> implements Serializable {

    private Long total;
    private List<T> rows;

    public PageModel(Long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
