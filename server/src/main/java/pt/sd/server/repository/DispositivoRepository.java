package pt.sd.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.sd.server.model.Dispositivo;

@Repository
public interface DispositivoRepository extends JpaRepository<Dispositivo, String> {
    // O JpaRepository já fornece métodos como findById, save, deleteById e findAll 
}