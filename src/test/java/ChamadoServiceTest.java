import org.example.suporte.dto.ChamadoDTO;
import org.example.suporte.exception.ErroExternoException;
import org.example.suporte.exception.RecursoNaoEncontradoException;
import org.example.suporte.model.Chamado;
import org.example.suporte.model.PrioridadeChamado;
import org.example.suporte.model.StatusChamado;
import org.example.suporte.repository.ChamadoRepository;
import org.example.suporte.service.ChamadoService;
import org.example.suporte.service.external.NotificacaoClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChamadoServiceTest {

    @Mock
    private ChamadoRepository repository;

    @Mock
    private NotificacaoClient notificacaoClient;

    @InjectMocks
    private ChamadoService service;

    @Test
    void deveCriarChamadoENotificar() throws Exception {
        ChamadoDTO dto = new ChamadoDTO(null, "Erro no sistema", "Aplicação não inicia", StatusChamado.ABERTO.name(), PrioridadeChamado.ALTA.name(), "cliente@exemplo.com");

        when(repository.save(any(Chamado.class))).thenAnswer(invocation -> {
            Chamado chamado = invocation.getArgument(0);
            ReflectionTestUtils.setField(chamado, "id", 1L);
            return chamado;
        });

        Chamado salvo = service.criarChamado(dto);

        assertThat(salvo.getId()).isEqualTo(1L);
        assertThat(salvo.getTitulo()).isEqualTo(dto.titulo());
        assertThat(salvo.getPrioridade()).isEqualTo(PrioridadeChamado.ALTA);
        verify(repository).save(any(Chamado.class));
        verify(notificacaoClient).enviarNotificacaoAbertura(salvo);
    }

    @Test
    void deveLancarErroExternoQuandoNotificacaoFalhar() throws Exception {
        ChamadoDTO dto = new ChamadoDTO(null, "Erro no sistema", "Aplicação não inicia", StatusChamado.ABERTO.name(), PrioridadeChamado.BAIXA.name(), "cliente@exemplo.com");

        when(repository.save(any(Chamado.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doThrow(new IOException("falha de rede")).when(notificacaoClient).enviarNotificacaoAbertura(any(Chamado.class));

        assertThrows(ErroExternoException.class, () -> service.criarChamado(dto));
    }

    @Test
    void deveRetornarChamadoExistente() {
        Chamado existente = Chamado.abrir("Titulo", "Detalhes", PrioridadeChamado.MEDIA, "cliente@exemplo.com");
        ReflectionTestUtils.setField(existente, "id", 10L);
        when(repository.findById(10L)).thenReturn(Optional.of(existente));

        Chamado encontrado = service.buscarPorId(10L);

        assertThat(encontrado.getId()).isEqualTo(10L);
        assertThat(encontrado.getStatus()).isEqualTo(StatusChamado.ABERTO);
    }

    @Test
    void deveLancarQuandoChamadoNaoEncontrado() {
        when(repository.findById(50L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> service.buscarPorId(50L));
    }

    @Test
    void deveAtualizarChamadoExistente() {
        Chamado existente = Chamado.abrir("Titulo", "Detalhes", PrioridadeChamado.MEDIA, "cliente@exemplo.com");
        ReflectionTestUtils.setField(existente, "id", 3L);
        when(repository.findById(3L)).thenReturn(Optional.of(existente));
        when(repository.save(any(Chamado.class))).thenAnswer(invocation -> invocation.getArgument(0));
        ChamadoDTO dto = new ChamadoDTO(3L, "Novo titulo", "Nova desc", StatusChamado.FECHADO.name(), PrioridadeChamado.ALTA.name(), "novo@cliente.com");

        Chamado atualizado = service.atualizar(3L, dto);

        assertThat(atualizado.getTitulo()).isEqualTo("Novo titulo");
        assertThat(atualizado.getDescricao()).isEqualTo("Nova desc");
        assertThat(atualizado.getPrioridade()).isEqualTo(PrioridadeChamado.ALTA);
        assertThat(atualizado.getStatus()).isEqualTo(StatusChamado.FECHADO);
        assertThat(atualizado.getClienteEmail()).isEqualTo("novo@cliente.com");
    }

    @Test
    void deveExcluirChamado() {
        Chamado existente = Chamado.abrir("Titulo", "Detalhes", PrioridadeChamado.MEDIA, "cliente@exemplo.com");
        ReflectionTestUtils.setField(existente, "id", 4L);
        when(repository.findById(4L)).thenReturn(Optional.of(existente));

        service.excluir(4L);

        verify(repository).delete(existente);
    }
}
