package com.learn.paho_mqtt_one.receiver.mwe.auth;


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
 * &emsp;						It uses qos1.																		</br>
 * &emsp;						in this class, it just change qos0 to qos1											</br>
 * 																													</br>
 * 
 * </p>
 * 
 * 
 * @author laipl
 *
 */
public class TestMain_TestCleanStart_auth_qos1_copy {

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
        
        String myuserName	= "IamPublisherOne";
        String mypwd		= "123456";
        
        MemoryPersistence persistence = new MemoryPersistence();

        final Logger LOGGER = LoggerFactory.getLogger(TestMain_TestCleanStart_auth_qos1_copy.class);
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
            //
            // ?????? setCleanStart(false) ?????????: 
            // ???????????? 	?????????		???	disconnect ??????  reconnect 
            // ?????? ??? 		????????? 	?????????  disconnect ??? reconnect ?????? 	?????????  ??????????????? ???????????????
            // ??????
            // publishing client 	?????? 		1	???	broker
            // subscribing client	??????		1	???	broker
            // publishing client 	?????? 		2	???	broker
            // subscribing client	??????		2	???	broker
            // subscribing client	disconnect
            // publishing client	??????		3	???	broker
            // publishing client	??????		4	???	broker
            // publishing client	??????		5	???	broker
            // subscribing client	reconnect
            // subscribing client	??????		3	???	broker
            // subscribing client	??????		4	???	broker
            // subscribing client	??????		5	???	broker
            //
            // publishing client	??????		6	???	broker
            // subscribing client	??????		6	???	broker
            //
            // ???????????? ???subscribing client 
            // 		?????????????????? 1 2 3 4 5 6 (?????? ???????????????????????????(setSessionExpiryInterval) ????????????, ?????????????????????????????????)
            //
            // ??????setCleanStart(true) ?????????:
            // ???????????? ???subscribing client 
            //		?????????????????? 1 2 6
            //
            // ????????? publishing client ?????????????????? 	connOpts.setCleanStart(false) ????????????	setSessionExpiryInterval
            // ?????????????????? publishing client ????????? ?????? connOpts.setCleanStart(true)  ????????????
            connOpts.setCleanStart(false);
            // ?????? ????????? ???????????? ??????????????????, ????????? ???, 
            // ?????????????????????, ???????????? 0s, ???????????? subscribing client ?????????????????? 1 2 6 ?????????  1 2 3 4 5 6
            // ?????? ?????? ??? disconnect ????????? ????????????, ????????? reconnect?????? ???????????? ??????????????? 3 4 5???
            // ??????????????????????????? reconnect ?????? publishing client ?????????6,
            // ???????????????????????????????????????, ??????????????????subscribe
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
            //
            //
            System.out.println("wow_hello");
            //
            //
            //------------------------------------------------------
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
            //
            //
            //
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
        }
    }
}
