package vn.amitgroup.digitalsignatureapi.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import vn.amitgroup.digitalsignatureapi.security.MyUserDetails;

public class UserUtil {
    public static String email(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null ? null : authentication.getName();
      }

    public static Integer userId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        // verify principal if your custom user details type.
        // if so, get the userid and do whatever.
        if(principal != null && principal instanceof MyUserDetails) {
            MyUserDetails userDetails = MyUserDetails.class.cast(principal);
            Integer userId = userDetails.getId();
            return userId;
        }
        return null;
    }
}
