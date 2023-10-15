package br.com.francisco.TodoList.task;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;


public interface ITaskRepository extends JpaRepository<task, UUID>{
    List<task> findByIdUser(UUID idUser);
}
