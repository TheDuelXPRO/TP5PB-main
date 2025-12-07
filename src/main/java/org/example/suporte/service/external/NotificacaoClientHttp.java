package org.example.suporte.service.external;

import org.example.suporte.model.Chamado;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import org.springframework.stereotype.Component;

@Component
public class NotificacaoClientHttp implements NotificacaoClient {

    private final Random random = new Random();

    @Override
    public void enviarNotificacaoAbertura(Chamado chamado) throws IOException, TimeoutException {
        int x = random.nextInt(10);
        if (x == 0) {
            throw new TimeoutException("Timeout ao chamar serviço de notificação");
        }
        if (x == 1) {
            throw new IOException("Falha de rede ao chamar serviço de notificação");
        }
    }
}
