package com.learn.paho_mqtt_one.receiver;

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

public class TestMain {

	public static void main(String[] args) {

        //String topic        = "MQTT Examples";
        String topic        = "sensors/temperature";
        //String content      = "Message from MqttPublishSample";
        String content      = "receiver";
        int qos             = 0;
        //String broker       = "tcp://iot.eclipse.org:1883";
        String broker       = "tcp://localhost:1883";
        //String clientId     = "JavaSample";
        String clientId     = "JavaSample_revcevier";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectionOptions connOpts = new MqttConnectionOptions();
            //
            connOpts.setCleanStart(true);
            
            sampleClient.setCallback(new MqttCallback() {

				@Override
				public void disconnected(MqttDisconnectResponse disconnectResponse) {
					// TODO Auto-generated method stub
					System.out.println("mqtt disconnected");
					
				}

				@Override
				public void mqttErrorOccurred(MqttException exception) {
					// TODO Auto-generated method stub
					System.out.println("mqtt error occurred");
				}

				@Override
				public void deliveryComplete(IMqttToken token) {
					// TODO Auto-generated method stub
					System.out.println("mqtt delivery complete");
				}

				@Override
				public void connectComplete(boolean reconnect, String serverURI) {
					// TODO Auto-generated method stub
					System.out.println("mqtt connect complete");
				}

				@Override
				public void authPacketArrived(int reasonCode, MqttProperties properties) {
					// TODO Auto-generated method stub
					System.out.println("mqtt auth Packet Arrived");
				}

				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					// TODO Auto-generated method stub
					System.out.println("message Arrived:\t"
							+ new String(message.getPayload()));
				}


			});
            
            
            System.out.println("Connecting to broker: "+broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            System.out.println("subsribing message topic: " + topic);

            sampleClient.subscribe(topic,qos);
            
            System.out.println("enter to exit!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            Scanner in =new Scanner(System.in) ;
            int int_choice = 0;
            while(int_choice!=-1) {
            	System.out.println("here is the choice:");
            	System.out.println("-1: to exit");
            	System.out.println("0: to unsubscribe");
            	System.out.println("1: to subscribe");
            	System.out.println("enter the choice:");
            	// input
            	int_choice = in.nextInt();
            	if(int_choice==-1) {
            		//System.exit(0);
            		break;
            	}
            	else if(int_choice==0) {
            		sampleClient.unsubscribe(topic);
            		System.out.println("subscribed topic");
            	}
            	else if(int_choice==1) {
            		sampleClient.subscribe(topic,qos);
            		System.out.println("unsubscribed topic");
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
