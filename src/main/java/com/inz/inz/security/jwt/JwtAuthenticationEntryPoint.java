package com.inz.inz.security.jwt;

import com.inz.inz.exceptionhandler.ErrorSpecifcation;
import com.inz.inz.exceptionhandler.ServerModelException;
import org.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -8970718410437077606L;
    private  static final String  MEDIA_TYPE = "application/json";
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setStatus(401);
        ServerModelException serverModelException=new ServerModelException();
        serverModelException.setCode(ErrorSpecifcation.UNAUTHORIZED.getCode());
        serverModelException.setDetails(ErrorSpecifcation.UNAUTHORIZED.getDetails());
        serverModelException.setFields(new ArrayList<>());


        JSONObject jsonObject=new JSONObject(serverModelException);

        response.setContentType(MEDIA_TYPE);

        response.getWriter().write(jsonObject.toString());


    }
}