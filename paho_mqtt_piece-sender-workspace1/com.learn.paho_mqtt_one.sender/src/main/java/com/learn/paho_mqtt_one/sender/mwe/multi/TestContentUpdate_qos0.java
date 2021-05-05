package com.learn.paho_mqtt_one.sender.mwe.multi;

import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;

public class TestContentUpdate_qos0 {

	public static void main(String[] args) {

        //String topic        = "MQTT Examples";
        String topic        = "sensors/temperature";
        //String content      = "Message from MqttPublishSample";
        String content      = "hello";
        int qos             = 2;
        //String broker       = "tcp://iot.eclipse.org:1883";
        String broker       = "tcp://localhost:1883";
        String clientId     = "JavaSample";
        MemoryPersistence persistence = new MemoryPersistence();
        
        
        //
        // ---------------------------
        // Attention, here assume that:
        // Publisher	(port 53144)
        // Broker		(port 1883)
        // Subscriber	(port 40003)
        // ---------------------------
        //
        //
        // P(Qos0)¡¢S(Qos0) == P(Qos0)¡¢S(Qos1) == P(Qos0)¡¢S(Qos2) 
        //
        //
        //QoS0
        //connect detail about client
        //40003		->	1883	MQTT		Subscribe Request		sensor/temperature
        //1883		->	40003	TCP			ACK
        //1883		->	40003	MQTT		Subscribe Ack
        //40003		->	1883	TCP			ACK
        //
        // ..... (connect detail about server)
        //
        //QoS0 -> broker-> QoS0
        //53144		->	1883	MQTT		Publish Message		hello_1
        //1883		->	53144	TCP			ACK
        //1883		->	40003	MQTT		Publish Message		hello_1
        //40003		->	1883	TCP			ACK
        //
        //53144		->	1883	MQTT		Publish Message		hello_2
        //1883		->	53144	TCP			ACK
        //1883		->	40003	MQTT		Publish Message		hello_2
        //40003		->	1883	TCP			ACK
        //
        //53144		->	1883	MQTT		Publish Message		hello_3
        //1883		->	53144	TCP			ACK
        //1883		->	40003	MQTT		Publish Message		hello_3
        //40003		->	1883	TCP			ACK
        //
        //53144		->	1883	MQTT		Publish Message		hello_4
        //1883		->	53144	TCP			ACK
        //1883		->	40003	MQTT		Publish Message		hello_4
        //40003		->	1883	TCP			ACK
        //
        //
        try {
        	MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
        	//MqttClient sampleClient = new MqttClient(broker, clientId);
        	//
            MqttConnectionOptions connOpts = new MqttConnectionOptions();
            connOpts.setCleanStart(false);
            connOpts.setSessionExpiryInterval(500L);
            //connOpts.setCleanStart(true);

            
            System.out.println("Connecting to broker: "+broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            // System.out.println("Publishing message: "+content);
            
            

            
            MqttMessage message_tmp;
            StringBuffer str_content_tmp = new StringBuffer("");
            for(int i=0; i<=100; i++) {
            	/*
            	if(i==3) {
            		System.out.println("disconnecting");
            		sampleClient.disconnect();
            		Thread.sleep(20000);
            		System.out.println("reconnecting");
            		sampleClient.reconnect();
            		System.out.println("reconnected");
            	}*/
            	
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
                Thread.sleep(10000);
            }
            
            System.out.println("Message published");
            //
            sampleClient.disconnect();
            System.out.println("Disconnected");
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
