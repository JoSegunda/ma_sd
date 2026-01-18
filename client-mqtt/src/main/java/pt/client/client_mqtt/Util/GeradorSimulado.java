package pt.client.client_mqtt.Util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class GeradorSimulado {
    private double temperaturaAtual = 22.0; // Ponto de partida realista
    private double humidadeAtual = 50.0;
    private final Random random = new Random();

    public void gerarNovosDados() {
        // Variação gradual: +/- 0.5 graus/percentagem [cite: 69, 73]
        temperaturaAtual += (random.nextDouble() - 0.5);
        humidadeAtual += (random.nextDouble() - 0.5);

        // Garantir intervalos obrigatórios [cite: 67, 68, 72]
        temperaturaAtual = Math.max(15, Math.min(30, temperaturaAtual));
        humidadeAtual = Math.max(30, Math.min(80, humidadeAtual));
    }

    public float getTemperatura() { return (float) temperaturaAtual; }
    public float getHumidade() { return (float) humidadeAtual; }
    
    public String getTimestampAtual() {
        // Formato ISO 8601 recomendado [cite: 77]
        return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
