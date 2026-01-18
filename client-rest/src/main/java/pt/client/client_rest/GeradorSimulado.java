package pt.client.client_rest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class GeradorSimulado {

    private float temperatura;
    private float humidade;
    private final Random random = new Random();

    public void gerarNovosDados() {
        // Gera valores simulados
        this.temperatura = 10 + random.nextFloat() * 20; // Temperatura entre 10 e 30
        this.humidade = 30 + random.nextFloat() * 40;    // Humidade entre 30 e 70
    }

    public float getTemperatura() {
        return temperatura;
    }

    public float getHumidade() {
        return humidade;
    }

    public String getTimestampAtual() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }
}