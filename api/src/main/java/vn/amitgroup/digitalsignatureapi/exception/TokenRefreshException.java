package vn.amitgroup.digitalsignatureapi.exception;

public class TokenRefreshException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  
    public TokenRefreshException(String token) {
      super(String.format("Failed for [%s]: %s", token, "Refresh token is not in database!"));
    }
}
