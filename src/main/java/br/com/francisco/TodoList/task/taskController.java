package br.com.francisco.TodoList.task;

import java.util.UUID;
import java.time.LocalDateTime;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

import br.com.francisco.TodoList.utils.Utils;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;



import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/tasks")
public class taskController {
    
    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody task taskModel, HttpServletRequest request){
        var idUser = request.getAttribute("idUser");
        taskModel.setIdUser((UUID) idUser);
       
       var currentDate = LocalDateTime.now();
       if(currentDate.isAfter(taskModel.getStartAt())||currentDate.isAfter(taskModel.getEndAt())){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("A data de início deve ser maior do que a data atual");
       }

        if(taskModel.getStartAt().isAfter(taskModel.getEndAt())){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("A data de início deve ser menor do que a data de término");
       }
        var Task = this.taskRepository.save(taskModel);
        return ResponseEntity.status(200).body(Task);
    }

    @GetMapping("/")
    public List<task> list(HttpServletRequest request){
        var idUser = request.getAttribute("idUser");
        var tasks = this.taskRepository.findByIdUser((UUID) idUser);
        return tasks;
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody task taskModel, @PathVariable UUID id,HttpServletRequest request){
        var tasks = this.taskRepository.findById(id).orElse(null);
        var idUser = request.getAttribute("idUser");
        
        if(tasks == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa não econtrada");
        }


        Utils.copyNonNullProperties(taskModel, tasks);

        if(!tasks.getIdUser().equals(idUser)){
            return ResponseEntity.status(401).body("Usuário não possui permissão para alterar essa tarefa");
        }

        var taskUpdate = this.taskRepository.save(tasks);
        return ResponseEntity.ok().body(taskUpdate);
    }
}
