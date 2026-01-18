package pt.sd.server.mqtt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import pt.sd.server.model.Metricas;
import pt.sd.server.service.MonitorizacaoService;

@Configuration
public class MqttConfig {

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() {
        // Subscreve o tópico no broker configurado [cite: 83]
        MqttPahoMessageDrivenChannelAdapter adapter =
            new MqttPahoMessageDrivenChannelAdapter("tcp://localhost:61616", "servidor-subscriber", "universidade/metricas");
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler(MonitorizacaoService service) {
        return message -> {
            try {
                String payload = (String) message.getPayload();
                
                // Converter JSON recebido para o objeto Metricas
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule()); // Para suportar LocalDateTime
                Metricas metrica = mapper.readValue(payload, Metricas.class);

                // Validar e guardar (descarta se o dispositivo não existir) [cite: 95-97]
                service.processarMetrica(metrica);
                
            } catch (Exception e) {
                System.err.println("Erro ao processar mensagem MQTT: " + e.getMessage());
            }
        };
    }
}