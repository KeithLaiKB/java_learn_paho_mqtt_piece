package com.learn.paho_mqtt_one.mytest1.unittest;

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

import com.learn.paho_mqtt_one.mytest1.MyThreadSleep;

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
    MqttMessage pub_message = null;
    MqttMessage sub_message = null; 
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
    MqttClient pubClient = null;
    MqttClient subClient = null;
    //----------------------------------------------------------
    final Logger LOGGER = LoggerFactory.getLogger(UT_TestQos.class);
	
	//----------------------------------------------------------
	//
	UT_TestQos(){
		System.out.println("constructor");
	}
	
	
	static void datapreparation() {
		
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
	    pub_message = null;
	    sub_message = null; 
		//
		//
		// ------------------------ configure publisher -----------------------
	    try {
			pubClient = new MqttClient(broker, publisher_clientId, pub_persistence);
			MqttConnectionOptions pub_connOpts = new MqttConnectionOptions();
            pub_connOpts.setCleanStart(true);
            //
            //
            System.out.println("publisher Connecting to broker: "+broker);
            pubClient.connect(pub_connOpts);
            System.out.println("publisher Connected");
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    //
	    //
	    //MyThreadSleep.sleep5s();
	    // ------------------------ configure subscriber -----------------------
	    try {
			subClient = new MqttClient(broker, subscriber_clientId, sub_persistence);
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
					sub_message = message;
					//LOGGER.info("message Arrived:\t"+ new String(message.getPayload()));
				}


			});
            //
            System.out.println("subscriber Connecting to broker: "+broker);
            subClient.connect(sub_connOpts);
            System.out.println("subscriber Connected");
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    //
		// sleep main function for providing subscriber and publisher with enough time to connect the broker
		MyThreadSleep.sleep2s();
	}
	
	
	@AfterEach
	void aftersomething() {
		//------------------------------------------------------------------------
		// 
		/* ref:
		* /org.eclipse.paho.mqttv5.client.test/src/test/java/org/eclipse/paho/mqttv5/client/test/BasicSSLTest.java
		*
		for (int i = 0; i < mqttPublisher.length; i++) {
			log.info("Disconnecting...MultiPub" + i);
			mqttPublisher[i].disconnect();
			log.info("Close...");
			mqttPublisher[i].close();
		}
		for (int i = 0; i < mqttSubscriber.length; i++) {
			log.info("Disconnecting...MultiSubscriber" + i);
			mqttSubscriber[i].disconnect();
			log.info("Close...");
			mqttSubscriber[i].close();
		}
				*/		
		// subscriber side
		try {
			
			subClient.disconnect();
			System.out.println("###############################################subscriber disconnected");
			subClient.close();
			System.out.println("###############################################subscriber closed");
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
		// publisher side
		try {
			pubClient.disconnect();
			System.out.println("###############################################publisher disconnected");
			pubClient.close();
			System.out.println("###############################################publisher closed");
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * Qos combination
     * P(Qos0)、S(Qos0) == P(Qos0)、S(Qos1) == P(Qos0)、S(Qos2) 
     * ref: https://blog.csdn.net/qq1623803207/article/details/89518318
	 */
	@Test
	void testQos0() {
		System.out.println("--------------------- testQos0 ----------------------------");
		//
		int pub_qos_tmp = publisher_qos0;
		int sub_qos_tmp = subscriber_qos0;
		//
		//----------------------- subscriber side -----------------------------
		try {
			// --------------------------------------------------
			System.out.println("subsribing message topic: " + topic);
			subClient.subscribe(topic, sub_qos_tmp);
			//
		} catch (MqttException me) {
			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			me.printStackTrace();
		}
		//
		// sleep main function for providing subscriber with enough time to subscribe the broker
		MyThreadSleep.sleep2s();
		//----------------------- publisher side -----------------------------
		try {
			//
            System.out.println("Publishing message: "+content);
            pub_message = new MqttMessage(content.getBytes());
            pub_message.setQos(pub_qos_tmp);
            pubClient.publish(topic, pub_message);
            System.out.println("Message published");
            //
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
		// sleep main function for getting the notification
		MyThreadSleep.sleep10s();
		//
		assertEquals(new String(pub_message.getPayload()),new String(sub_message.getPayload()),"test_canceled_client1");
		//
	}
	
	
	/**
	 * Qos combination
     * P(Qos1)、S(Qos0)
     * ref: https://blog.csdn.net/qq1623803207/article/details/89518318
	 */
	@Test
	void test_pQos1_sQos0() {
		System.out.println("--------------------- test_pQos1_sQos0 ----------------------------");
		//
		int pub_qos_tmp = publisher_qos1;
		int sub_qos_tmp = subscriber_qos0;
		//
		//----------------------- subscriber side -----------------------------
		try {
			// --------------------------------------------------
			System.out.println("subsribing message topic: " + topic);
			subClient.subscribe(topic, sub_qos_tmp);
			//
		} catch (MqttException me) {
			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			me.printStackTrace();
		}
		//
		// sleep main function for providing subscriber with enough time to subscribe the broker
		MyThreadSleep.sleep2s();
		//----------------------- publisher side -----------------------------
		try {
			//
            System.out.println("Publishing message: "+content);
            pub_message = new MqttMessage(content.getBytes());
            pub_message.setQos(pub_qos_tmp);
            pubClient.publish(topic, pub_message);
            System.out.println("Message published");
            //
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
		// sleep main function for getting the notification
		MyThreadSleep.sleep10s();
		//
		assertEquals(new String(pub_message.getPayload()),new String(sub_message.getPayload()),"test_canceled_client1");
		//
	}	


	/**
	 * Qos combination
     * P(Qos1)、S(Qos1) == P(Qos1)、S(Qos2)
     * ref: https://blog.csdn.net/qq1623803207/article/details/89518318
	 */
	@Test
	void test_pQos1_sQos1() {
		System.out.println("--------------------- test_pQos1_sQos1 ----------------------------");
		//
		int pub_qos_tmp = publisher_qos1;
		int sub_qos_tmp = subscriber_qos1;
		//
		//----------------------- subscriber side -----------------------------
		try {
			// --------------------------------------------------
			System.out.println("subsribing message topic: " + topic);
			subClient.subscribe(topic, sub_qos_tmp);
			//
		} catch (MqttException me) {
			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			me.printStackTrace();
		}
		//
		// sleep main function for providing subscriber with enough time to subscribe the broker
		MyThreadSleep.sleep2s();
		//----------------------- publisher side -----------------------------
		try {
			//
            System.out.println("Publishing message: "+content);
            pub_message = new MqttMessage(content.getBytes());
            pub_message.setQos(pub_qos_tmp);
            pubClient.publish(topic, pub_message);
            System.out.println("Message published");
            //
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
		// sleep main function for getting the notification
		MyThreadSleep.sleep10s();
		//
		assertEquals(new String(pub_message.getPayload()),new String(sub_message.getPayload()),"test_canceled_client1");
		//
	}
	
	
	/**
	 * Qos combination
     * P(Qos2)、S(Qos0)
     * ref: https://blog.csdn.net/qq1623803207/article/details/89518318
	 */
	@Test
	void test_pQos2_sQos0() {
		System.out.println("--------------------- test_pQos2_sQos0 ----------------------------");
		//
		int pub_qos_tmp = publisher_qos2;
		int sub_qos_tmp = subscriber_qos0;
		//
		//----------------------- subscriber side -----------------------------
		try {
			// --------------------------------------------------
			System.out.println("subsribing message topic: " + topic);
			subClient.subscribe(topic, sub_qos_tmp);
			//
		} catch (MqttException me) {
			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			me.printStackTrace();
		}
		//
		// sleep main function for providing subscriber with enough time to subscribe the broker
		MyThreadSleep.sleep2s();
		//----------------------- publisher side -----------------------------
		try {
			//
            System.out.println("Publishing message: "+content);
            pub_message = new MqttMessage(content.getBytes());
            pub_message.setQos(pub_qos_tmp);
            pubClient.publish(topic, pub_message);
            System.out.println("Message published");
            //
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
		// sleep main function for getting the notification
		MyThreadSleep.sleep10s();
		//
		assertEquals(new String(pub_message.getPayload()),new String(sub_message.getPayload()),"test_canceled_client1");
		//
	}

	
	/**
	 * Qos combination
     * P(Qos2)、S(Qos1)
     * ref: https://blog.csdn.net/qq1623803207/article/details/89518318
	 */
	@Test
	void test_pQos2_sQos1() {
		System.out.println("--------------------- test_pQos2_sQos1 ----------------------------");
		//
		int pub_qos_tmp = publisher_qos2;
		int sub_qos_tmp = subscriber_qos1;
		//
		//----------------------- subscriber side -----------------------------
		try {
			// --------------------------------------------------
			System.out.println("subsribing message topic: " + topic);
			subClient.subscribe(topic, sub_qos_tmp);
			//
		} catch (MqttException me) {
			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			me.printStackTrace();
		}
		//
		// sleep main function for providing subscriber with enough time to subscribe the broker
		MyThreadSleep.sleep2s();
		//----------------------- publisher side -----------------------------
		try {
			//
            System.out.println("Publishing message: "+content);
            pub_message = new MqttMessage(content.getBytes());
            pub_message.setQos(pub_qos_tmp);
            pubClient.publish(topic, pub_message);
            System.out.println("Message published");
            //
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
		// sleep main function for getting the notification
		MyThreadSleep.sleep10s();
		//
		assertEquals(new String(pub_message.getPayload()),new String(sub_message.getPayload()),"test_canceled_client1");
		//
	}
	
	
	/**
	 * Qos combination
     * P(Qos2)、S(Qos2)
     * ref: https://blog.csdn.net/qq1623803207/article/details/89518318
	 */
	@Test
	void test_pQos2_sQos2() {
		System.out.println("--------------------- test_pQos2_sQos2 ----------------------------");
		//
		int pub_qos_tmp = publisher_qos2;
		int sub_qos_tmp = subscriber_qos2;
		//
		//----------------------- subscriber side -----------------------------
		try {
			// --------------------------------------------------
			System.out.println("subsribing message topic: " + topic);
			subClient.subscribe(topic, sub_qos_tmp);
			//
		} catch (MqttException me) {
			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			me.printStackTrace();
		}
		//
		// sleep main function for providing subscriber with enough time to subscribe the broker
		MyThreadSleep.sleep2s();
		//----------------------- publisher side -----------------------------
		try {
			//
            System.out.println("Publishing message: "+content);
            pub_message = new MqttMessage(content.getBytes());
            pub_message.setQos(pub_qos_tmp);
            pubClient.publish(topic, pub_message);
            System.out.println("Message published");
            //
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
		// sleep main function for getting the notification
		MyThreadSleep.sleep10s();
		//
		assertEquals(new String(pub_message.getPayload()),new String(sub_message.getPayload()),"test_canceled_client1");
		//
	}	
	
}
