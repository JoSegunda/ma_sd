package pt.sd.server.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Metricas {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String idDispositivo;
    private float temperatura;
    private float humidade;
    private LocalDateTime tempo;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getIdDispositivo() {
        return idDispositivo;
    }
    public void setIdDispositivo(String idDispositivo) {
        this.idDispositivo = idDispositivo;
    }
    public float getTemperatura() {
        return temperatura;
    }
    public void setTemperatura(float temperatura) {
        this.temperatura = temperatura;
    }
    public float getHumidade() {
        return humidade;
    }
    public void setHumidade(float humidade) {
        this.humidade = humidade;
    }
    public LocalDateTime getTempo() {
        return tempo;
    }
    public void setTempo(LocalDateTime tempo) {
        this.tempo = tempo;
    }

    
}
