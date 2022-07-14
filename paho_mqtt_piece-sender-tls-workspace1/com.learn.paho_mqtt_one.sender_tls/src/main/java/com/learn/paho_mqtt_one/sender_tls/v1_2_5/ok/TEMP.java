package com.learn.paho_mqtt_one.sender_tls.v1_2_5.ok;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.eclipse.paho.mqttv5.client.MqttAsyncClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MqttDefaultFilePersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;

public class TEMP {
	public static void main(final String[] args) throws InterruptedException, CertificateException, IOException,
			KeyStoreException, NoSuchAlgorithmException, KeyManagementException, MqttException {

		// Certificate definition
		final CertificateFactory cf = CertificateFactory.getInstance("X.509");
		// final InputStream is = new FileInputStream(System.getProperty("user.dir") + "/mycerts/my_own/samecn/s_cacert.crt");
		final InputStream is = new FileInputStream(System.getProperty("user.dir") + "/mycerts/my_own/s_cacert.crt");
		final Certificate ca = cf.generateCertificate(is);

		// Trust manager factory
		final KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(null, null);
		ks.setCertificateEntry("ca", ca);
		final TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(ks);

		// SSL Socket Factory
		final SSLContext cxt = SSLContext.getInstance("TLSv1.3");
		cxt.init(null, tmf.getTrustManagers(), new java.security.SecureRandom());
		final SSLSocketFactory sf = cxt.getSocketFactory();

		// Typical MQTT use
		final MqttConnectionOptions co = new MqttConnectionOptions();
		co.setCleanStart(false);
		co.setSessionExpiryInterval(500L);
		co.setUserName("IamPublisherOne");
		co.setPassword("123456".getBytes());
		// !!! ------------- Set TLS/SSL -------------
		co.setSocketFactory(sf);
		co.setHttpsHostnameVerificationEnabled(false);

		final MqttAsyncClient client = new MqttAsyncClient("ssl://192.168.239.137:8883", "JavaSample",
				new MqttDefaultFilePersistence());
		client.connect(co, null, null).waitForCompletion(-1);
		final StringBuffer sb = new StringBuffer("");

		System.out.println("Connected, publishing messages...");
		for (int i = 0; i <= 1000; i++) {
			sb.setLength(0);
			sb.append("Hello: " + (i + 1));
			System.out.println("Publishing message: " + sb);

			final MqttMessage msg = new MqttMessage(sb.toString().getBytes());
			msg.setQos(1);
			msg.setRetained(false);
			client.publish("sensors/temperature", msg);

			Thread.sleep(3000);
		}
		System.out.println("Messages published");

		// Cleaning up
		client.disconnect();
		client.close();
		is.close();
	}
}
