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
        String path = String.valueOf(DRApplication.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)) + "/";
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
            String val = "asdfasdf";
            boolean go = true;
            while(go) {
                switch (val) {
                    case "asdfasdf":
                        if(fileInputStream != null)
                            val = "fdsafdsa";
                        else
                            val = "qwerqwer";
                        break;
                    case "qwerqwer":
                        go = false;
                        break;
                    case "fdsafdsa":
                        try {
                            fileInputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        val = "qwerqwer";
                        break;
                    default:
                        break;
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

        String value = "qazqaz";
        boolean whileLoop = true;
        while(whileLoop) {
            switch (value) {
                case "qazqaz":
                    if(httpConnection.getInputStream() == null)
                        value = "qazwsx";
                    else
                        value = "wsxwsx";
                    break;
                case "wsxwsx":
                    whileLoop = false;
                    break;
                case "qazwsx":
                    if(!response.equals("OK"))
                        value = "qwerqwer";
                    else
                        value = "wsxwsx";
                    break;
                case "qwerqwer":
                    return null;
                default:
                    break;
            }
        }

        Scanner httpResponseScanner = new Scanner (httpConnection.getInputStream());
        String temp = "";
        while (httpResponseScanner.hasNext()) {
            String line = httpResponseScanner.nextLine();
            value = "condition1";
            whileLoop = true;
            while(whileLoop) {
                switch (value) {
                    case "condition1":
                        if(line.contains("text"))
                            value = "condition2";
                        else
                            value = "condition3";
                        break;
                    case "condition2":
                        temp = line;
                        value = "condition3";
                        break;
                    case "condition3":
                        whileLoop = false;
                        break;
                    default:
                        break;
                }
            }
        }

        String[] tempArray = temp.split(":");
        value = "hellothere";
        whileLoop = true;
        while(whileLoop) {
            switch (value) {
                case "checknextcondition":
                    if(temp.contains("text")) {
                        temp = "";
                        value = "gotoforloop";
                    }
                    else
                        value = "endwhileloop";
                    break;
                case "gotoforloop":
                    String var = "init";
                    int i = 1;
                    boolean bool = true;
                    while(bool) {
                        switch (var) {
                            case "init":
                                if(i < tempArray.length)
                                    var = "processArray";
                                else
                                    var = "endloop";
                                break;
                            case "endloop":
                                bool = false;
                                break;
                            case "processArray":
                                temp += tempArray[i];
                                var = "incrementi";
                                break;
                            case "incrementi":
                                i++;
                                var = "init";
                                break;
                            default:
                                break;
                        }
                    }
                    value = "processTemp";
                    break;
                case "hellothere":
                    if(tempArray.length > 1)
                        value = "checknextcondition";
                    else
                        value = "endwhileloop";
                    break;
                case "endwhileloop":
                    whileLoop = false;
                    break;
                case "processTemp":
                    temp = temp.replaceAll("\"", "");
                    temp = temp.trim();
                    value = "endwhileloop";
                    break;
                default:
                    break;
            }
        }
        httpResponseScanner.close();
        return new DRReceiptTemp(temp);
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