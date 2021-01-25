package com.example.iormatrix;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MQTT_Connect extends MqttAndroidClient {

    public static String username = "binome8"; // <ApplicationID>
    public static String password = "binome8";
    public static String subscriptionTopic = "IOR_MATRIX/devices/phone1";

    public static String clientId = "IOR_MATRIX1";
    public static String serverURI = "tcp://test.mosquitto.org";
    private MqttConnectOptions mqttConnectOptions;

    //===============================================================================================
    class MQTT_ActionListener implements IMqttActionListener {

        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            Log.d("debug_MQTT", "MQTT_ActionListener.onSuccess() called");
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            Log.d("debug_MQTT", "MQTT_ActionListener.onFailure() called");
        }
    }
    //===============================================================================================

    public MQTT_Connect(Context context) {
        super(context, serverURI, clientId);
        mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        //mqttConnectOptions.setUserName(username);
        //mqttConnectOptions.setPassword(password.toCharArray());

        try {
            this.connect(mqttConnectOptions, null, new MQTT_ActionListener()
            {
                @Override
                public void onSuccess(IMqttToken asyncActionToken)
                {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    MQTT_Connect.this.setBufferOpts(disconnectedBufferOptions);
                    Log.d("debug_MQTT", "Connected successfully");
                    try {
                        MQTT_Connect.this.subscribe(subscriptionTopic, 0, null, new MQTT_ActionListener(){});
                    } catch (MqttException e) {
                        Log.d("debug_MQTT", "MQTT exception : " + e.getMessage());
                    }
                }
            });
        } catch (MqttException e) {
            Log.d("debug_MQTT", "MQTT exception : " + e.getMessage());
        }

    }

}
