package kz.adem.gatewayservice;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@RequiredArgsConstructor
@OpenAPIDefinition(
		info = @Info(
				title = "User service REST API",
				description = "User service REST API documentation",
				version = "v1.0",
				contact = @Contact(
						name = "Adem",
						email = "ademshanghai@gmail.com",
						url = "http://github.com/kissmylala"
				)
		)
)

public class GatewayServiceApplication {


	public static void main(String[] args) {
		SpringApplication.run(GatewayServiceApplication.class, args);
	}

}

