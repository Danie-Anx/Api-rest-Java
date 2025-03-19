package br.com.fiap.api_rest.service;

import br.com.fiap.api_rest.controller.LivroController;
import br.com.fiap.api_rest.dto.*;
import br.com.fiap.api_rest.model.Autor;
import br.com.fiap.api_rest.model.Livro;
import br.com.fiap.api_rest.repository.LivroRepository;
import br.com.fiap.api_rest.repository.AutorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LivroService {

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LivroRepository livroRepository;

    public Livro requestToLivro(LivroRequest livroRequest) {
        Livro livro = new Livro();
        List<Autor> autores = autorRepository.findAllById(livroRequest.getAutoresIds());
        livro.setAutores(autores);
        livro.setTitulo(livroRequest.getTitulo());
        livro.setPreco(livroRequest.getPreco());
        livro.setCategoria(livroRequest.getCategoria());
        livro.setIsbn(livroRequest.getIsbn());
        return livro;
    }

    public Livro recordToLivro(LivroRequestDTO livroRecord) {
        Livro livro = new Livro();
        livro.setTitulo(livroRecord.titulo());
        livro.setAutores(livroRecord.autores());
        return livro;
    }

    public LivroResponse livroToResponse(Livro livro) {
        String info = formatarAutores(livro.getAutores()) + " - " + livro.getTitulo();
        return new LivroResponse(livro.getId(), info);
    }

    public LivroResponseDTO livroToResponseDTO(Livro livro, boolean self) {
        Link link;
        if (self) {
            link = WebMvcLinkBuilder.linkTo(
                            WebMvcLinkBuilder.methodOn(LivroController.class).read(livro.getId()))
                    .withSelfRel();
        } else {
            link = WebMvcLinkBuilder.linkTo(
                            WebMvcLinkBuilder.methodOn(LivroController.class).read(livro.getId()))
                    .withRel("Lista de Livros");
        }

        String info = formatarAutores(livro.getAutores()) + " - " + livro.getTitulo();
        return new LivroResponseDTO(livro.getId(), info, link);
    }

    public List<LivroResponse> livrosToResponse(List<Livro> livros) {
        List<LivroResponse> listaLivros = new ArrayList<>();
        for (Livro livro : livros) {
            listaLivros.add(livroToResponse(livro));
        }
        return listaLivros;
    }

    public Page<LivroResponseDTO> findAllDTO(Pageable pageable) {
        return livroRepository.findAll(pageable)
                .map(livro -> livroToResponseDTO(livro, false));
    }

    private String formatarAutores(List<Autor> autores) {
        if (autores == null || autores.isEmpty()) return "Autor desconhecido";
        List<String> nomes = new ArrayList<>();
        for (Autor autor : autores) {
            nomes.add(autor.getNome());
        }
        return String.join(", ", nomes);
    }
}
