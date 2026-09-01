package com.theelixrlabs.healthcare.utility;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageUtil {

    private final MessageSource messageSource;

    public MessageUtil(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String messageKey) {
       return messageSource.getMessage(messageKey,null,Locale.getDefault());
    }

    public String getMessage(String messageKey, Object[] dynamicArguments) {
        return messageSource.getMessage(messageKey,dynamicArguments, Locale.getDefault());
    }
}
