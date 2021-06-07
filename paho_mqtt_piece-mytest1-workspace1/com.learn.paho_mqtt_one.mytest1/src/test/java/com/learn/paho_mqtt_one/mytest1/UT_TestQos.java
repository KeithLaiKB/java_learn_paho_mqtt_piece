package com.learn.paho_mqtt_one.mytest1;

import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class UT_TestQos {

    String broker       			= "tcp://127.0.0.1:1883";
    //
	//---------------- publisher settings field ----------------
    int publisher_qos0             		= 0;
    int publisher_qos1             		= 1;
    int publisher_qos2             		= 2;
    String publisher_clientId     		= "JavaSample";
    MemoryPersistence pub_persistence 	= new MemoryPersistence();
	//----------------------------------------------------------
    //------------------ publisher data field ------------------
    //
    String topic        			= "sensors/temperature";
    String content      			= "hi_myfriend";
	//
    //----------------------------------------------------------
    //----------------------------------------------------------
    //
	//---------------- subscriber settings field ---------------
    int subscriber_qos0             		= 0;
    int subscriber_qos1             		= 1;
    int subscriber_qos2             		= 2;
    String subscriber_clientId     		= "JavaSample_revcevier";
    MemoryPersistence sub_persistence 	= new MemoryPersistence();
	//----------------------------------------------------------
    //----------------------------------------------------------
    final Logger LOGGER = LoggerFactory.getLogger(UT_TestQos.class);
	
	//----------------------------------------------------------
	//
	UT_TestQos(){
		System.out.println("constructor");
	}
	
	
	static void datapreparation() {
		/*
		// set data vo to test
		DtoFruit dtoFruit1 = new DtoFruit();
		dtoFruit1.setName("i am apple");
		dtoFruit1.setWeight(23.666);
		//
		//
		// transform the vo into json
		objectMapper = new ObjectMapper();
		dtoFruit1AsString = new String("");
		//
		try {
			dtoFruit1AsString = objectMapper.writeValueAsString(dtoFruit1);
			// resp = client1.post(dtoFruit1AsString, MediaTypeRegistry.APPLICATION_JSON);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//
		//
		 */

	}
	//
	//
	//----------------------------------------------------------
	//---------------------start test---------------------------
	

	@BeforeAll
	static void preparation() {
		datapreparation();
	}

	
	@BeforeEach
	void beforesomething() {
		System.out.println("---------------------------------------------------------");
		//

		//
		// -----------configure server-----------------------
		// new server
		
		//
		// -----------start server-----------------------
		System.out.println("starting server");

		System.out.println("started server");
		//
		//----------------------------------------------------
		//--------------------- client1 ----------------------
		//
		// new client
		
		
		// ----------- client1 observe -----------
		System.out.println("+++++ sending request +++++");

		System.out.println("++++++ sent request ++++++");
		//----------------------------------------

	}
	
	
	@AfterEach
	void aftersomething() {
		// server side
		
		// client side
		
        //MyThreadSleep.sleep20s();
		System.out.println("###############################################server1.destroy");
	}
	
	
	
	/**
	 * Qos combination
     * P(Qos0)¡¢S(Qos0) == P(Qos0)¡¢S(Qos1) == P(Qos0)¡¢S(Qos2) 
     * ref: https://blog.csdn.net/qq1623803207/article/details/89518318
	 */
	@Test
	void testQos0() {
		System.out.println("--------------------- testDelete_syn_then_observe_sameresc ----------------------------");
		//
		//----------------------- subscriber side -----------------------------
		try {
            MqttClient subClient = new MqttClient(broker, subscriber_clientId, sub_persistence);
            MqttConnectionOptions sub_connOpts = new MqttConnectionOptions();
            // don't save the information during disconnected from subscribed status
            sub_connOpts.setCleanStart(true);
            //
            subClient.setCallback(new MqttCallback() {

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
            subClient.connect(sub_connOpts);
            System.out.println("Connected");
            System.out.println("subsribing message topic: " + topic);
            //--------------------------------------------------
            subClient.subscribe(topic, subscriber_qos0);		
            MyThreadSleep.sleep20s();

		} 
		catch(MqttException me) {
			System.out.println("reason "+me.getReasonCode());
			System.out.println("msg "+me.getMessage());
			System.out.println("loc "+me.getLocalizedMessage());
			System.out.println("cause "+me.getCause());
			System.out.println("excep "+me);
			me.printStackTrace();
		}
		
		//----------------------- publisher side -----------------------------
		try {
            MqttClient pubClient = new MqttClient(broker, publisher_clientId, pub_persistence);
            MqttConnectionOptions pub_connOpts = new MqttConnectionOptions();
            pub_connOpts.setCleanStart(true);
            System.out.println("Connecting to broker: "+broker);
            pubClient.connect(pub_connOpts);
            System.out.println("Connected");
            System.out.println("Publishing message: "+content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(publisher_qos0);
            pubClient.publish(topic, message);
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
        }
		// sleep main function for getting the notification
		MyThreadSleep.sleep20s();
		//------------------------------------------------------------------------
		//

        //
		
        System.out.println("###############################################end");

	}
	
	
	
}
