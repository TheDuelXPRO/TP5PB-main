import org.example.suporte.dto.ChamadoDTO;
import org.example.suporte.model.Chamado;
import org.example.suporte.model.PrioridadeChamado;
import org.example.suporte.model.StatusChamado;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class ChamadoDomainTest {

    @Test
    void deveCriarChamadoComValoresPadrao() {
        Chamado chamado = Chamado.abrir("Titulo", "Descricao", PrioridadeChamado.ALTA, "cliente@exemplo.com");

        assertThat(chamado.getStatus()).isEqualTo(StatusChamado.ABERTO);
        assertThat(chamado.getDataAbertura()).isNotNull();
        assertThat(chamado.getDataFechamento()).isNull();
    }

    @Test
    void deveFecharEReabrirChamadoAtualizandoDatas() {
        Chamado chamado = Chamado.abrir("Titulo", "Descricao", PrioridadeChamado.MEDIA, "cliente@exemplo.com");

        chamado.alterarStatus(StatusChamado.FECHADO);
        assertThat(chamado.getStatus()).isEqualTo(StatusChamado.FECHADO);
        assertThat(chamado.getDataFechamento()).isNotNull();

        chamado.alterarStatus(StatusChamado.EM_ANDAMENTO);
        assertThat(chamado.getStatus()).isEqualTo(StatusChamado.EM_ANDAMENTO);
        assertThat(chamado.getDataFechamento()).isNull();
    }

    @Test
    void deveAplicarAtualizacaoAPartirDoDTO() {
        Chamado chamado = Chamado.abrir("Titulo", "Descricao", PrioridadeChamado.MEDIA, "cliente@exemplo.com");
        ReflectionTestUtils.setField(chamado, "id", 5L);

        ChamadoDTO dto = new ChamadoDTO(5L, "Novo Titulo", "Nova descricao", "fechado", "alta", "novo@cliente.com");
        dto.aplicarAtualizacao(chamado);

        assertThat(chamado.getTitulo()).isEqualTo("Novo Titulo");
        assertThat(chamado.getDescricao()).isEqualTo("Nova descricao");
        assertThat(chamado.getPrioridade()).isEqualTo(PrioridadeChamado.ALTA);
        assertThat(chamado.getStatus()).isEqualTo(StatusChamado.FECHADO);
        assertThat(chamado.getClienteEmail()).isEqualTo("novo@cliente.com");
        assertThat(chamado.getDataFechamento()).isNotNull();
    }

    @Test
    void deveConverterChamadoParaDTO() {
        Chamado chamado = Chamado.abrir("Titulo", "Descricao", PrioridadeChamado.MEDIA, "cliente@exemplo.com");
        ReflectionTestUtils.setField(chamado, "id", 9L);
        chamado.alterarStatus(StatusChamado.EM_ANDAMENTO);

        ChamadoDTO dto = ChamadoDTO.fromEntity(chamado);

        assertThat(dto.id()).isEqualTo(9L);
        assertThat(dto.titulo()).isEqualTo("Titulo");
        assertThat(dto.descricao()).isEqualTo("Descricao");
        assertThat(dto.status()).isEqualTo(StatusChamado.EM_ANDAMENTO.name());
        assertThat(dto.prioridade()).isEqualTo(PrioridadeChamado.MEDIA.name());
        assertThat(dto.clienteEmail()).isEqualTo("cliente@exemplo.com");
    }
}
