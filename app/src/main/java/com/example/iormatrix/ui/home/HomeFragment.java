package com.example.iormatrix.ui.home;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.iormatrix.MyMatrix;
import com.example.iormatrix.R;
import com.example.iormatrix.Socket_Connect;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private MyMatrix matrix = null;
    private View root = null;
    private Socket_Connect socket_connect = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        this.root = inflater.inflate(R.layout.fragment_home, container, false);
        //((Button)root.findViewById(R.id.exec_cmd)).setEnabled(enable_button);
        //((EditText)root.findViewById(R.id.cmd_field)).setEnabled(enable_button);

        Log.d("debug", "onCreateView() called");
        matrix = new MyMatrix(root);
        exec_click_config(root);
        socket_connect = new Socket_Connect();

        Log.d("debug", "onCreateView() exiting");

        return root;
    }

    //============================================================================================



    @Override
    public void onStart() {
        super.onStart();
        Log.d("debug", "onStart() called");
        //new ServerAsyncTask(matrix).execute();
    }

    private class ServerAsyncTask extends AsyncTask<Void, String, Void> {

        private volatile MyMatrix matrix;
        private boolean loop = true;

        //============================================================================================

        public ServerAsyncTask(MyMatrix matrix){
            this.matrix = matrix;
            Log.d("debug", "ServerAsyncTask() constructor called " + this.matrix);
        }
        //============================================================================================

        @Override
        protected Void doInBackground(Void... voids) {
            String cmd = null;
            Log.d("debug", "Getting message ....");
            while(loop){
                Log.d("debug", "doInBackground() looped ...");
                //cmd = socket_connect.getMessage();
                publishProgress(cmd);
            }
            return null;
        }

        //============================================================================================
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("debug", "ServerAsyncTask.onPostExecute() called ....");

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            this.matrix.terminal_exec(values[0], HomeFragment.this.getActivity());
            ((EditText)this.matrix.getParentview().findViewById(R.id.cmd_field)).setText(values[0]);
        }
        //============================================================================================

        //============================================================================================


    }

    //============================================================================================
    @Override
    public void onDestroy() {
        //MainActivity.socket_connect.Socket_Connect_Destroy();
        Log.d("debug", "onDestroy called : " + matrix);
        matrix = null;
        super.onDestroy();
    }

    //============================================================================================

    private void exec_click_config(final View curr_view){
        //Log.d("debug",  "clicked on the button !!");
        FloatingActionButton exec_btn = (FloatingActionButton) curr_view.findViewById(R.id.exec_cmd);
        exec_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("debug",  "clicked on the button !!");
                Log.d("debug",  "matrix = " + matrix);
                String val = "";
                final EditText editText = (EditText) curr_view.findViewById(R.id.cmd_field);
                socket_connect.sendMessage(editText.getText().toString());
                /*
                if(matrix.terminal_exec(editText.getText().toString(), HomeFragment.super.getActivity())){
                    editText.setText("");
                }*/
            }
        });
    }

}