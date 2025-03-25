package br.com.fiap.api_rest.dto;

import jakarta.validation.constraints.NotBlank;

public record EnderecoRequest(
        @NotBlank(message = "A localização é obrigatória") String localizacao
) {
}
