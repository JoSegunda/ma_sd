package pt.client.client_mqtt;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import pt.sd.server.model.Metricas;

@SpringBootApplication
public class ClientMqttApplication {
    public static void main(String[] args) throws Exception {
        String broker = "tcp://localhost:61616"; // ActiveMQ [cite: 22]
        MqttClient client = new MqttClient(broker, "Sensor-MQTT-01");
        client.connect();

        GeradorSimulado gerador = new GeradorSimulado();
        ObjectMapper mapper = new ObjectMapper();

        while (true) {
            gerador.gerarNovosDados();
            
            Metricas m = new Metricas();
            m.setIdDispositivo("SENSOR_MQTT_LAB_01");
            m.setTemperatura(gerador.getTemperatura());
            m.setHumidade(gerador.getHumidade());

            String json = mapper.writeValueAsString(m);
            MqttMessage message = new MqttMessage(json.getBytes());
            message.setQos(1);

            // Publicação assíncrona no tópico [cite: 24, 30]
            client.publish("universidade/metricas", message);
            System.out.println("MQTT: Publicado em 'universidade/metricas'");

            Thread.sleep(8000);
			client.close();
        }

		
    }
}
