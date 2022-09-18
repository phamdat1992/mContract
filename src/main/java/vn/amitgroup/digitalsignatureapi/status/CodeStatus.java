package vn.amitgroup.digitalsignatureapi.status;

public enum CodeStatus {
    SUCCESS(1, "success"), 
    FAIL(0,"fail");
    private Integer code;
    private String message;

    CodeStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
