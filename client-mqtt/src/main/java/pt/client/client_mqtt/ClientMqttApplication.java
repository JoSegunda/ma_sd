package pt.client.client_mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // datas
import pt.client.client_mqtt.model.Metricas; 
import java.util.Locale;

public class ClientMqttApplication {

    public static void main(String[] args) {
        String broker = "tcp://localhost:1883";
        String clientId = "Sensor-MQTT-Client-Gen";
        String topico = "universidade/metricas"; // Confirme se o server ouve este tópico
        
        // --- CORREÇÃO 1: ID IGUAL AO REGISTO NO ADMIN-CLI ---
        String idDispositivoNaBD = "SENSOR_MQTT_01"; 
        // (Antes estava "SENSOR_MQTT_LAB_01", que não deve estar registado)

        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            
            System.out.println("A ligar ao broker: " + broker);
            sampleClient.connect(connOpts);
            System.out.println("Ligado!");

            while (true) {
                // Simulação de dados
                float temp = 20.0f + (float)(Math.random() * 5);
                float hum = 50.0f + (float)(Math.random() * 10);
                String tempoAgora = java.time.LocalDateTime.now().toString();

                
                String content = String.format(Locale.US,
                    "{\"id\":\"%s\", \"temperatura\":%.2f, \"humidade\":%.2f, \"tempo\":\"%s\"}",
                    idDispositivoNaBD, 
                    temp, 
                    hum, 
                    tempoAgora
                );
                

                MqttMessage message = new MqttMessage(content.getBytes());
                message.setQos(1);
                
                sampleClient.publish(topico, message);
                System.out.println("Enviado MQTT: " + content);
                
                Thread.sleep(5000);
            }

        } catch (Exception me) {
            System.err.println("Erro MQTT: " + me.getMessage());
            me.printStackTrace();
        }
    }
}