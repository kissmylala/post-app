package kz.adem.userservice;

import kz.adem.userservice.dto.UserDto;
import kz.adem.userservice.entity.Role;
import kz.adem.userservice.entity.User;
import kz.adem.userservice.repository.RoleRepository;
import kz.adem.userservice.repository.UserRepository;
import kz.adem.userservice.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(
			UserService service,
			RoleRepository roleRepository,
			PasswordEncoder passwordEncoder,
			UserRepository userRepository
	) {
		return args -> {
			Role userRole = roleRepository.findByName("ROLE_USER").orElseGet(() -> {
				Role newUserRole = new Role();
				newUserRole.setName("ROLE_USER");
				return roleRepository.save(newUserRole);
			});

			Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
				Role newAdminRole = new Role();
				newAdminRole.setName("ROLE_ADMIN");
				return roleRepository.save(newAdminRole);
			});

			userRepository.findByUsername("admin").orElseGet(() -> {
				var admin = UserDto.builder()
						.name("Admin")
						.username("admin")
						.email("admin@gmail.com")
						.password(passwordEncoder.encode("admin"))
						.build();
				service.createAdmin(admin);
				System.out.println("Admin is created, login: " + admin.getUsername() + " password: " + admin.getPassword());
				return null;  // возвращаем null, так как метод orElseGet ожидает значение (или null, если ничего не было выполнено)
			});

			// Проверка наличия пользователя
			userRepository.findByUsername("user").orElseGet(() -> {
				var user = UserDto.builder()
						.name("User")
						.username("user")
						.email("user@gmail.com")
						.password(passwordEncoder.encode("user"))
						.build();
				service.createUser(user);
				System.out.println("Admin is created, login: " + user.getUsername() + " password: " + user.getPassword());
				return null;  // аналогично для пользователя
			});

		};
	}

}
