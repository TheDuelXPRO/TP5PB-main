package org.example.suporte.service;

import org.example.suporte.dto.ChamadoDTO;
import org.example.suporte.exception.ErroExternoException;
import org.example.suporte.exception.RecursoNaoEncontradoException;
import org.example.suporte.model.Chamado;
import org.example.suporte.repository.ChamadoRepository;
import org.example.suporte.service.external.NotificacaoClient;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;
import org.springframework.stereotype.Service;

@Service
public class ChamadoService {

    private final ChamadoRepository repository;
    private final NotificacaoClient notificacaoClient;

    public ChamadoService(ChamadoRepository repository, NotificacaoClient notificacaoClient) {
        this.repository = repository;
        this.notificacaoClient = notificacaoClient;
    }

    public Chamado criarChamado(@Valid ChamadoDTO dto) {
        Chamado chamado = dto.toNovoChamado();
        Chamado salvo = repository.save(chamado);
        try {
            notificacaoClient.enviarNotificacaoAbertura(salvo);
        } catch (IOException | TimeoutException e) {
            throw new ErroExternoException("Não foi possível notificar abertura do chamado", e);
        }
        return salvo;
    }

    public Chamado buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Chamado não encontrado"));
    }

    public List<Chamado> listarTodos() {
        return repository.findAll();
    }

    public Chamado atualizar(Long id, @Valid ChamadoDTO dto) {
        Chamado existente = buscarPorId(id);
        dto.aplicarAtualizacao(existente);
        return repository.save(existente);
    }

    public void excluir(Long id) {
        Chamado existente = buscarPorId(id);
        repository.delete(existente);
    }
}
