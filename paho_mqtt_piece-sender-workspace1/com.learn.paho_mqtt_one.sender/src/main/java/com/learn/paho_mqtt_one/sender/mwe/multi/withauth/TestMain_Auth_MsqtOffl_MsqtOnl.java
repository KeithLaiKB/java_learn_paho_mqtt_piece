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
 *	����Ҫ����
 *	step1(����):	publisher 	���� 123
 *	step2(����):	subscriber 	����123
 *
 *	step3(����):	�ر� subscriber 
 *
 *	step4(����):	publisher 	����45678
 *  
 *	step5(����):	�ر� docker mosquitto		!!!!!!!!!!!!!!!!!!!!!
 *
 *	step6(����):	publisher 	�������� 9 10 11 12
 *	step7(����):	Ȼ�� ���� docker mosquitto
 *
 *	step7(����):	Ȼ��publisher �������� 13 14 15
 *
 *	step8(����):	Ȼ�� ���� subscriber
 *	step9(����):	Ȼ�� subscriber �ܽ��� 
 *								4 5 6 7 8
 *								      ��
 *								9 10 11 12
 *								      ��
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
 *  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 *  publisher(online) 	-------------> 	mosquitto(online)  -------------->	subscriber(offline)
 *  publisher(online) 	----45678----> 	mosquitto(online)  -------------->	subscriber(offline)
 *  publisher(online) 	-------------> 	mosquitto(online)  -------------->	subscriber(offline)
 *  									   4 5 6 7 8
 *  
 *  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 *  ++++++++++++++++++++++++++ 			turn off broker			+++++++++++++++++++++++++++++++
 *  ++++++	��Ϊ (setBufferEnabled(true)) ʹ�� broker���� ʱ    publisher �ܱ��淢�Ͳ���ȥ�� 9 10 11 12	+++++++
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
 *
 */
public class TestMain_Auth_MsqtOffl_MsqtOnl {

	public static void main(String[] args) {

        //String topic        = "MQTT Examples";
        String topic        = "sensors/temperature";
        //String content      = "Message from MqttPublishSample";
        String content      = "hello";
        int qos             = 1;
        //String broker       = "tcp://iot.eclipse.org:1883";
        String broker       = "tcp://localhost:1883";
        String clientId     = "JavaSample";
        
        String myuserName	= "IamPublisherOne";
        String mypwd		= "123456";
        


        try {
        	//MqttClient sampleClient = new MqttClient(broker, clientId, new MemoryPersistence());
        	//MqttAsyncClient sampleClient = new MqttAsyncClient(broker, clientId, new MqttDefaultFilePersistence());
        	MqttAsyncClient sampleClient = new MqttAsyncClient(broker, clientId, new MemoryPersistence());
        	//
        	//
        	// set connection options
            MqttConnectionOptions connOpts = new MqttConnectionOptions();
            //connOpts.setCleanStart(false);
            //connOpts.setSessionExpiryInterval(500L);
            connOpts.setCleanStart(true);
            //
            //
            //
            // https://mosquitto.org/man/mosquitto-conf-5.html
            // for mosquitto, anonymous log in is just allowed in local machine
            // however, gernerally, the broker is deployed in the server, so the client would not in the same machine
            // so here is authentication
            // authentication
            connOpts.setUserName(myuserName);
            connOpts.setPassword(mypwd.getBytes());
            //
            //
            //------------------
            connOpts.setAutomaticReconnect(true);
            //------------------
            DisconnectedBufferOptions disconnect_bfOpt_1=new DisconnectedBufferOptions();
            // ��ʼ��disconnectedBufferOptions
            disconnect_bfOpt_1.setBufferSize(100);				//���ߺ���໺��100��
            disconnect_bfOpt_1.setPersistBuffer(false);  		//��һֱ��������
            disconnect_bfOpt_1.setDeleteOldestMessages(false);	//ɾ������Ϣ
            disconnect_bfOpt_1.setBufferEnabled(true);			// �Ͽ����Ӻ���л���
            sampleClient.setBufferOpts(disconnect_bfOpt_1);
            //------------------
            //
            //
            // connect to broker
            System.out.println("Connecting to broker: "+broker);
            //sampleClient.connect(connOpts);									//�����MqttClient ����Ҫ���
            sampleClient.connect(connOpts, null, null).waitForCompletion(-1); 	//�����MqttAsyncClient ����Ҫ���
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