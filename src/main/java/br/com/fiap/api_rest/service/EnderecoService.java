package br.com.fiap.api_rest.service;

import br.com.fiap.api_rest.dto.EnderecoRequest;
import br.com.fiap.api_rest.dto.EnderecoResponse;
import br.com.fiap.api_rest.model.Endereco;
import org.springframework.stereotype.Service;

@Service
public class EnderecoService {
    public Endereco requestToEndereco(EnderecoRequest enderecoRequest) {
        return new Endereco(enderecoRequest.localizacao());
    }

    public EnderecoResponse enderecoToResponse(Endereco endereco) {
        return new EnderecoResponse(endereco.getId(), endereco.getLocalizacao());
    }
}
