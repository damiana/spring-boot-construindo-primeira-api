package br.com.alura.pomodoro.api.controller;

import br.com.alura.pomodoro.api.dto.TaskRequestDTO;
import br.com.alura.pomodoro.api.dto.TaskResponseDTO;
import br.com.alura.pomodoro.api.model.Task;
import br.com.alura.pomodoro.api.repository.TaskRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getTasks(@RequestParam(required = false) Boolean completed) {
        List<Task> tasks = completed == null
                ? taskRepository.findAll()
                : taskRepository.findByCompleted(completed);
        return ResponseEntity.ok(tasks.stream().map(this::toDTO).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id) {
        return taskRepository.findById(id)
                .map(task -> ResponseEntity.ok(toDTO(task)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody TaskRequestDTO dto) {
        Task saved = taskRepository.save(toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequestDTO dto) {
        return taskRepository.findById(id)
                .map(existing -> {
                    Task updated = toEntity(dto);
                    existing.setTitle(updated.getTitle());
                    existing.setCompleted(updated.getCompleted());
                    return ResponseEntity.ok(toDTO(taskRepository.save(existing)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (!taskRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        taskRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private TaskResponseDTO toDTO(Task task) {
        return new TaskResponseDTO(task.getId(), task.getTitle(), task.getCompleted());
    }

    private Task toEntity(TaskRequestDTO dto) {
        Task task = new Task();
        task.setTitle(dto.title());
        task.setCompleted(dto.completed() != null ? dto.completed() : false);
        return task;
    }
}
