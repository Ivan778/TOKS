package com.company;

import java.util.ArrayList;
import java.util.List;

public class Encoder {
    private String polinom = "1011";

    // Выполняет операцию xor над двумя строками
    public String XOR(String str) {
        String result = "";

        for (int i = 0; i < polinom.length(); i++) {
            if (str.charAt(i) == polinom.charAt(i)) {
                result += "0";
            } else {
                result += "1";
            }
        }

        return result;
    }

    // Делит последовательность на полином, дописывая к ней добавку add
    public String findCRC(String str, String add) {
        int k = 0;
        String temp = str;
        while (k < 3) {
            temp = XOR(temp);
            if (temp.charAt(0) == '0') {
                temp = temp.substring(1, temp.length()) + add.charAt(k);
                k++;
            }
        }

        while (true) {
            temp = XOR(temp);
            if (temp.charAt(0) == '0') {
                temp = temp.substring(1, temp.length());
                break;
            }
        }

        return temp;
    }

    // Формирую из строки её битовое представление
    public String convertToArray(String str) {
        String s = "";

        byte[] bytes = str.getBytes();
        for (byte b : bytes) {
            s += Integer.toBinaryString(b & 255 | 256).substring(1);
        }

        return s;
    }

    // Разбиваю строку на пакеты по 4 бита каждый
    private List<String> getPackets(String str) {
        List<String> packets = new ArrayList<String>();

        for (int i = 0; i < str.length() / 4; i++) {
            String s = "";
            for (int j = i * 4; j < i * 4 + 4; j++) {
                s += str.charAt(j);
            }
            packets.add(s);
        }

        return packets;
    }

    public List<String> encodeMessage(String str) {
        String t = convertToArray(str);
        List<String> packets = getPackets(t);

        for (int i = 0; i < packets.size(); i++) {
            String temp = packets.get(i);
            temp = temp + findCRC(temp, "000");
            packets.set(i, temp);
        }

        return packets;
    }


}
