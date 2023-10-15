package br.com.francisco.TodoList.users;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface UserRepository  extends JpaRepository<UserModel, UUID>{
    UserModel findByUsername(String username);
    UserModel findByPassword(String password);
}
