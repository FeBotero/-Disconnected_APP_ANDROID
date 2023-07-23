package com.example.disconected;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HandleProps {
    public static final String GETPROP_EXECUTABLE_PATH = "/system/bin/getprop";
    public static final String SETPROP_EXECUTABLE_PATH = "/system/bin/setprop";
    public  String read(String propName) {
        Process process = null;
        BufferedReader bufferedReader = null;

        try {
            process = new ProcessBuilder().command(GETPROP_EXECUTABLE_PATH, propName).redirectErrorStream(true).start();
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = bufferedReader.readLine();
            if (line == null){
                line = ""; //prop not set
            }
            Log.i("SystemProperties","Reading prop:" + propName + " value:" + line);
            return line;

        } catch (Exception e) {
            Log.e("SystemProperties","Failed to read System Property " + propName,e);
            return "";
        } finally{
            if (bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    return "";
                }
            }
            if (process != null){
                process.destroy();
            }
        }
    }
    public  void write(String propName, String propValue) {
        Process process = null;
        BufferedReader bufferedReader = null;

        try {
            process = new ProcessBuilder().command(SETPROP_EXECUTABLE_PATH, propName, propValue).redirectErrorStream(true).start();
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = bufferedReader.readLine();
        } catch (Exception e) {
            Log.e("SystemProperties","Failed to write System Property " + propName,e);
        } finally{
            if (bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {

                }
            }
            if (process != null){
                process.destroy();
            }
        }
    }
}