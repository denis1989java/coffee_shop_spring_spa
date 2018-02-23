package ru.mail.system.configs.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import ru.mail.system.service.model.CustomResponseEntity;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Denis Monich
 * success logout handler, sending response to UI with code 200
 */
@Component("customLogoutSuccessHandler")
public class CustomLogoutSuccessHandler extends
        SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {

    private static final Logger logger = Logger.getLogger(CustomLogoutSuccessHandler.class);


    @Override
    public void onLogoutSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        logger.debug("logout success handler");

        CustomResponseEntity customResponseEntity = new CustomResponseEntity();
        customResponseEntity.setMessage("success logout");

        response.getWriter().write(new ObjectMapper().writeValueAsString(customResponseEntity));
        response.setStatus(200);
    }
}
