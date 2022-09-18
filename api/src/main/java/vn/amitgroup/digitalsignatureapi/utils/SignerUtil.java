package vn.amitgroup.digitalsignatureapi.utils;

import javax.servlet.http.HttpServletRequest;

public class SignerUtil {
    public static String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Signer");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.replace("Bearer ", "");
        }
        return null;

    }
}
