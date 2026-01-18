package pt.sd.server.repository;


import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pt.sd.server.model.Metricas;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MetricasRepository extends JpaRepository<Metricas, Long> {
    
    // Método para encontrar métricas brutas de um dispositivo num intervalo[cite: 118].
    List<Metricas> findByIdDispositivoAndTempoBetween(String idDispositivo, LocalDateTime inicio, LocalDateTime fim);

    // No futuro, adicionaremos aqui as queries customizadas para as médias agregadas [cite: 113-116].
}
