package br.com.fiap.api_rest.service;

import br.com.fiap.api_rest.dto.AutorRequest;
import br.com.fiap.api_rest.dto.AutorResponse;
import br.com.fiap.api_rest.model.Autor;
import br.com.fiap.api_rest.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AutorService {
    @Autowired
    private AutorRepository autorRepository;
    @Autowired
    private LivroService livroService;

    public Autor requestToAutor(AutorRequest autorRequest) {
        return new Autor(autorRequest.nome());
    }

    public AutorResponse autorToResponse(Autor autor) {
        return new AutorResponse(
                autor.getId(),
                autor.getNome(),
                livroService.livrosToResponse(autor.getLivros()));
    }

    public AutorResponse save(AutorRequest autorRequest) {
        return autorToResponse(autorRepository.save(requestToAutor(autorRequest)));
    }

    public Page<AutorResponse> findAll(Pageable pageable) {
        return autorRepository.findAll(pageable).map(this::autorToResponse);
    }

    public AutorResponse findById(Long id) {
        Optional<Autor> autor = autorRepository.findById(id);
        return autor.map(this::autorToResponse).orElse(null);
    }

    public AutorResponse update(AutorRequest autorRequest, Long id) {
        Optional<Autor> autor = autorRepository.findById(id);
        if (autor.isPresent()) {
            Autor autorSalvo = autorRepository.save(autor.get());
            return autorToResponse(autorSalvo);
        }
        return null;
    }

    public boolean delete(Long id) {
        Optional<Autor> autor = autorRepository.findById(id);
        if (autor.isPresent()) {
            autorRepository.delete(autor.get());
            return true;
        }
        return false;
    }
}
