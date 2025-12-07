package org.example.suporte.model;

public class StatusChamado {
    ABERTO,
    EM_ANDAMENTO,
    FECHADO;

    public boolean isFechado() {
        return this == FECHADO;
    }

    public static StatusChamado from(String valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Status não pode ser nulo");
        }

        String normalizado = valor.trim();

        return Arrays.stream(values())
                .filter(status -> status.name().equalsIgnoreCase(normalizado))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Status inválido: " + valor));
    }
}
