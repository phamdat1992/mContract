package vn.amitgroup.digitalsignatureapi.dto.response;


public enum ERROR {
    SUCCESS(1, "Thành công"),
    EXIST(2, "Đã tồn tại"),
    NOTEXIST(3, "Không tồn tại"),
    VALIDATEFAIL(4, "Sai xác thực"),
    ALREADYEXIST(5, "Đã sớm tồn tại"),
    RESULTNOTFOUND(6,"Dữ liệu không tồn tại"),
    S3UPLOADFAIL(7, "Up load không thành công"),
    S3PresignedUrl(8, "Lỗi lấy url"),
    ACCOUNTALREADYEXIST(9,"Tài khoản đã tồn tại"),
    ACCOUNTNOTFOUND(10,"Tài khoản chưa đăng kí"),
    INVALIDDATA(10,"Dữ liệu không hợp lệ"),
    CERTIFICATEEXPIRED(11,"Chữ kí hết hạn"),
    CERTIFICATENOTFOUND(12,"Không thuộc Việt Nam root CA"),
    CONVERTFAIL(13,"Lỗi chuyển đổi file"),
    ISSIGNING(14,"Hợp đồng đang kí bởi người khác"),
    FILECHANGED(15,"File đã cập nhật trước đó"),
    EXPIRED(16,"Hợp đồng đã hết hạn"),
    TAXCODEINVALID(17,"Sai mã số thuế"),
    CANNOTCANCEL(18,"Không thể hủy hợp đồng"),
    CANNOTSIGN(19,"Không thể kí hợp đồng")
    ;
    private int code;
    private String message;
    ERROR(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
