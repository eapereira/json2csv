package com.xlib.json2csv.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

/**
 * 
 * @author <a href="eap.eapereira@gmail.com">Edson Alves Pereira</a>
 *
 */
@Configuration
public class JSon2CSVServerConfiguration {

	/**
	 * {@link ViewResolver} Para fazer o spring converter os objetos do projeto em <code>REST</code>;
	 * 
	 * @return
	 */
	@Bean
	public ViewResolver viewResolver(ContentNegotiationManager contentNegotiationManager) {
		ContentNegotiatingViewResolver viewResolver = new ContentNegotiatingViewResolver();

		viewResolver.setContentNegotiationManager(contentNegotiationManager);

		return viewResolver;
	}
}
