package com.compsci702.DigiReceipt.core;

import android.os.Environment;
import com.compsci702.DigiReceipt.ui.model.DRReceiptTemp;
import com.google.api.client.util.Base64;
import com.google.gson.Gson;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import rx.Observable;
import rx.Subscriber;

/**
 * Network Hub.
 */
public class DRNetworkHub {

    private static final String TARGET_URL = "https://vision.googleapis.com/v1/images:annotate?";
    private static final String API_KEY = "key=AIzaSyBa8Ozp8y9v8NQixUMMDKX58a9dHQe4rSo";
    static Gson gson = new Gson();

    private static byte[] readBytesFromFile(String filePath) {
        String path = String.valueOf(DRApplication.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        path += filePath.split("/")[filePath.split("/").length-1];
        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;
        try {
            File file = new File(path);
            bytesArray = new byte[(int) file.length()];
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bytesArray;
    }

    public static DRReceiptTemp sendPost(String filePath) throws Exception {

        byte[] fileContent = readBytesFromFile(filePath);
        URL serverUrl = new URL(TARGET_URL + API_KEY);
        URLConnection urlConnection = serverUrl.openConnection();
        HttpURLConnection httpConnection = (HttpURLConnection)urlConnection;
        httpConnection.setRequestMethod("POST");
        httpConnection.setRequestProperty("Content-Type", "application/json");
        httpConnection.setDoOutput(true);
        BufferedWriter httpRequestBodyWriter = new BufferedWriter(new
                OutputStreamWriter(httpConnection.getOutputStream()));
        httpRequestBodyWriter.write
                ("{\"requests\":  [{ \"features\":  [ {\"type\": \"TEXT_DETECTION\""
                        +"}], \"image\": {\"content\": \"" + Base64.encodeBase64String(fileContent)+ "\"}}]}");
        httpRequestBodyWriter.close();
        String response = httpConnection.getResponseMessage();
        if (httpConnection.getInputStream() == null) {
            return null;
        }
        Scanner httpResponseScanner = new Scanner (httpConnection.getInputStream());
        String temp = "";
        while (httpResponseScanner.hasNext()) {
            String line = httpResponseScanner.nextLine();
            if(line.contains("text")){
                temp = line;
            }
        }
        httpResponseScanner.close();
        return (DRReceiptTemp) gson.fromJson("{"+ temp + "}", Class.forName("com.compsci702.DigiReceipt.ui.model.DRReceiptTemp"));
    }
    public static rx.Observable<DRReceiptTemp> httpObservable(final String filePath){
        return Observable.create(new Observable.OnSubscribe<DRReceiptTemp>() {
            @Override
            public void call(Subscriber<? super DRReceiptTemp> subscriber) {
                try {
                    subscriber.onNext(sendPost(filePath));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                subscriber.onCompleted();
            }
        });
    }
}