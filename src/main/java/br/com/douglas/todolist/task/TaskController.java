package br.com.douglas.todolist.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TaskController
 */
@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private ITasksRepository tasksRepository;
    
    @PostMapping("/")
    public TaskModel create(@RequestBody TaskModel taskModel) {
        return  tasksRepository.save(taskModel);
    }
}