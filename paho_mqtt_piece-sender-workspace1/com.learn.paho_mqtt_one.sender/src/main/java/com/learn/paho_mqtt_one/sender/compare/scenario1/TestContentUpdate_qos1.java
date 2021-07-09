package com.learn.paho_mqtt_one.sender.compare.scenario1;

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
 * &emsp;						use different values to publish message each time									</br>	
 * &emsp; 							with Quality of Service (QoS) level 0 											</br>
 * 																													</br>
 *
 * @author laipl
 *
 */
public class TestContentUpdate_qos1 {

	public static void main(String[] args) {

        //String topic      = "MQTT Examples";
        String topic        = "sensors/temperature";
        //String content    = "Message from MqttPublishSample";
        String content      = "hello";
        int qos             = 1;
        //String broker    	= "tcp://iot.eclipse.org:1883";
        String brokerUri    = "tcp://localhost:1883";
        String clientId     = "JavaSample";
        MemoryPersistence persistence = new MemoryPersistence();
        
        

        try {
        	MqttClient sampleClient = new MqttClient(brokerUri, clientId, persistence);
        	//MqttClient sampleClient = new MqttClient(broker, clientId);
        	//
            MqttConnectionOptions connOpts = new MqttConnectionOptions();
            //connOpts.setCleanStart(false);
            //connOpts.setSessionExpiryInterval(500L);
            connOpts.setCleanStart(true);

            
            System.out.println("Connecting to broker: "+brokerUri);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            // System.out.println("Publishing message: "+content);
            
            

            
            MqttMessage message_tmp;
            StringBuffer str_content_tmp = new StringBuffer("");
            for(int i=0; i<=6; i++) {
            	
            	//str_content_tmp = content +":"+(i+1);
            	str_content_tmp.delete(0, str_content_tmp.length()-1+1);
            	str_content_tmp.append(content +":"+(i+1));
            	//
            	message_tmp = new MqttMessage(str_content_tmp.toString().getBytes());
            	message_tmp.setQos(qos);
            	//
            	try {
            		if(sampleClient.isConnected()==false) {
            			sampleClient.reconnect();
            		}
                	System.out.println("Publishing message: "+str_content_tmp);
                    sampleClient.publish(topic, message_tmp);
            	}
            	catch(MqttException me) {
            		me.printStackTrace();
            	}
                //
                Thread.sleep(5000);
            }
            
            System.out.println("Message published");
            //
            //
            sampleClient.disconnect();
            System.out.println("Disconnected");
            sampleClient.close();
            System.out.println("closed");
            System.exit(0);
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
