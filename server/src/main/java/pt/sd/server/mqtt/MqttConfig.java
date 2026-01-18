package pt.sd.server.mqtt;

import java.util.UUID;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent; 
import org.springframework.context.event.EventListener;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
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
    public MqttPahoMessageDrivenChannelAdapter inbound() {
        String clientId = "server-sub-" + UUID.randomUUID().toString();
        
        MqttPahoMessageDrivenChannelAdapter adapter =
            new MqttPahoMessageDrivenChannelAdapter("tcp://localhost:1883", clientId, "universidade/metricas");
        
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        
        adapter.setAutoStartup(false); 
        
        return adapter;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void iniciarMqttQuandoPronto() {
        System.out.println("🚦 Contexto Spring carregado. A ligar adaptador MQTT...");
        inbound().start();
    }

    // (Handler)
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler(MonitorizacaoService service) {
        return message -> {
            try {
                String payload = (String) message.getPayload();
                System.out.println("MQTT RECEBIDO: " + payload);

                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                
                Metricas metrica = mapper.readValue(payload, Metricas.class);

                // Verificação da Opção 3 (Classe Metricas)
                if (metrica.getIdDispositivo() == null) {
                    System.err.println(" ERRO: ID NULL.");
                    System.err.println("   -> O JSON traz 'id' mas a classe pede 'idDispositivo'?");
                    System.err.println("   -> Solução: Adicione @JsonAlias(\"id\") na classe Metricas.");
                    return; 
                }

                boolean guardou = service.processarMetrica(metrica);
                
                if (guardou) {
                    System.out.println("GRAVADO COM SUCESSO: " + metrica.getIdDispositivo());
                } else {
                    System.out.println("REJEITADO (Dispositivo não registado): " + metrica.getIdDispositivo());
                }
                
            } catch (Exception e) {
                System.err.println("Erro no processamento MQTT: " + e.getMessage());
            }
        };
    }
}