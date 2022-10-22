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
/**
 * 
 * 
 * <p>
 * 							description:																			</br>	
 * &emsp;						It would use the authentication(user name and password).							</br>
 * &emsp;&emsp;						because some broker(like mosquitto, etc) needs authentication, 					</br>
 * &emsp;&emsp;						if your client is not in the same local machine where your broker is deployed.	</br>	
 * &emsp;						It uses qos0.																		</br>
 * &emsp;																											</br>
 * 																													</br>
 * 
 * </p>
 * 
 * @author laipl
 *
 */
public class Con_TestMain_TestCleanStart_auth_qos0 {

	public static void main(String[] args) {


        String topic        = "sensors/temperature";

        //String content      = "receiver";
        int qos             = 0;
        
        String broker       = "tcp://localhost:1883";
        
        String clientId     = "JavaSample_recver";
        
        String myuserName	= "IamPublisherOne";
        String mypwd		= "123456";
        

        final Logger LOGGER = LoggerFactory.getLogger(Con_TestMain_TestCleanStart_auth_qos0.class);
        //
        //
        try {
        	// create mqtt client
            MqttClient sampleClient = new MqttClient(broker, clientId, new MemoryPersistence());
            //MqttClient sampleClient = new MqttClient(broker, clientId);
        	//
        	// -----------------------set connection options-------------------------
        	// 
            MqttConnectionOptions connOpts = new MqttConnectionOptions();
            //
            //
            // ------------------
            // authentication
            //
            connOpts.setUserName(myuserName);
            connOpts.setPassword(mypwd.getBytes());
            //
            // ------------------
            // set persistence
            connOpts.setCleanStart(false);
            connOpts.setSessionExpiryInterval(500L);
            //
            //connOpts.setCleanStart(true);
            //
            // -------------------------------------------------------------------------
            // -----------------------set handler for asynchronous request--------------
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
            // -------------------------------------------------------------------------
            // ---------------- to connect and to subscribe ----------------------------
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
            System.out.println("wow_hello");
            //
            //
            //------------------------------------------------------
            
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
