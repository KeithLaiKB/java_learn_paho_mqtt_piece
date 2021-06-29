package com.learn.paho_mqtt_one.sender.mwe.multi.withauth;

import org.eclipse.paho.mqttv5.client.DisconnectedBufferOptions;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttAsyncClient;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.client.persist.MqttDefaultFilePersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
/**
 * 
 * @author laipl
 *
 *	我想要做到
 *	step1(数据):	publisher 	发送 123
 *	step2(数据):	subscriber 	接受123
 *
 *	step3(操作):	关闭 subscriber 
 *
 *	step4(数据):	publisher 	发送45678
 *  
 *	step5(操作):	关闭 docker mosquitto		!!!!!!!!!!!!!!!!!!!!!
 *
 *	step6(数据):	publisher 	继续发送 9 10 11 12
 *	step7(操作):	然后 启动 docker mosquitto
 *
 *	step7(数据):	然后publisher 继续发送 13 14 15
 *
 *	step8(操作):	然后 启动 subscriber
 *	step9(数据):	然后 subscriber 能接受 
 *								4 5 6 7 8
 *								      和
 *								9 10 11 12
 *								      和
 *								13 14 15
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
 * ++++++++++++
 * 为了让broker保存  45678(因为broker下线了 				broker		没来得及发送给  ------>subscriber) 
 * 
 * 因为中途要 关闭 broker, 那么就  需要 在mosquitto.config 中 设置 persistence true
 * 因为
 * publisher 发送到broker 的消息 , 但subscriber因为中途突然下线 没收到
 * 而这部分 subscriber的信息 broker是需要保存的,
 * 可是broker 也因为关掉了, 但这部分存在broker的消息 会消失
 * 为了使  broker因为中途不小心关机, 仍然能保存 这部分消息, 则需要 在mosquitto.config 中 设置 persistence true 
 * 因为 broker需要保存 		
 * 
 * 因为broker需要记得 subscriber
 * 在这里 还需要设置 subscriber 
 * 	connOpts.setCleanStart(false);
 * 	connOpts.setSessionExpiryInterval(500L);		//500是个时间 你可以随便设置
 * 
 * subscriber关闭后	 重启 		就可以直接获得 45678
 *
 * ++++++++++++
 *	为了让publisher 保存  9 10 11 12(因为broker下线了 	publisher 	没来得及发送给  ------>broker) 
 *
 *  需要让 publisher 设置 (setBufferEnabled(true)), 
 *  当然你不设置, 9 10 11 12 这一片段就会丢失 
 * +++++++++++++++++++++++++++++++++
 * 也就是说 这个例子 聚合了两个功能
 * publisher 记住publisher 			未发送的	(publisher 一直在线, 没有重启)
 * broker    记住broker		重启前 	 未发送的
 * 
 * 你在使用的时候看你需要 选择只添加哪一块, 
 * 	我只是这里给了一个 比较自己常用的 解决方案
 * 		因为 自己希望subscriber能记住 所有的东西
 * 
 * +++++++++++++++++++++++++++++++++
 * 
 * 在这例子的设置当中:
 * 
 * 衍生小case1:
 * 无论 是否  中途 停止publisher, 
 * 只要是这个数据已经停留在broker当中
 * 在这例子的设置当中, 就算是重启后, 即使publisher没有重启
 * 当 subscriber重启后 都能够获得broker中存储的数据
 *
 * +++++++++++++++++++++++++++++++++
 *
 * 由于要设置 DisconnectedBufferOptions
 * MqttClient 这个类比较简单, 无法直接设置
 * 所以 把 MqttClient 改成用 MqttAsyncClient
 * 因此要把
 *      sampleClient.connect(connOpts);										//如果是MqttClient 贼需要这个
 *      改成这个
 *      sampleClient.connect(connOpts, null, null).waitForCompletion(-1); 	//如果是MqttAsyncClient 贼需要这个               
 * 
 *
 */
public class TestMain_Auth_MsqtOffl_MsqtOnl {

	public static void main(String[] args) {

        //String topic      = "MQTT Examples";
        String topic        = "sensors/temperature";
        //String content    = "Message from MqttPublishSample";
        String content      = "hello";
        int qos             = 1;
        //String broker     = "tcp://iot.eclipse.org:1883";
        String brokerUri    = "tcp://localhost:1883";
        String clientId     = "JavaSample";
        
        String myuserName	= "IamPublisherOne";
        String mypwd		= "123456";
        


        try {
        	//MqttClient sampleClient = new MqttClient(brokerUri, clientId, new MemoryPersistence());
        	//MqttAsyncClient sampleClient = new MqttAsyncClient(brokerUri, clientId, new MqttDefaultFilePersistence());
        	MqttAsyncClient sampleClient = new MqttAsyncClient(brokerUri, clientId, new MemoryPersistence());
        	//
        	// -----------------------set connection options-------------------------
        	// 
            MqttConnectionOptions connOpts = new MqttConnectionOptions();
            //connOpts.setCleanStart(false);
            //connOpts.setSessionExpiryInterval(500L);
            connOpts.setCleanStart(true);
            //
            //
            // ------------------
            // authentication
            //
            // https://mosquitto.org/man/mosquitto-conf-5.html
            // for mosquitto, anonymous log in is just allowed in local machine
            // however, gernerally, the broker is deployed in the server, so the client would not in the same machine
            connOpts.setUserName(myuserName);
            connOpts.setPassword(mypwd.getBytes());
            //
            // ------------------
            //
            connOpts.setAutomaticReconnect(true);
            //
            // -------------------------------------------------------------------------
            // -----------------------set  disconnected buffer options------------------
            //
            DisconnectedBufferOptions disconnect_bfOpt_1=new DisconnectedBufferOptions();
            // 初始化disconnectedBufferOptions
            disconnect_bfOpt_1.setBufferSize(100);				//离线后最多缓存100条
            disconnect_bfOpt_1.setPersistBuffer(false);  		//不一直持续留存
            disconnect_bfOpt_1.setDeleteOldestMessages(false);	//删除旧消息
            disconnect_bfOpt_1.setBufferEnabled(true);			// 断开连接后进行缓存
            sampleClient.setBufferOpts(disconnect_bfOpt_1);
            // -------------------------------------------------------------------------
            //
            //
            // connect to broker
            System.out.println("Connecting to broker: "+brokerUri);
            //sampleClient.connect(connOpts);									//如果是MqttClient 贼需要这个
            sampleClient.connect(connOpts, null, null).waitForCompletion(-1); 	//如果是MqttAsyncClient 贼需要这个
            System.out.println("Connected");
            //
            //
            //
            MqttMessage message_tmp;
            StringBuffer str_content_tmp = new StringBuffer("");
            for(int i=0; i<=1000; i++) {
            	//
            	str_content_tmp.delete(0, str_content_tmp.length()-1+1);
            	str_content_tmp.append(content +":"+(i+1));
            	//
            	message_tmp = new MqttMessage(str_content_tmp.toString().getBytes());
            	message_tmp.setQos(qos);
            	message_tmp.setRetained(false);
            	//
            	try {
                	System.out.println("Publishing message: "+str_content_tmp);
                    sampleClient.publish(topic, message_tmp);
            	}
            	catch(MqttException me) {
            		me.printStackTrace();
            	}
                //
                Thread.sleep(3000);
            }
            
            System.out.println("Message published");
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
