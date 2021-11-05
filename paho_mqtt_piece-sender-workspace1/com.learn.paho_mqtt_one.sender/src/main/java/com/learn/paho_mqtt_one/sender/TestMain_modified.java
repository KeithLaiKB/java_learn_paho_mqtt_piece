package com.learn.paho_mqtt_one.sender;

import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;

public class TestMain_modified {
	/**
	 * 
	 * 
	 * <p>
	 * 							description:																			</br>	
	 * &emsp;						use different value to publish message each time 									</br>	
	 * 																													</br>
	 *
	 *
	 * @author laipl
	 *
	 */
	public static void main(String[] args) {

        //String topic        = "MQTT Examples";
        String topic        = "sensors/temperature";
        //String content      = "Message from MqttPublishSample";
        String content      = "ÄãºÃ";
        int qos             = 2;
        //String broker       = "tcp://iot.eclipse.org:1883";
        String broker       = "tcp://localhost:1883";
        String clientId     = "JavaSample";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
        	MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
        	//MqttClient sampleClient = new MqttClient(broker, clientId);
        	
            MqttConnectionOptions connOpts = new MqttConnectionOptions();
            //connOpts.setCleanStart(false);
            //connOpts.setSessionExpiryInterval(100L);
            //connOpts.setCleanStart(false);
            connOpts.setCleanStart(true);

            
            System.out.println("Connecting to broker: "+broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            // System.out.println("Publishing message: "+content);
            
            

            
            MqttMessage message_tmp;
            String str_content_tmp;
            for(int i=0; i<=1000-1; i++) {
            	//str_content_tmp = content +":"+(i+1);
            	str_content_tmp = content +":"+(i+1);
            	message_tmp = new MqttMessage(str_content_tmp.getBytes());
            	message_tmp.setQos(qos);
            	//
            	System.out.println("Publishing message: "+str_content_tmp);
                sampleClient.publish(topic, message_tmp);
                Thread.sleep(10000);
            }
            
            System.out.println("Message published");
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
        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
