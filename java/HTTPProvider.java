import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Scanner;
import com.google.gson.Gson;

public class HTTPProvider {
	private static final String TARGET_URL =
            "https://vision.googleapis.com/v1/images:annotate?";
private static final String API_KEY =
            "key=AIzaSyBa8Ozp8y9v8NQixUMMDKX58a9dHQe4rSo";
	Gson gson = new Gson();

	public static void main(String[] args) throws Exception {

		HTTPProvider http = new HTTPProvider();

		System.out.println("\nTesting 2 - Send Http POST request");
		http.sendPost("2.jpg");

	}


	// HTTP POST request
	private ReceiptText sendPost(String filePath) throws Exception {
		
		File fi = new File(filePath);
		byte[] fileContent = Files.readAllBytes(fi.toPath());

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
	    		 +"}], \"image\": {\"content\": \"" +Base64.getEncoder().encodeToString(fileContent)+ "\"}}]}");
		httpRequestBodyWriter.close();
		String response = httpConnection.getResponseMessage();
		if (httpConnection.getInputStream() == null) {
			System.out.println("No stream");
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
		
		ReceiptText rt = gson.fromJson("{"+ temp + "}", Class.forName("ReceiptText"));
		System.out.println(rt.text);
		return  rt ;
	}
}