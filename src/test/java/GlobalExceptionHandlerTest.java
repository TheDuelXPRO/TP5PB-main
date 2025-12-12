import org.example.suporte.config.GlobalExceptionHandler;
import org.example.suporte.exception.ErroExternoException;
import org.example.suporte.exception.RecursoNaoEncontradoException;
import org.example.suporte.exception.ValidacaoException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void deveRetornarBadRequestParaValidacao() {
        var response = handler.handleValidacao(new ValidacaoException("erro de validação"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsEntry("erro", "erro de validação");
    }

    @Test
    void deveRetornarNotFoundParaRecursoInexistente() {
        var response = handler.handleNotFound(new RecursoNaoEncontradoException("não encontrado"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).containsEntry("erro", "não encontrado");
    }

    @Test
    void deveRetornarServiceUnavailableParaErroExterno() {
        var response = handler.handleErroExterno(new ErroExternoException("falha", new RuntimeException("x")));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        assertThat(response.getBody()).containsEntry("erro", "Serviço temporariamente indisponível. Tente novamente mais tarde.");
    }

    @Test
    void deveTratarErroGenericoComStatus500() {
        var response = handler.handleGeneric(new IllegalStateException("falha"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).containsEntry("erro", "Erro inesperado. Tente novamente mais tarde.");
    }
}
