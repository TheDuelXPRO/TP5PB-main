package org.example.suporte.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
public class Chamado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String titulo;

    @NotBlank
    @Size(max = 2000)
    private String descricao;

    @Enumerated(EnumType.STRING)
    private StatusChamado status;

    @Enumerated(EnumType.STRING)
    private PrioridadeChamado prioridade;

    @Email
    @NotBlank
    private String clienteEmail;

    private LocalDateTime dataAbertura;
    private LocalDateTime dataFechamento;

    protected Chamado() {
    }

    private Chamado(String titulo,
                    String descricao,
                    PrioridadeChamado prioridade,
                    String clienteEmail) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.clienteEmail = clienteEmail;
        this.status = StatusChamado.ABERTO;
        this.dataAbertura = LocalDateTime.now();
    }

    public static Chamado abrir(String titulo,
                                String descricao,
                                PrioridadeChamado prioridade,
                                String clienteEmail) {
        return new Chamado(titulo, descricao, prioridade, clienteEmail);
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public StatusChamado getStatus() {
        return status;
    }

    public PrioridadeChamado getPrioridade() {
        return prioridade;
    }

    public String getClienteEmail() {
        return clienteEmail;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public LocalDateTime getDataFechamento() {
        return dataFechamento;
    }

    public void atualizarDetalhes(String titulo,
                                  String descricao,
                                  PrioridadeChamado prioridade,
                                  String clienteEmail) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.clienteEmail = clienteEmail;
    }

    public void alterarStatus(StatusChamado novoStatus) {
        if (novoStatus == null || novoStatus == this.status) {
            return;
        }

        this.status = novoStatus;

        if (novoStatus == StatusChamado.FECHADO) {
            fecharChamado();
        } else {
            reabrirChamado();
        }
    }

    private void fecharChamado() {
        if (dataFechamento == null) {
            dataFechamento = LocalDateTime.now();
        }
    }

    private void reabrirChamado() {
        dataFechamento = null;
    }
}
