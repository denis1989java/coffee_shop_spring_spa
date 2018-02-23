package ru.mail.system.configs.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import ru.mail.system.service.model.CustomResponseEntity;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Denis Monich
 * authentication failure handler, sending response to UI with code 200 and message: access denied
 */
@Component("authenticationFailureHandler")
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private static final Logger logger = Logger.getLogger(AuthenticationFailureHandler.class);


    /**
     * @param request   default parameter for override method "onAuthenticationFailure"
     * @param response  for creating and sending json object
     * @param exception default parameter for override method "onAuthenticationFailure"
     * @throws IOException      can be throw during reading the {@link Properties} file
     * @throws ServletException to indicate that servlet is permanently or temporarily unavailable.
     */
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException,
            javax.servlet.ServletException {

        logger.debug("Authentication is failure");

        CustomResponseEntity customResponseEntity = new CustomResponseEntity();
        customResponseEntity.setMessage("credentials is not valid");

        response.getWriter().write(new ObjectMapper().writeValueAsString(customResponseEntity));
        response.setStatus(200);
    }
}
