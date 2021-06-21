package com.learn.paho_mqtt_one.mytest1.unittest.withauth;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.MqttSecurityException;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.learn.paho_mqtt_one.mytest1.MyThreadSleep;

class TestMain_Auth_MsqtOffl_MsqtOnl {

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
    //
    //String arr_content_send[] 	= {"hi_myfriend0","hi_myfriend1","hi_myfriend2","hi_myfriend3","hi_myfriend4","hi_myfriend5"};
    //String arr_content_recv[] 	= new String[arr_content_send.length]; 
    
    List<String> lst_content_send = new ArrayList<String>();
    List<String> lst_content_recv = new ArrayList<String>();
    //
    int content_num_send = 25;
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
    static MqttClient pubClient = null;
    static MqttClient subClient = null;
    //
    MqttConnectionOptions pub_connOpts = null;
    MqttConnectionOptions sub_connOpts = null;
    //
    String myuserName	= "IamPublisherOne";
    String mypwd		= "123456";
    //
	Runnable serverRun_tmp = null;
	Thread thread_pub1 = null;
	//
	ExecutorService myPubThread_executor1 = null;
	ExecutorService mySubThread_executor1 = null;
    //----------------------------------------------------------
    final Logger LOGGER = LoggerFactory.getLogger(TestMain_Auth_MsqtOffl_MsqtOnl.class);
	//----------------------------------------------------------
	//
	TestMain_Auth_MsqtOffl_MsqtOnl(){
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
	    myPubThread_executor1 = Executors.newFixedThreadPool(1);
	    mySubThread_executor1 = Executors.newFixedThreadPool(1);
		//
		// ------------------------ configure publisher -----------------------
	    try {
			pubClient = new MqttClient(broker, publisher_clientId, pub_persistence);
			pub_connOpts = new MqttConnectionOptions();
            pub_connOpts.setCleanStart(true);
            //
            // authentication
            //
            // https://mosquitto.org/man/mosquitto-conf-5.html
            // for mosquitto, anonymous log in is just allowed in local machine
            // however, gernerally, the broker is deployed in the server, so the client would not in the same machine
            pub_connOpts.setUserName(myuserName);
            pub_connOpts.setPassword(mypwd.getBytes());
            //
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
            sub_connOpts = new MqttConnectionOptions();
            // don't save the information during disconnected from subscribed status
            //sub_connOpts.setCleanStart(false);
            //sub_connOpts.setSessionExpiryInterval(500L);
            //
            //
            // authentication
            //
            // https://mosquitto.org/man/mosquitto-conf-5.html
            // for mosquitto, anonymous log in is just allowed in local machine
            // however, gernerally, the broker is deployed in the server, so the client would not in the same machine
            sub_connOpts.setUserName(myuserName);
            sub_connOpts.setPassword(mypwd.getBytes());
            //
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
					if(message.getPayload() !=null && message.getPayload().equals("")==false) {
						lst_content_recv.add(new String(message.getPayload()));
						//LOGGER.info("message Arrived:\t"+ new String(message.getPayload()));
					}
				}


			});
            //
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
		
		//
		//
		
		mySubThread_executor1.shutdownNow();
		//
		MyThreadSleep.sleep5s();
		// subscriber side
		try {
			//
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
		myPubThread_executor1.shutdownNow();
		try {
			//
			pubClient.disconnect();
			System.out.println("###############################################publisher disconnected");
			pubClient.close();
			System.out.println("###############################################publisher closed");
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//

	}
	
	
	
	/**
	 * operation:
	 * 1.	sub_connOpts.setCleanStart(true);
	 * 
	 * 2.	after broker restart
	 * 			subscriber need reconnect
	 * 
	 * 
	 * 
	 *  publisher(online)	-------------> 	mosquitto(online)  -------------->	subscriber(online)
	 *  publisher(online) 	----123------> 	mosquitto(online)  -------------->	subscriber(online)
	 *  publisher(online) 	-------------> 	mosquitto(online)  -------------->	subscriber(online)
	 *                     						123
	 *  publisher(online) 	-------------> 	mosquitto(online)  ------123----->	subscriber(online)
	 *  publisher(online) 	-------------> 	mosquitto(online)  ------123----->	subscriber(online)
	 *  																			1 2 3
	 *  

	 *  
	 *  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 *  ++++++++++++++++++++++++++ 			turn off broker			+++++++++++++++++++++++++++++++
	 *  ++++++	因为 (setBufferEnabled(true)) 							使得 broker离线 时    publisher 保存	publisher 	发送不到	broker 		的 9 10 11 12	+++++++
	 *  ++++++	 此外 还需要 在mosquitto.config 中 设置 persistence true		使得 broker离线 时    broker  	保存  	broker 		发送不到	subscriber	的 4 5 6 7 8 	+++++++
	 *  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 *  publisher(online)	-------------> 	mosquitto(offline) -------------->	subscriber(online)
	 *  									   4 5 6 7 8
	 *  publisher(online)	-9-10-11-12--> 	mosquitto(offline) -------------->	subscriber(online)
	 *   		                               4 5 6 7 8
	 *  publisher(online)	-------------> 	mosquitto(offline) -------------->	subscriber(online)
	 *   9 10 11 12                            4 5 6 7 8
	 *   
	 *  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 *  ++++++++++++++++++++++++++ 			turn on broker			+++++++++++++++++++++++++++++++
	 *  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 *  publisher(online)	-------------> 	mosquitto(online) -------------->	subscriber(online)
	 *   9 10 11 12                            4 5 6 7 8
	 *  publisher(online)	------------->  mosquitto(online) -----45678---->	subscriber(online)
	 *   9-10-11-12		                            
	 *  publisher(online)	-9-10-11-12--> 	mosquitto(online) -------------->	subscriber(online)
	 *   		                               										4 5 6 7 8
	 *   
	 *  publisher(online)	-------------> 	mosquitto(online) -------------->	subscriber(online)
	 *  									  9 10 11 12
	 *  publisher(online)	--13-14-15---> 	mosquitto(online) -------------->	subscriber(online)
	 *  									  
	 *  publisher(online)	-------------> 	mosquitto(online) ---9101112---->	subscriber(online)
	 *  							     										9 10 11 12
	 *  
	 *  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 *  ++++++++++++++++++++++++++ 			turn on subscriber			+++++++++++++++++++++++++++
	 *  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 *  publisher(online)	-------------> 	mosquitto(online) -456789101112131415-->subscriber(online)
	 *  publisher(online)	-------------> 	mosquitto(online) --------------------->subscriber(online)
	 *  							     									456789 10 11 12 13 14 15 		
	 * 
	 * 
	 * 
	 * 
	 * 
	 *
	 */
	
	@Test
	void test_brkoffline_subreconnect() {
		System.out.println("--------------------- test_brkoffline_subreconnect ----------------------------");
		//
		//
		int pub_qos_tmp = publisher_qos1;
		int sub_qos_tmp = subscriber_qos1;
		//-----------------------------
		//
        // ------------------
        //
        try {
        	// you need to set publisher setAutomaticReconnect
            // ------------------
            //
        	pub_connOpts.setAutomaticReconnect(true);
            //
            // -------------------------------------------------------------------------
        	//
            System.out.println("publisher Connecting to broker: "+broker);
            pubClient.connect(pub_connOpts);
            System.out.println("publisher Connected");
            //
		} catch (MqttSecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (MqttException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        //
        // -------------------------------------------------------------------------
		//
        try {
        	//
    		// on the basis that the the broker would not shutdown or crash during the process,
    		// let broker remember the subscriber
    		sub_connOpts.setCleanStart(false);
    		// 500 seconds for broker to remember this subscriber
    		sub_connOpts.setSessionExpiryInterval(500L);
    		//
            // ------------------
            //
    		sub_connOpts.setAutomaticReconnect(true);
            //
            // -------------------------------------------------------------------------
    		//
            System.out.println("subscriber Connecting to broker: "+broker);
            subClient.connect(sub_connOpts);
            System.out.println("subscriber Connected");
            //
		} catch (MqttSecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (MqttException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        //
		//---------------------------------------------------------------------
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
		//
		//---------------------------------------------------------------------
		//----------------------- publisher side -----------------------------
		//
		//-------------- thread -------------
		// 用 ExecutorService mythread_executor1 是为了 最后能够 停止这个thread, 
		// 因为我发现 thread.stop 和 thread.interrupt 方法已经deprecated了
		// 	网上推荐的方法使用ExecutorService 来最后停止 
		Runnable serverRun_tmp = new MyServerRun(pub_qos_tmp);
		Thread thread_pub1 = new Thread(serverRun_tmp, "publisher1");
		//
		myPubThread_executor1.submit(thread_pub1);
		//---------------------------
		//
		// sleep main function for getting the some notifications
		// if you set more sleep time, subscriber might receive more notifications
		MyThreadSleep.sleep6s();
		//
		//lst_content_recv.add("kkk");
		//---------------------------------------------------------------------
		//----------------------- main test side ------------------------------
		//
		Scanner in = new Scanner(System.in);
		String str_choice_tmp = null;
		//
		//
		//----------------------- simulate broker crash ------------------------------
		LOGGER.info("please close your broker and press enter here!!!!!!!!!!!!!!!!!!!!!!");
		str_choice_tmp = in.nextLine();
		//----------------------- simulate broker open ------------------------------
		LOGGER.info("please wait for some seconds and open the the broker!!!!!!!!!!!!!!!!!!!!!!");
		//
		//
		//
		MyThreadSleep.sleep35s();
		//
		assertEquals(lst_content_send,lst_content_recv,"test_canceled_client1");
		System.out.println("#######################testend");
		//
	}
	
	
	
	/**
	 * 
	 * 
	 * 
	 *  publisher(online)	-------------> 	mosquitto(online)  -------------->	subscriber(online)
	 *  publisher(online) 	----123------> 	mosquitto(online)  -------------->	subscriber(online)
	 *  publisher(online) 	-------------> 	mosquitto(online)  -------------->	subscriber(online)
	 *                     						123
	 *  publisher(online) 	-------------> 	mosquitto(online)  ------123----->	subscriber(online)
	 *  publisher(online) 	-------------> 	mosquitto(online)  ------123----->	subscriber(online)
	 *  																			1 2 3
	 *  
	 *  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 *  +++++++++++++++++++++++++			turn off subscriber		+++++++++++++++++++++++++++++++
	 *  ++++++	要设置 subscriber 的 setCleantStart(false) 和 interval, 	使得 subscriber 重启 后   broker     仍然记得 这个subscriber 						+++++++
	 *  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 *  publisher(online) 	-------------> 	mosquitto(online)  -------------->	subscriber(offline)
	 *  publisher(online) 	----45678----> 	mosquitto(online)  -------------->	subscriber(offline)
	 *  publisher(online) 	-------------> 	mosquitto(online)  -------------->	subscriber(offline)
	 *  									   4 5 6 7 8
	 *  
	 *  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 *  ++++++++++++++++++++++++++ 			turn off broker			+++++++++++++++++++++++++++++++
	 *  ++++++	因为 (setBufferEnabled(true)) 							使得 broker离线 时    publisher 保存	publisher 	发送不到	broker 		的 9 10 11 12	+++++++
	 *  ++++++	 此外 还需要 在mosquitto.config 中 设置 persistence true		使得 broker离线 时    broker  	保存  	broker 		发送不到	subscriber	的 4 5 6 7 8 	+++++++
	 *  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 *  publisher(online)	-------------> 	mosquitto(offline) -------------->	subscriber(offline)
	 *  									   4 5 6 7 8
	 *  publisher(online)	-9-10-11-12--> 	mosquitto(offline) -------------->	subscriber(offline)
	 *   		                               4 5 6 7 8
	 *  publisher(online)	-------------> 	mosquitto(offline) -------------->	subscriber(offline)
	 *   9 10 11 12                            4 5 6 7 8
	 *   
	 *  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 *  ++++++++++++++++++++++++++ 			turn on broker			+++++++++++++++++++++++++++++++
	 *  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 *  publisher(online)	-------------> 	mosquitto(online) -------------->	subscriber(offline)
	 *   9 10 11 12                            4 5 6 7 8
	 *  publisher(online)	-9-10-11-12--> 	mosquitto(online) -------------->	subscriber(offline)
	 *   		                               4 5 6 7 8
	 *  publisher(online)	-------------> 	mosquitto(online) -------------->	subscriber(offline)
	 *  									 45678 9 10 11 12
	 *  publisher(online)	--13-14-15---> 	mosquitto(online) -------------->	subscriber(offline)
	 *  									 45678 9 10 11 12
	 *  publisher(online)	-------------> 	mosquitto(online) -------------->	subscriber(offline)
	 *  							     45678 9 10 11 12 13 14 15
	 *  
	 *  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 *  ++++++++++++++++++++++++++ 			turn on subscriber			+++++++++++++++++++++++++++
	 *  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 *  publisher(online)	-------------> 	mosquitto(online) -456789101112131415-->subscriber(online)
	 *  publisher(online)	-------------> 	mosquitto(online) --------------------->subscriber(online)
	 *  							     									456789 10 11 12 13 14 15 		
	 * 
	 * 
	 * </p>
	 * 
	 * description:
	 * 		to simulate the condition when the subscriber crashed and rerun the program
	 * 		so the program will rerun which means it needs connect and subscribe after crash
	 * 
	 * you need to set:
	 * 		sub_connOpts.setCleanStart(false);
	 * 		sub_connOpts.setSessionExpiryInterval(500L);
	 * 
	 * and use
	 * 		subClient.connect(sub_connOpts);
	 * 		subClient.subscribe(topic, sub_qos_tmp);
	 * 
	 * 
	 * </p>
	 * 
	 * 
	 */
	
	@Test
	void test_brkoffline_sub_subscribeagain() {
		System.out.println("--------------------- test_brkoffline_sub_subscribeagain ----------------------------");
		//
		//
		int pub_qos_tmp = publisher_qos1;
		int sub_qos_tmp = subscriber_qos1;
		//-----------------------------
		//
        // ------------------
        //
        try {
        	// you need to set publisher setAutomaticReconnect
            // ------------------
            //
        	pub_connOpts.setAutomaticReconnect(true);
            //
            // -------------------------------------------------------------------------
        	//
            System.out.println("publisher Connecting to broker: "+broker);
            pubClient.connect(pub_connOpts);
            System.out.println("publisher Connected");
            //
		} catch (MqttSecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (MqttException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        //
        try {
        	//
    		// on the basis that the the broker would not shutdown or crash during the process,
    		// let broker remember the subscriber
    		sub_connOpts.setCleanStart(false);
    		// 500 seconds for broker to remember this subscriber
    		sub_connOpts.setSessionExpiryInterval(500L);
    		//
            // ------------------
            //
    		sub_connOpts.setAutomaticReconnect(true);
            //
            // -------------------------------------------------------------------------
    		//
            System.out.println("subscriber Connecting to broker: "+broker);
            subClient.connect(sub_connOpts);
            System.out.println("subscriber Connected");
            //
		} catch (MqttSecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (MqttException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        //
		//
		// sleep main function for providing subscriber with enough time to subscribe the broker
		MyThreadSleep.sleep2s();
		//
		//---------------------------------------------------------------------
		//----------------------- publisher side -----------------------------
		//
		//-------------- publisher thread -------------
		// 用 ExecutorService mythread_executor1 是为了 最后能够 停止这个thread, 
		// 因为我发现 thread.stop 和 thread.interrupt 方法已经deprecated了
		// 	网上推荐的方法使用ExecutorService 来最后停止 
		Runnable serverRun_tmp = new MyServerRun(pub_qos_tmp);
		Thread thread_pub1 = new Thread(serverRun_tmp, "publisher1");
		//
		myPubThread_executor1.submit(thread_pub1);
		//---------------------------------------------------------------------
		//----------------------- subscriber side -----------------------------
		//
		//-------------- subscriber thread -------------
		// 虽然不需要专门搞个 线程 或者 线程池 给subscriber
		// 只是因为我打算模拟 subscriber 是在另外一个计算机上运行的
		// 所以 用一个线程模拟 一个计算机, 然后在这个计算机上 运行subscriber
		Runnable subRun_tmp = new MySubRun(pub_qos_tmp);
		Thread thread_sub1 = new Thread(subRun_tmp, "subscriber1");
		//
		mySubThread_executor1.submit(thread_sub1);
		//---------------------------
		//
		// sleep main function for getting the some notifications
		// if you set more sleep time, subscriber might receive more notifications
		MyThreadSleep.sleep6s();
		//
		//lst_content_recv.add("kkk");
		//---------------------------------------------------------------------
		//----------------------- main test side ------------------------------
		//
		Scanner in = new Scanner(System.in);
		String str_choice_tmp = null;
		//
		//
		//----------------------- simulate subscriber crash ------------------------------
		LOGGER.info("simulating subscriber crash");
		mySubThread_executor1.shutdownNow();
		try {
			System.out.println("###############################################subscriber disconnected");
			subClient.disconnect();
			subClient.close();
			System.out.println("###############################################subscriber closed");
		} catch (MqttException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//----------------------- simulate subscriber open ------------------------------
		LOGGER.info("simulating subscriber crashed");
		//
		MyThreadSleep.sleep10s();
		//
		//----------------------- simulate broker crash ------------------------------
		LOGGER.info("please close your broker and press enter here!!!!!!!!!!!!!!!!!!!!!!");
		str_choice_tmp = in.nextLine();
		//----------------------- simulate broker open ------------------------------
		LOGGER.info("please wait for some seconds and open the the broker!!!!!!!!!!!!!!!!!!!!!!");
		//
		//----------------------- simulate subscriber again ------------------------------
		LOGGER.info("simulating subscriber subscribe again");
		try {
			subClient = new MqttClient(broker, subscriber_clientId, sub_persistence);
			//
	        sub_connOpts = new MqttConnectionOptions();
	        sub_connOpts.setUserName(myuserName);
	        sub_connOpts.setPassword(mypwd.getBytes());
	        //
	        //
    		// on the basis that the the broker would not shutdown or crash during the process,
    		// let broker remember the subscriber
    		sub_connOpts.setCleanStart(false);
    		// 500 seconds for broker to remember this subscriber
    		sub_connOpts.setSessionExpiryInterval(500L);
    		//
            // ------------------
            //
    		sub_connOpts.setAutomaticReconnect(true);
            //
            // -------------------------------------------------------------------------
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
					if(message.getPayload() !=null && message.getPayload().equals("")==false) {
						lst_content_recv.add(new String(message.getPayload()));
						//LOGGER.info("message Arrived:\t"+ new String(message.getPayload()));
					}
				}


			});
    		//
    		//
            System.out.println("subscriber Connecting to broker: "+broker);
            subClient.connect(sub_connOpts);
            System.out.println("subscriber Connected");
            //
	        //---------------- thread -------------------
			Runnable subRun_tmp2 = new MySubRun(sub_qos_tmp);
			Thread thread_sub1_tmp2 = new Thread(subRun_tmp2, "sublisher1");
			//
			myPubThread_executor1.submit(thread_sub1_tmp2);
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
		//
		MyThreadSleep.sleep30s();
		//
		//lst_content_recv.add("kkkkk");
		assertEquals(lst_content_send,lst_content_recv,"test_canceled_client1");
		System.out.println("#######################testend");
		//
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 
*  publisher(online)	-------------> 	mosquitto(online)  -------------->	subscriber(online)
	 *  publisher(online) 	----123------> 	mosquitto(online)  -------------->	subscriber(online)
	 *  publisher(online) 	-------------> 	mosquitto(online)  -------------->	subscriber(online)
	 *                     						123
	 *  publisher(online) 	-------------> 	mosquitto(online)  ------123----->	subscriber(online)
	 *  publisher(online) 	-------------> 	mosquitto(online)  ------123----->	subscriber(online)
	 *  																			1 2 3
	 *  
	 *  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 *  +++++++++++++++++++++++++			turn off subscriber		+++++++++++++++++++++++++++++++
	 *  ++++++	要设置 subscriber 的 setCleantStart(false) 和 interval, 	使得 subscriber 重启 后   broker     仍然记得 这个subscriber 						+++++++
	 *  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 *  publisher(online) 	-------------> 	mosquitto(online)  -------------->	subscriber(offline)
	 *  publisher(online) 	----45678----> 	mosquitto(online)  -------------->	subscriber(offline)
	 *  publisher(online) 	-------------> 	mosquitto(online)  -------------->	subscriber(offline)
	 *  									   4 5 6 7 8
	 *  
	 *  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 *  ++++++++++++++++++++++++++ 			turn off broker			+++++++++++++++++++++++++++++++
	 *  ++++++	因为 (setBufferEnabled(true)) 							使得 broker离线 时    publisher 保存	publisher 	发送不到	broker 		的 9 10 11 12	+++++++
	 *  ++++++	 此外 还需要 在mosquitto.config 中 设置 persistence true		使得 broker离线 时    broker  	保存  	broker 		发送不到	subscriber	的 4 5 6 7 8 	+++++++
	 *  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 *  publisher(online)	-------------> 	mosquitto(offline) -------------->	subscriber(offline)
	 *  									   4 5 6 7 8
	 *  publisher(online)	-9-10-11-12--> 	mosquitto(offline) -------------->	subscriber(offline)
	 *   		                               4 5 6 7 8
	 *  publisher(online)	-------------> 	mosquitto(offline) -------------->	subscriber(offline)
	 *   9 10 11 12                            4 5 6 7 8
	 *   
	 *  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 *  ++++++++++++++++++++++++++ 			turn on broker			+++++++++++++++++++++++++++++++
	 *  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 *  publisher(online)	-------------> 	mosquitto(online) -------------->	subscriber(offline)
	 *   9 10 11 12                            4 5 6 7 8
	 *  publisher(online)	-9-10-11-12--> 	mosquitto(online) -------------->	subscriber(offline)
	 *   		                               4 5 6 7 8
	 *  publisher(online)	-------------> 	mosquitto(online) -------------->	subscriber(offline)
	 *  									 45678 9 10 11 12
	 *  publisher(online)	--13-14-15---> 	mosquitto(online) -------------->	subscriber(offline)
	 *  									 45678 9 10 11 12
	 *  publisher(online)	-------------> 	mosquitto(online) -------------->	subscriber(offline)
	 *  							     45678 9 10 11 12 13 14 15
	 *  
	 *  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 *  ++++++++++++++++++++++++++ 			turn on subscriber			+++++++++++++++++++++++++++
	 *  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 *  publisher(online)	-------------> 	mosquitto(online) -456789101112131415-->subscriber(online)
	 *  publisher(online)	-------------> 	mosquitto(online) --------------------->subscriber(online)
	 *  							     									456789 10 11 12 13 14 15 		
	 * 
	 * 
	 * </p>
	 * 
	 * description:
	 * 		to simulate the condition when the subscriber crashed and rerun the program
	 * 		so the program will rerun which means it needs connect and subscribe after crash
	 * 
	 * you need to set:
	 * 		sub_connOpts.setCleanStart(false);
	 * 		sub_connOpts.setSessionExpiryInterval(500L);
	 * 
	 * and use
	 * 		subClient.connect(sub_connOpts);
	 * 		subClient.subscribe(topic, sub_qos_tmp);
	 * 
	 * 
	 * </p>
	 */
	/*
	@Test
	void test_sub_restart() {
		System.out.println("--------------------- test_sub_restart ----------------------------");
		//
		//
		int pub_qos_tmp = publisher_qos1;
		int sub_qos_tmp = subscriber_qos1;
		//-----------------------------
		//
        try {
        	//
    		// on the basis that the the broker would not shutdown or crash during the process,
    		// let broker remember the subscriber
    		sub_connOpts.setCleanStart(false);
    		// 500 seconds for broker to remember this subscriber
    		sub_connOpts.setSessionExpiryInterval(500L);
    		//
    		//
            System.out.println("subscriber Connecting to broker: "+broker);
            subClient.connect(sub_connOpts);
            System.out.println("subscriber Connected");
            //
		} catch (MqttSecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (MqttException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        //
		//---------------------------------------------------------------------
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
		//
		//---------------------------------------------------------------------
		//----------------------- publisher side -----------------------------
		//
		//-------------- thread -------------
		Runnable serverRun_tmp = new MyServerRun(pub_qos_tmp);
		Thread thread_pub1 = new Thread(serverRun_tmp, "publisher1");
		//
		mythread_executor1.submit(thread_pub1);
		//---------------------------
		//
		// sleep main function for getting the some notifications
		// if you set more sleep time, subscriber might receive more notifications
		MyThreadSleep.sleep10s();
		//
		//lst_content_recv.add("kkk");
		//---------------------------
		//
		//---------------------------------------------------------------------
		//----------------------- main test side ------------------------------
		//
		Scanner in = new Scanner(System.in);
		String str_choice_tmp = null;
		//
		//
		//----------------------- simulate subscriber crash ------------------------------
		System.out.println("please press enter to crash your subscriber!!!!!!!!!!!!!!!!!!!!!!");
		str_choice_tmp = in.nextLine();
		try {
			System.out.println("subscriber crashing");
			subClient.disconnect();
			//subClient.close();			// close 好像会把relationship给去掉, 所以应该是disconnect
			System.out.println("subscriber crashed");
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
		//
		MyThreadSleep.sleep5s();
		//----------------------- simulate subscriber open after crash ------------------------------
		System.out.println("please press enter to start your subscriber!!!!!!!!!!!!!!!!!!!!!!");
		str_choice_tmp = in.nextLine();
		try {
			System.out.println("subscriber staring");
			//subClient.reconnect();
			subClient.connect(sub_connOpts);
			subClient.subscribe(topic, sub_qos_tmp);
			System.out.println("subscriber started");
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MyThreadSleep.sleep15s();
		//
		assertEquals(lst_content_send,lst_content_recv,"test_canceled_client1");
		System.out.println("#######################testend");
		//
	}
	*/
	
	class MyServerRun implements Runnable{
		private int qos_tmp = -1;
		public MyServerRun(int qos){
			this.qos_tmp = qos;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//
			StringBuffer str_content_tmp = new StringBuffer("");
			//
			for(int i=0; i<=content_num_send; i++) {
            	//
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("interrupted");
                    break;
                }
				//
				//
	            //str_content_tmp = content +":"+(i+1);
	            str_content_tmp.delete(0, str_content_tmp.length()-1+1);
	            str_content_tmp.append(content +":"+(i+1));
            	//
				MqttMessage message_tmp = null;
            	message_tmp = new MqttMessage(str_content_tmp.toString().getBytes());
            	message_tmp.setQos(qos_tmp);
            	message_tmp.setRetained(false);
            	//
            	try {
                	System.out.println("Publishing message: "+str_content_tmp);
                	pubClient.publish(topic, message_tmp);
                	//
            	}
            	catch(MqttException me) {
            		me.printStackTrace();
            		MyThreadSleep.sleep2s();
            		//
            		// if publish wrong the content will not add in the lst_content_send
            		continue;
            	}
            	// if publish successfully
            	// it need to record what publisher sent for testing
            	lst_content_send.add(str_content_tmp.toString());
                //
            	MyThreadSleep.sleep2s();
            }
			//
			//
			System.out.println("Message published");
		}
		
	}
	
	
	
	
	class MySubRun implements Runnable{
		private int qos_tmp = -1;
		public MySubRun(int qos){
			this.qos_tmp = qos;
		}
		@Override
		public void run() {
			try {
				// --------------------------------------------------
				System.out.println("subsribing message topic: " + topic);
				subClient.subscribe(topic, qos_tmp);
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
		}
		
	}
	
}
