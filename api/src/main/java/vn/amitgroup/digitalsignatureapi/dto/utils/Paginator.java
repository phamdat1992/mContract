package vn.amitgroup.digitalsignatureapi.dto.utils;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

@Data
public class Paginator {
    public static Pageable GetPageable(
            @RequestParam(value = "currentPage", defaultValue = "0") Integer currentPage,
            @RequestParam(value = "size", defaultValue = "0") Integer size ){
        Pageable paging = null;
        if (size == 0) {
            paging = PageRequest.of(currentPage, Integer.MAX_VALUE);
        } else {
            paging = PageRequest.of(currentPage, size);
        }
        return paging;
    }

}
