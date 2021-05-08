package com.learn.paho_mqtt_one.receiver.mwe.auth.concise;


import java.util.Scanner;

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

public class TestMain_TestCleanStart_auth_qos0 {

	public static void main(String[] args) {


        String topic        = "sensors/temperature";

        //String content      = "receiver";
        int qos             = 0;
        
        String broker       = "tcp://localhost:1883";
        
        String clientId     = "JavaSample_revcevier";
        
        String myuserName	= "IamPublisherOne";
        String mypwd		= "123456";
        

        final Logger LOGGER = LoggerFactory.getLogger(TestMain_TestCleanStart_auth_qos0.class);
        //
        //
        try {
        	// create mqtt client
            MqttClient sampleClient = new MqttClient(broker, clientId, new MemoryPersistence());
            //MqttClient sampleClient = new MqttClient(broker, clientId);
        	//
            //------------------------
            // set option
            MqttConnectionOptions connOpts = new MqttConnectionOptions();
            //
            //
            // set username and pwd
            connOpts.setUserName(myuserName);
            connOpts.setPassword(mypwd.getBytes());
            //
            //
            //
            // 如果 setCleanStart(false) 意味着: 
            // 你想要让 	订阅者		在	disconnect 之后  reconnect 
            // 此外 该 		订阅者 	能够把  disconnect 到 reconnect 期间 	发布者  发送的消息 都全部获得
            // 例如
            // publishing client 	发送 		1	到	broker
            // subscribing client	接受		1	从	broker
            // publishing client 	发送 		2	到	broker
            // subscribing client	接受		2	从	broker
            // subscribing client	disconnect
            // publishing client	发送		3	到	broker
            // publishing client	发送		4	到	broker
            // publishing client	发送		5	到	broker
            // subscribing client	reconnect
            // subscribing client	接受		3	从	broker
            // subscribing client	接受		4	从	broker
            // subscribing client	接受		5	从	broker
            //
            // publishing client	发送		6	到	broker
            // subscribing client	接受		6	从	broker
            //
            // 也就是说 该subscribing client 
            // 		一共可以接受 1 2 3 4 5 6 (假设 设置的会话过期时间(setSessionExpiryInterval) 足够的长, 能够保存所有的离线信息)
            //
            // 如果setCleanStart(true) 意味着:
            // 也就是说 该subscribing client 
            //		一共可以接受 1 2 6
            //
            // 我发现 publishing client 可以不用设置 	connOpts.setCleanStart(false) 和下面的	setSessionExpiryInterval
            // 而且我还发现 publishing client 就算是 设置 connOpts.setCleanStart(true)  也没关系
            connOpts.setCleanStart(false);
            // 注意 订阅者 还要设置 会话过期时间, 单位是 秒, 
            // 如果不设置的话, 它默认是 0s, 则会导致 subscribing client 一共可以接受 1 2 6 而不是  1 2 3 4 5 6
            // 注意 如果 你 disconnect 超过了 这个时间, 那么你 reconnect以后 就没办法 获取中间的 3 4 5，
            // 并且你也没办法获取 reconnect 后面 publishing client 发送的6,
            // 此时如果你还想获得订阅信息, 你还需要重新subscribe
            connOpts.setSessionExpiryInterval(500L);
            //
            //connOpts.setCleanStart(true);
            //------------------------
            //
            //
            sampleClient.setCallback(new MqttCallback() {

				@Override
				public void disconnected(MqttDisconnectResponse disconnectResponse) {
					// TODO Auto-generated method stub
					//System.out.println("mqtt disconnected");
					//
					//LOGGER.info("mqtt disconnected:"+disconnectResponse.getReturnCode()+"//"+disconnectResponse.getReasonString());
					LOGGER.info("mqtt disconnected:"+disconnectResponse.toString());
					
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
            //
            //
            // connect
            System.out.println("Connecting to broker: "+broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            System.out.println("subsribing message topic: " + topic);
            //
            //
            // subscribe
            sampleClient.subscribe(topic,qos);
            //
            //
            //
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
            //
            System.out.println("wow_hello");
            //
            //
            //
            //
            //sampleClient.disconnect();
            //System.out.println("Disconnected");
            //System.exit(0);
            //
            in.close();
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
