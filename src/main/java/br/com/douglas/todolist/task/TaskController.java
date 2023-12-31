package br.com.douglas.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.douglas.todolist.Utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

/**
 * TaskController
 */
@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private ITasksRepository tasksRepository;

    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        UUID idUser = (UUID) (request.getAttribute("idUser"));
        taskModel.setIdUser(idUser);

        LocalDateTime currentDate = LocalDateTime.now();
        if (currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Start Date / end Date must be higher than the current date");
        }

        if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Start Date can't be higher than the end date ");
        }

        TaskModel task = tasksRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request) {
        UUID idUser = (UUID) (request.getAttribute("idUser"));
        return tasksRepository.findByIdUser(idUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody TaskModel taskModel,@PathVariable UUID id,
            HttpServletRequest request)

    {
        TaskModel task = tasksRepository.findById(id).orElse(null);

        if (task == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Task not found");
        }

        var idUser = request.getAttribute("idUser");

        if (!task.getIdUser().equals(idUser)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("User does not have permission to change this task");
        }

        Utils.copyNonNullProperties(taskModel, task);
        return ResponseEntity.ok().body(tasksRepository.save(task));
    }

}