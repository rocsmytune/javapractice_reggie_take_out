package com.project.reggie.config;//configuration class

import com.project.reggie.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    /***
     * Set up static resource mapping
     * @param registry
     */

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("$$$  Start static resources mapping...  $$$");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    /***
     * extend original msg converter
     * @param converters
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("$$$  Extend Msg Converter...   $$$");
        //create new converter object
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //set converter using Jackson
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //add the converter above to mvc framework
        converters.add(0, messageConverter);//优先使用：0
    }
}
