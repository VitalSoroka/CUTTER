package com.mycompany.model.util;

public class TextUtils {
    public static String getBitesForEnglishMessage(String message){
        StringBuffer result = new StringBuffer();
        for (char symbol : message.toCharArray()){
            result.append(getBitesForASCIISymbol(symbol));
        }
        return result.toString();
    }

    public static String getBitesForASCIISymbol(char symbol) {
        String str = Integer.toBinaryString(symbol);
        while (str.length() < 8) {
            str = "0" + str;
        }
        return str;
    }

    public static String convertBitesToACCIISrting(String bites) {
        int curentIteration = 0;
        StringBuffer result = new StringBuffer();
        char curentSymbol = 0x0000;
        for (char bite : bites.toCharArray()) {
            if (bite == '1') {
                curentSymbol = (char) ((curentSymbol << 1) + 1);
            } else {
                curentSymbol <<= 1;
            }
            curentIteration ++;
            if (curentIteration % 8 == 0) {
                result.append(curentSymbol);
                curentSymbol = 0x0000;
            }
        }
        return result.toString();
    }
}
