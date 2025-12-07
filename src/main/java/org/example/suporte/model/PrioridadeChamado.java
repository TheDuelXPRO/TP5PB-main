package org.example.suporte.model;

public class PrioridadeChamado {
    BAIXA,
    MEDIA,
    ALTA,
    CRITICA;

    public static PrioridadeChamado from(String valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Prioridade não pode ser nula");
        }

        String normalizado = valor.trim();

        return Arrays.stream(values())
                .filter(p -> p.name().equalsIgnoreCase(normalizado))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Prioridade inválida: " + valor));
    }
}
