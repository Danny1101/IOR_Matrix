package com.example.iormatrix.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.iormatrix.MQTT_Connect;
import com.example.iormatrix.R;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private MQTT_Connect mqtt_connect;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // ========================================================================================
        ((Button)root.findViewById(R.id.gotop)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DashboardFragment.this.send_command("h");
                try {
                    mqtt_connect.publish(MQTT_Connect.subscriptionTopic, "top".getBytes(), 0, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });
        // ========================================================================================
        ((Button)root.findViewById(R.id.gobot)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DashboardFragment.this.send_command("b");
                try {
                    mqtt_connect.publish(MQTT_Connect.subscriptionTopic, "bot".getBytes(), 0, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });
        // ========================================================================================
        ((Button)root.findViewById(R.id.goleft)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DashboardFragment.this.send_command("g");
                try {
                    mqtt_connect.publish(MQTT_Connect.subscriptionTopic, "left".getBytes(), 0, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });
        // ========================================================================================
        ((Button)root.findViewById(R.id.goright)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DashboardFragment.this.send_command("d");
                try {
                    mqtt_connect.publish(MQTT_Connect.subscriptionTopic, "right".getBytes(), 0, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });
        // ========================================================================================
        ((Button)root.findViewById(R.id.exit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DashboardFragment.this.send_command("exit");
            }
        });
        // ========================================================================================
        ((Button)root.findViewById(R.id.go_init)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DashboardFragment.this.send_command("init");
                try {
                    mqtt_connect.publish("IOR_MATRIX/devices/matrix", "reset".getBytes(), 0, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });
        // ========================================================================================
        mqtt_config();
        return root;
    }

    private void mqtt_config(){

        mqtt_connect = new MQTT_Connect(super.getContext());
        mqtt_connect.setCallback(new MqttCallbackExtended()
        {
            @Override
            public void connectComplete(boolean b, String s)
            {
                Log.w("debug_MQTT","connectComplete");
            }

            @Override
            public void connectionLost(Throwable throwable)
            {
                Log.w("debug_MQTT","connectionLost");
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception
            {
                Log.w("debug_MQTT", "messageArrived : " + mqttMessage.toString());
                try{
                    Integer.parseInt(mqttMessage.toString());
                    Toast.makeText(DashboardFragment.super.getActivity(),
                            "You won ! Your current score now is "+mqttMessage.toString()+"/100",
                            Toast.LENGTH_LONG).show();
                }catch (NumberFormatException nfe) {
                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken)
            {
                Log.w("debug_MQTT", "deliveryComplete");
            }
        });
    }
    //

    private void send_command(String cmd){
        Log.d("debug",cmd);
        //mqtt_connect.publish(MQTT_Connect.subscriptionTopic, "");
    }


}