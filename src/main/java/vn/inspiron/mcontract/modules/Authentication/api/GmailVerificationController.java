package vn.inspiron.mcontract.modules.Authentication.api;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.inspiron.mcontract.modules.Authentication.component.GoogleUtils;
import vn.inspiron.mcontract.modules.Authentication.dto.GooglePojoDTO;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class GmailVerificationController {

    @Autowired
    private GoogleUtils googleUtils;

    @RequestMapping("/login-google")
    public void loginGoogle(HttpServletRequest request) throws ClientProtocolException, IOException {
        String code = request.getParameter("code");
        String accessToken = googleUtils.getToken(code);
        googleUtils.getUserInfo(accessToken);
    }
}
