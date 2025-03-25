package br.com.fiap.api_rest.service;

import br.com.fiap.api_rest.dto.BibliotecaRequest;
import br.com.fiap.api_rest.dto.BibliotecaResponse;
import br.com.fiap.api_rest.model.Biblioteca;
import br.com.fiap.api_rest.repository.BibliotecaRepository;
import br.com.fiap.api_rest.repository.BibliotecaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BibliotecaService {
    @Autowired
    private BibliotecaRepository bibliotecaRepository;
    @Autowired
    private EnderecoService enderecoService;

    public Biblioteca requestToBiblioteca(BibliotecaRequest bibliotecaRequest) {
        Biblioteca biblioteca = new Biblioteca();
        biblioteca.setNome(bibliotecaRequest.nome());
        biblioteca.setEndereco(enderecoService.requestToEndereco(bibliotecaRequest.endereco()));
        return biblioteca;
    }

    public BibliotecaResponse bibliotecaToResponse(Biblioteca biblioteca) {
        return new BibliotecaResponse(
                biblioteca.getId(),
                biblioteca.getNome(),
                enderecoService.enderecoToResponse(biblioteca.getEndereco()));
    }

    public BibliotecaResponse save(BibliotecaRequest bibliotecaRequest) {
        return bibliotecaToResponse(bibliotecaRepository.save(requestToBiblioteca(bibliotecaRequest)));
    }

    public Page<BibliotecaResponse> findAll(Pageable pageable) {
        return bibliotecaRepository.findAll(pageable).map(this::bibliotecaToResponse);
    }

    public BibliotecaResponse findById(Long id) {
        Optional<Biblioteca> biblioteca = bibliotecaRepository.findById(id);
        return biblioteca.map(this::bibliotecaToResponse).orElse(null);
    }

    public BibliotecaResponse update(BibliotecaRequest bibliotecaRequest, Long id) {
        Optional<Biblioteca> biblioteca = bibliotecaRepository.findById(id);
        if (biblioteca.isPresent()) {
            Biblioteca bibliotecaSalvo = bibliotecaRepository.save(biblioteca.get());
            return bibliotecaToResponse(bibliotecaSalvo);
        }
        return null;
    }

    public boolean delete(Long id) {
        Optional<Biblioteca> biblioteca = bibliotecaRepository.findById(id);
        if (biblioteca.isPresent()) {
            bibliotecaRepository.delete(biblioteca.get());
            return true;
        }
        return false;
    }
}
