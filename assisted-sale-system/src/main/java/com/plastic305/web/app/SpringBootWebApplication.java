package com.plastic305.web.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SpringBootWebApplication implements CommandLineRunner{
	@Autowired
	private BCryptPasswordEncoder passwordEncoder ;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception 
	{
//		String pwdAdminClear = "admin";
//		String pwdUserClear = "ok" ;
//		
//        String bCryptPasswordAdmin = passwordEncoder.encode(pwdAdminClear);
//        String bCryptPasswordUser = passwordEncoder.encode(pwdUserClear);
//        
//        
//        System.out.println(bCryptPasswordAdmin);
//        System.out.println(bCryptPasswordUser);
	}
	
	
}
