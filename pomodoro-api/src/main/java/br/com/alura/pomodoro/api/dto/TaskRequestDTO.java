package br.com.alura.pomodoro.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskRequestDTO(

        @NotBlank(message = "O título é obrigatório")
        @Size(min = 3, max = 100)
        String title,

        Boolean completed
) {
}
