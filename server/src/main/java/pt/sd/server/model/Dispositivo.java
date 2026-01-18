package pt.sd.server.model;

import org.springframework.data.annotation.Id;
import jakarta.persistence.Entity;

@Entity
public class Dispositivo {
    @Id
    private String id;
    private String protocolo; // MQTT, GRPC, REST [cite: 108]
    private String sala;
    private String departmento;
    private String andar;
    private String edificio;
    private boolean ativo;

    
    // Getters e Setters...
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getProtocolo() {
        return protocolo;
    }
    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }
    public String getSala() {
        return sala;
    }
    public void setSala(String sala) {
        this.sala = sala;
    }
    public String getDepartmento() {
        return departmento;
    }
    public void setDepartmento(String departmento) {
        this.departmento = departmento;
    }
    public String getAndar() {
        return andar;
    }
    public void setAndar(String andar) {
        this.andar = andar;
    }
    public String getEdificio() {
        return edificio;
    }
    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }
    public boolean isAtivo() {
        return ativo;
    }
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }


    

}
