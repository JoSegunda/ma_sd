package pt.sd.server.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pt.sd.server.model.Metricas;
import java.time.LocalDateTime;

public interface MetricasRepository extends JpaRepository<Metricas, Long> {

    // Consulta genérica: Média por Edifício
    @Query("SELECT AVG(m.temperatura), AVG(m.humidade) FROM Metricas m, Dispositivo d " +
           "WHERE m.idDispositivo = d.id AND d.edificio = :edificio " +
           "AND m.tempo BETWEEN :inicio AND :fim")
    String obterMediaPorEdificio(@Param("edificio") String edificio, 
                                 @Param("inicio") LocalDateTime inicio, 
                                 @Param("fim") LocalDateTime fim);

    // Consulta genérica: Média por Sala
    @Query("SELECT AVG(m.temperatura), AVG(m.humidade) FROM Metricas m, Dispositivo d " +
           "WHERE m.idDispositivo = d.id AND d.sala = :sala " +
           "AND m.tempo BETWEEN :inicio AND :fim")
    String obterMediaPorSala(@Param("sala") String sala, 
                             @Param("inicio") LocalDateTime inicio, 
                             @Param("fim") LocalDateTime fim);
                             
    // Nota: O retorno é uma String "temp,hum" ou Object[]. 
    // Para simplificar, o Spring converte CSV. O ideal seria uma interface de projeção.
}