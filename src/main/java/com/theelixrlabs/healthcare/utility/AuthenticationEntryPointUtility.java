package com.theelixrlabs.healthcare.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theelixrlabs.healthcare.constants.MessageConstants;
import com.theelixrlabs.healthcare.response.FailureResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Collections;

/**
 * Class to configure custom authentication configurations at entry point
 */
@Component
public class AuthenticationEntryPointUtility implements AuthenticationEntryPoint {

    private final MessageUtil messageUtil;

    public AuthenticationEntryPointUtility(MessageUtil messageUtil) {
        this.messageUtil = messageUtil;
    }

    /**
     * Overridden commence method to set custom message if authentication fails
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        FailureResponse customErrorResponse =
                new FailureResponse(false, Collections.singletonList(messageUtil.getMessage(MessageConstants.NOT_AUTHORISED)));
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(customErrorResponse));
    }
}
