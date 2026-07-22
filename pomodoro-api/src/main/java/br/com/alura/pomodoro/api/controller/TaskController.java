package br.com.alura.pomodoro.api.controller;

import br.com.alura.pomodoro.api.model.Task;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private List<Task> tasks = new ArrayList<>();

    public TaskController() {
        tasks.add(new Task(1L, "Estudar Spring Boot", false));
        tasks.add(new Task(2L, "Fazer exercícios de revisão", false));
        tasks.add(new Task(3L, "Assistir aula de testes", true));
    }

    @GetMapping
    public List<Task> getTasks(@RequestParam(required = false) Boolean completed) {
        if (completed == null) {
            return tasks;
        }
        return tasks.stream()
                .filter(task -> task.getCompleted().equals(completed))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
