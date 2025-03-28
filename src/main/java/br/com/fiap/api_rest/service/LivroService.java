package br.com.fiap.api_rest.service;

import br.com.fiap.api_rest.controller.LivroController;
import br.com.fiap.api_rest.dto.*;
import br.com.fiap.api_rest.model.Autor;
import br.com.fiap.api_rest.model.Livro;
import br.com.fiap.api_rest.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class LivroService {
    @Autowired
    private LivroRepository livroRepository;
    @Autowired
    private AutorService autorService;

    public Livro requestToLivro(LivroRequest livroRequest) {
        Livro livro = new Livro();
        List<Autor> autores = new ArrayList<>();
        for (AutorRequest autorRequest : livroRequest.getAutores()) {
            autores.add(autorService.requestToAutor(autorRequest));
        }
        livro.setAutores(autores);
        livro.setTitulo(livroRequest.getTitulo());
        livro.setPreco(livroRequest.getPreco());
        livro.setCategoria(livroRequest.getCategoria());
        livro.setIsbn(livroRequest.getIsbn());
        return livro;
    }

    public LivroResponse livroToResponse(Livro livro) {

        return new LivroResponse(livro.getId(), livro.getAutores().stream().map(Autor::getNome) + " - " + livro.getTitulo());
    }

    public LivroResponseDTO livroToResponseDTO(Livro livro, boolean self) {
        Link link;
        if (self) {
            link = linkTo(methodOn(LivroController.class).readLivro(livro.getId())).withSelfRel();
        } else {
            link = linkTo(methodOn(LivroController.class).readLivros(0)).withRel("Lista de Livros");
        }
        return new LivroResponseDTO(livro.getId(), livro.getAutores().stream().map(Autor::getNome) + " - " + livro.getTitulo(), link);
    }

    public List<LivroResponse> livrosToResponse(List<Livro> livros) {
        List<LivroResponse> listaLivros = new ArrayList<>();
        for (Livro livro : livros) {
            listaLivros.add(livroToResponse(livro));
        }
        return listaLivros;
    }

    public Page<LivroResponse> findAll(Pageable pageable) {
        //return livroRepository.findAll(pageable).map(livro -> livroToResponse(livro));
        return livroRepository.findAll(pageable).map(this::livroToResponse);
    }

    public Page<LivroResponseDTO> findAllDTO(Pageable pageable) {
        return livroRepository.findAll(pageable).map(livro -> livroToResponseDTO(livro, true));
    }
}
