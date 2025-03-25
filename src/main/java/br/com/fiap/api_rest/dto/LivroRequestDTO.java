package br.com.fiap.api_rest.dto;

import br.com.fiap.api_rest.model.Autor;

import java.util.List;

public record LivroRequestDTO(String titulo, List<Autor> autores) {
}
