package by.youngliqui.EShopProject;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "E-SHOP API", description = "Мои контактный данные:",
		contact = @Contact(name = "Петров Павел", url = "https://github.com/youngliqui"),version = "v1"))
public class EShopProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(EShopProjectApplication.class, args);
	}
}
