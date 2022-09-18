package vn.amitgroup.digitalsignatureapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchContract {
    @Schema(required = true)
    private Integer userId;
    private String search;
    private String partner;
    private String topic;
    @Schema(description = "format yyyy-mm-dd")
    private String fromDate;
    @Schema(description = "format yyyy-mm-dd")
    private String toDate;
    @Schema(description = "there are 2 options CREATER , GUEST", 
    example = "GUEST")
    private String type;
    @Schema(description = "there are 6 options PROCESSING,WAITINGFORPARTNER,AUTHENTICATIONFAIL,COMPLETE,EXPIRED,CANCEL", 
    example = "COMPLETE")
    private String status;
    @Schema(description = "there are 2 options ASC , DESC , can use find new contranct", 
    example = "ASC")
    private String sortByDate;
    private Integer currentPage;
    private Integer size;
    
}
