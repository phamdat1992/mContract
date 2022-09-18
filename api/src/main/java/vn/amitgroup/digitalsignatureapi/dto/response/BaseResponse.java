package vn.amitgroup.digitalsignatureapi.dto.response;

import lombok.Data;

@Data
public class BaseResponse <T> {
    private int pageIndex;
    private int totalPage;
    private String type;
    private long total;
    private long totalUnread;
    private int statusCode;
    private T data;
    private String errorCode;
    private String errors;
    private String message;

    public BaseResponse() {
        this.statusCode=200;
        this.message=ERROR.SUCCESS.getMessage();
    }
    public BaseResponse(String message ) {
        this.statusCode=200;
        this.message= message;
    }
    public BaseResponse(T data,int statusCode) {
        this.data=data;
        this.statusCode=statusCode;
    }
    public BaseResponse(String errors,String message,int statusCode) {
        this.errors=errors;
        this.message= message;
        this.statusCode= statusCode;
    }
    public BaseResponse(String errors,String message,int statusCode,String errorCode) {
        this.errors=errors;
        this.message= message;
        this.statusCode= statusCode;
        this.errorCode = errorCode;
    }

}
