package com.learn.paho_mqtt_one.sender.mwe;

import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
/**
 * 
 * 
 * <p>
 * 							description:																			</br>	
 * &emsp;						use Quality of Service (QoS) level 1 to publish messages							</br>	
 * 																													</br>
 *
 *
 * @author laipl
 *
 */
public class TestMain_qos1 {

	public static void main(String[] args) {

        //String topic        = "MQTT Examples";
        String topic        = "sensors/temperature";
        //String content      = "Message from MqttPublishSample";
        //String content      = "ÄãºÃ";
        String content      = "hi_myfriend";
        int qos             = 1;
        //String broker       = "tcp://iot.eclipse.org:1883";
        String brokerUri       = "tcp://localhost:1883";
        //String broker       = "ssl://localhost:8883";
        String clientId     = "JavaSample";
        MemoryPersistence persistence = new MemoryPersistence();
        
        
        //QoS1
        //53144		->	1883	MQTT		Publish Message		hello_nihao
        //1883		->	53144	TCP			ACK
        //1883		->	1883	MQTT		Publish Ack
        //53144		->	1883	TCP			ACK
        //
        try {
            MqttClient sampleClient = new MqttClient(brokerUri, clientId, persistence);
            //
            // server configuration
            MqttConnectionOptions connOpts = new MqttConnectionOptions();
            connOpts.setCleanStart(true);
            //
            // connect
            System.out.println("Connecting to broker: "+brokerUri);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            //
            // create message
            System.out.println("Publishing message: "+content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            // publish message
            sampleClient.publish(topic, message);
            System.out.println("Message published");
            //
            //
            //
            //
            //sampleClient.disconnect();
            //System.out.println("Disconnected");
            //System.exit(0);
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }

}
