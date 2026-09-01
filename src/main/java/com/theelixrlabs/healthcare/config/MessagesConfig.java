package com.theelixrlabs.healthcare.config;

import com.theelixrlabs.healthcare.constants.PatientConstants;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/***
 * Read the messages from message.properties file in the resource directory
 */
@Configuration
public class MessagesConfig {
    /**
     * This method is responsible for managing the messages from a properties file
     *
     * @return message source object
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename(PatientConstants.MESSAGE_RESOURCE_CLASSPATH_NAME);
        return messageSource;
    }

    /**
     * Responsible for managing custom validation messages
     *
     * @return LocalValidatorFactory object
     */
    @Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.setValidationMessageSource(messageSource());
        return localValidatorFactoryBean;
    }
}
