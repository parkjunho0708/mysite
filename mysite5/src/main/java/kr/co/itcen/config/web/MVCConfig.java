package kr.co.itcen.config.web;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

/*
 * 
 *  1. View Resolver
 *  2. Default Servlet Handler
 *  3. Message Converter
 *  
 */
@Configuration
@EnableWebMvc
public class MVCConfig extends WebMvcConfigurerAdapter {

	// View Resolver
	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		resolver.setExposeContextBeansAsAttributes(true);
				
		return resolver;
	}

	// Default Servlet Handler
	// <!-- 서블릿 컨테이너의 디폴트 서블릿 위임 핸들러 -->
	// <mvc:default-servlet-handler />
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	// Message Converter
	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		// 1. Builder를 만들었으니,
		Jackson2ObjectMapperBuilder builder = 
				new Jackson2ObjectMapperBuilder()
				.indentOutput(true) // JSON 문자열 앞에 indent가 붙음 (문자열을 예쁘게 출력해줌)
				.dateFormat(new SimpleDateFormat("yyyy-MM-dd"))
				.modulesToInstall(new ParameterNamesModule());

		// 2. Converter를 만들 수 있다.
		MappingJackson2HttpMessageConverter converter = 
				new MappingJackson2HttpMessageConverter(builder.build()); // Converter에 위에서 만들어준 builder를 넣어준다.

		// 3. Message Converter 설정
		// dispatcher-sevlet.xml에서는 이러한 형태로 들어감
//		<bean class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter">
//			<property name="supportedMediaTypes">
//				<list>
//					<value>application/json; charset=UTF-8</value>
//				</list>
//			</property>
//		</bean>
		// MediaType은 지속적으로 추가 가능
		// [example] new MediaType("application", "json", Charset.forName("UTF-8"))
		converter.setSupportedMediaTypes(
				Arrays.asList(
						new MediaType("application", "json", Charset.forName("UTF-8"))
						)
				);

		return converter;
	}

	// StringHttpMessage Converter
//	<bean class="org.springframework.http.converter.StringHttpMessageConverter">
//	<property name="supportedMediaTypes">
//		<list>
//			<value>text/html; charset=UTF-8</value>
//		</list>
//	</property>
//	</bean>
	@Bean
	public StringHttpMessageConverter stringHttpMessageConverter() {
		StringHttpMessageConverter converter = new StringHttpMessageConverter();
		converter.setSupportedMediaTypes(
				Arrays.asList(
						new MediaType("text", "html", Charset.forName("UTF-8"))));
		return converter;
	}

	// Message Converter를 생성하고 지속적으로 ADD!! (추가!!)
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(mappingJackson2HttpMessageConverter());
		converters.add(stringHttpMessageConverter());
	}

}
