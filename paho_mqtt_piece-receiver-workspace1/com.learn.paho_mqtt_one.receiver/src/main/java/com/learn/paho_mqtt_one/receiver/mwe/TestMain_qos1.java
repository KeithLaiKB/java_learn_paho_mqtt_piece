package com.learn.paho_mqtt_one.receiver.mwe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import org.eclipse.paho.mqttv5.client.IMqttDeliveryToken;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestMain_qos1 {

	public static void main(String[] args) {

        //String topic        = "MQTT Examples";
        String topic        = "sensors/temperature";
        //String content      = "Message from MqttPublishSample";
        String content      = "receiver";
        int qos             = 1;
        //String broker       = "tcp://iot.eclipse.org:1883";
        String broker       = "tcp://localhost:1883";
        //String clientId     = "JavaSample";
        String clientId     = "JavaSample_revcevier";
        MemoryPersistence persistence = new MemoryPersistence();
        //
        final Logger LOGGER = LoggerFactory.getLogger(TestMain_qos1.class);

        //
        // ---------------------------
        // Attention, here assume that:
        // Publisher	(port 53144)
        // Broker		(port 1883)
        // Subscriber	(port 40003)
        // ---------------------------
        //
        // Qos组合
        // 1. P(Qos1)、S(Qos1) == P(Qos1)、S(Qos2)
        // 2. P(Qos1)、S(Qos0)
        //
        //
        //QoS
        //connect detail about client
        //40003		->	1883	MQTT		Subscribe Request		sensor/temperature
        //1883		->	40003	TCP			ACK
        //1883		->	40003	MQTT		Subscribe Ack
        //40003		->	1883	TCP			ACK
        //
        // ..... (connect detail about server)
        //
        //QoS1 -> broker-> QoS1
        //53144		->	1883	MQTT		Publish Message		hello_nihao				pub 	-> broker
        //1883		->	53144	TCP			ACK
        //
        //1883		->	40003	MQTT		Publish Message		hello_nihao				broker 	-> sub
        //40003		->	1883	TCP			ACK
        //
        //40003		->	1883	MQTT		Publish Ack									sub 	-> broker 
        //1883		->	40003	TCP			ACK
        //
        //1883		->	53144	MQTT		Publish Ack									broker 	-> pub 
        //53144		->	1883	TCP			ACK
        //
        //
        //++++++++++++++++++++++++++++++++++++++++
        // 其实 P(Qos1)、S(Qos1) == P(Qos1)、S(Qos2)  可能你会疑问  既然相同为什么 这两块东西的 数据包顺序不一样
        // 但注意 qos 主要作用在  pub和 broker 之间 ,   sub和 broker 之间
        // 所以
        // 我们 QoS1 -> broker-> QoS2 的 数据包 顺序  也有可能 和  QoS1 -> broker-> QoS1 一样
        // 同样的
        // 我们 QoS1 -> broker-> QoS1 的 数据包 顺序  也有可能 和  QoS1 -> broker-> QoS2 一样
        //
        //QoS1 -> broker-> QoS2
        //53144		->	1883	MQTT		Publish Message		hello_nihao				pub 	-> broker
        //1883		->	53144	TCP			ACK
        //1883		->	53144	MQTT		Publish Ack									broker 	-> pub 
        //53144		->	1883	TCP			ACK
        //
        //1883		->	40003	MQTT		Publish Message		hello_nihao				broker 	-> sub
        //40003		->	1883	TCP			ACK
        //40003		->	1883	MQTT		Publish Ack									sub 	-> broker 
        //1883		->	40003	TCP			ACK
        //
        //
        //++++++++++++++++++++++++++++++++++++++++
        //QoS1 -> broker-> QoS0
        //53144		->	1883	MQTT		Publish Message		hello_nihao				pub 	-> broker
        //1883		->	53144	TCP			ACK
        //
        //1883		->	40003	MQTT		Publish Message		hello_nihao				broker 	-> sub
        //40003		->	1883	TCP			ACK
        //
        //1883		->	53144	MQTT		Publish Ack									broker 	-> pub
        //53144		->	1883	TCP			ACK

        //
        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectionOptions connOpts = new MqttConnectionOptions();
            // don't save the information during disconnected from subscribed status
            connOpts.setCleanStart(true);
            //
            sampleClient.setCallback(new MqttCallback() {

				@Override
				public void disconnected(MqttDisconnectResponse disconnectResponse) {
					// TODO Auto-generated method stub
					//System.out.println("mqtt disconnected");
					//
					LOGGER.info("mqtt disconnected");
					
					
				}

				@Override
				public void mqttErrorOccurred(MqttException exception) {
					// TODO Auto-generated method stub
					//System.out.println("mqtt error occurred");
					//
					LOGGER.info("mqtt error occurred");
					
				}

				@Override
				public void deliveryComplete(IMqttToken token) {
					// TODO Auto-generated method stub
					//System.out.println("mqtt delivery complete");
					//
					LOGGER.info("mqtt delivery complete");
				}

				@Override
				public void connectComplete(boolean reconnect, String serverURI) {
					// TODO Auto-generated method stub
					//System.out.println("mqtt connect complete");
					//
					LOGGER.info("mqtt connect complete");
				}

				@Override
				public void authPacketArrived(int reasonCode, MqttProperties properties) {
					// TODO Auto-generated method stub
					//System.out.println("mqtt auth Packet Arrived");
					//
					LOGGER.info("mqtt auth Packet Arrived");
				}

				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					// TODO Auto-generated method stub
					System.out.println("message Arrived:\t" + new String(message.getPayload()));
					//
					//LOGGER.info("message Arrived:\t"+ new String(message.getPayload()));
				}


			});
            
            
            System.out.println("Connecting to broker: "+broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            System.out.println("subsribing message topic: " + topic);

            sampleClient.subscribe(topic,qos);
            
            
            //
            //
            //----------------------------------------------
            Scanner in =new Scanner(System.in) ;
            int int_choice = 0;
            while(int_choice!=-1) {
            	System.out.println("here is the choice:");
            	System.out.println("-1: to exit");
            	System.out.println("1: to disconnect broker");
            	System.out.println("2: to reconnect broker");
            	System.out.println("3: to unsubscribe");
            	System.out.println("4: to subscribe");
            	System.out.println("enter the choice:");
            	// input
            	int_choice = in.nextInt();
            	if(int_choice==-1) {
            		//System.exit(0);
            		break;
            	}
            	else if(int_choice==1) {
            		sampleClient.disconnect();
            		System.out.println("disconnected broker");
            	}
            	else if(int_choice==2) {
            		sampleClient.reconnect();
            		System.out.println("reconnect broker");
            	}
            	else if(int_choice==3) {
            		sampleClient.unsubscribe(topic);
            		System.out.println("unsubscribed topic");
            	}
            	else if(int_choice==4) {
            		sampleClient.subscribe(topic,qos);
            		System.out.println("subscribed topic");
            	}
            }
            
            System.out.println("wow_hello");
            
            
            
            
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
