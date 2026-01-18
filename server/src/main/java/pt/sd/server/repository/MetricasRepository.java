package pt.sd.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.sd.server.model.Dispositivo;
import pt.sd.server.model.Metricas;

public interface MetricasRepository extends JpaRepository<Dispositivo, String> {}
public interface DispositivoRepository extends JpaRepository<Metricas, Long> {}


