package org.example.suporte.repository;

import java.util.List;
import org.example.suporte.model.Chamado;
import org.example.suporte.model.StatusChamado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChamadoRepository extends JpaRepository<Chamado, Long> {

    List<Chamado> findByStatus(StatusChamado status);
}
