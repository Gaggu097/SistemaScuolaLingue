package org.gagandeepsuman.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC configuration class.
 * Configures view controllers and other web-related settings.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Adds view controllers for direct URL-to-view mapping without needing a controller.
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // These are optional - we can add direct mappings if needed
        // For now, we'll rely on our WebController for menu navigation
    }
}