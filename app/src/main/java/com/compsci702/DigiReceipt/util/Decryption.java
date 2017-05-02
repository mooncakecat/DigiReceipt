package com.compsci702.DigiReceipt.util;
import android.util.Base64;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Decryption {
	private static SecretKeySpec secretKey;
	private static byte[] key;
	private static char[] ch = "ITSH^E-UGDK-5H8S1P*!FEG6B$ADUSFSJA#S!DGACIKG#PNVW/".toCharArray();
	public static String decrypt(String strToDecrypt){
		try {
			String secret = getKey();
			MessageDigest sha = null;
			key = secret.getBytes(getChar(1,160)+getChar(2,154)+getChar(3,136)+getChar(4,157)+getChar(5,100));
			sha = MessageDigest.getInstance(getChar(6,99)+getChar(7,143)+getChar(8,106)+getChar(9,132)+getChar(10,104));
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);
			secretKey = new SecretKeySpec(key, getChar(11,72)+getChar(12,120)+getChar(13,93));
			Cipher cipher = Cipher.getInstance(getChar(14,106)+getChar(15,111)+getChar(16,98) + ch[49] + getChar(17,117)+getChar(18,35)+getChar(19,79) + ch[49] +getChar(20,94)+getChar(21,130)+getChar(22,85)+ getChar(23,89)+getChar(24,90)+getChar(25,84)+getChar(26,107)+getChar(27,126)+getChar(28,131)+getChar(29,112)+getChar(30,107)+getChar(31,135));
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return new String(cipher.doFinal(Base64.decode(strToDecrypt,Base64.NO_WRAP)));}
		catch (Exception e) {}
		return null;}

	public static String getChar(int i, int subtract){
		int x = ch[i] + ch[i+1] - subtract;
		return Character.toString(ch[x]);}

	private static int getIndex (double x){
		return (int) Math.round (5 * Math.sin(x) + 5);
	}

	private static String getKey(){
		String res = "";
		String[] alphabet = new String[50];
		int[] index = new int[50];
		String switchVariable = "^niu&hy87Gyov(fg6F(BJKhohiYRe(";
		while(switchVariable != "anfadsuY&)Y2340ypag8fva69*H"){
			switch (switchVariable){
				case "F*^d3^S6sx5x8X%":
					res += alphabet[index[getIndex(3.75)]] + alphabet[index[getIndex(0.5)]] + index[getIndex(2.5)] +  index[getIndex(3.25)];
					switchVariable = "vi76Fidrd8e%RI(PH&%^%";
					break;
				case "vi76Fidrd8e%RI(PH&%^%":
					res += "" + index[getIndex(0.5)] + index[getIndex(3)] + index[getIndex(2.25)] + index[getIndex(3.5)];
					switchVariable = "anfadsuY&)Y2340ypag8fva69*H";
					break;
				case "bifa^&*12g4og7f_+===":
					int j = 0;
					for(char i = 'a'; i <= 'z'; i++){
						alphabet[j] = String.valueOf(i);
						j++;
					}
					switchVariable = "f48C47f5d*9tf&45e%$udF(";
					break;
				case "g(^f$sEs63sdcivoyF5DurCkfTY":
					res += alphabet[index[getIndex(0)]] + alphabet[index[getIndex(1.25)]] + alphabet[index[getIndex(0)]] + alphabet[index[getIndex(5.5)]];
					switchVariable ="F*^d3^S6sx5x8X%";
					break;
				case "^niu&hy87Gyov(fg6F(BJKhohiYRe(":
					index = new int[]{7, 17, 11, 6, 2, 14, 4, 3, 1, 5, 22};
					switchVariable = "bifa^&*12g4og7f_+===";
					break;
				case "f48C47f5d*9tf&45e%$udF(":
					res += alphabet[index[getIndex(5)]] + alphabet[index[getIndex(3)]] + alphabet[index[getIndex(3.75)]] + alphabet[index[getIndex(3.75)]];
					switchVariable = "g(^f$sEs63sdcivoyF5DurCkfTY";
					break;
			}
		}
		return res;
	}
}