package org.example.suporte.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.suporte.model.Chamado;
import org.example.suporte.model.PrioridadeChamado;
import org.example.suporte.model.StatusChamado;

public record ChamadoDTO(
        Long id,

        @NotBlank(message = "Título é obrigatório")
        @Size(max = 100, message = "Título deve ter no máximo 100 caracteres")
        String titulo,

        @NotBlank(message = "Descrição é obrigatória")
        String descricao,

        @NotBlank(message = "Status é obrigatório")
        String status,

        @NotBlank(message = "Prioridade é obrigatória")
        String prioridade,

        @Email(message = "E-mail inválido")
        @NotBlank(message = "E-mail é obrigatório")
        String clienteEmail
) {

    public Chamado toNovoChamado() {
        return Chamado.abrir(
                titulo,
                descricao,
                PrioridadeChamado.from(prioridade),
                clienteEmail
        );
    }

    public void aplicarAtualizacao(Chamado chamado) {
        chamado.atualizarDetalhes(
                titulo,
                descricao,
                PrioridadeChamado.from(prioridade),
                clienteEmail
        );
        chamado.alterarStatus(StatusChamado.from(status));
    }

    public static ChamadoDTO fromEntity(Chamado chamado) {
        return new ChamadoDTO(
                chamado.getId(),
                chamado.getTitulo(),
                chamado.getDescricao(),
                chamado.getStatus() != null ? chamado.getStatus().name() : null,
                chamado.getPrioridade() != null ? chamado.getPrioridade().name() : null,
                chamado.getClienteEmail()
        );
    }

    public static ChamadoDTO novoPadrao() {
        return new ChamadoDTO(
                null,
                "",
                "",
                StatusChamado.ABERTO.name(),
                PrioridadeChamado.MEDIA.name(),
                ""
        );
    }
}
