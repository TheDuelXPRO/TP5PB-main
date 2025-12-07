package org.example.suporte.service.external;

import org.example.suporte.model.Chamado;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface NotificacaoClient {

    void enviarNotificacaoAbertura(Chamado chamado) throws IOException, TimeoutException;
}
