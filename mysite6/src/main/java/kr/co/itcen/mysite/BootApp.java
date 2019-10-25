package kr.co.itcen.mysite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootConfiguration
//@EnableAutoConfiguration
//@ComponentScan({ "kr.co.itcen.mysite.controller" })

@SpringBootApplication // 이 어노테이션 하나로 위에 3가지 어노테이션 처리가 다 가능함.
public class BootApp {
	public static void main(String[] args) {
		SpringApplication.run(BootApp.class, args);
	}
}
