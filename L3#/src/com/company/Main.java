package com.company;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static char getSymbolFromBits(String str) {
        int parseInt = Integer.parseInt(str, 2);

        return (char)parseInt;
    }

    // Разбиваю строку на пакеты по 8 бита каждый
    public static List<String> getPackets(String str) {
        List<String> packets = new ArrayList<String>();

        for (int i = 0; i < str.length() / 8; i++) {
            String s = "";
            for (int j = i * 8; j < i * 8 + 8; j++) {
                s += str.charAt(j);
            }
            packets.add(s);
        }

        return packets;
    }

    public static String cyclicSdvigLeft(String packet) {
        return packet.substring(1, packet.length()) + packet.charAt(0);
    }

    public static String cyclicSdvigRight(String packet) {
        return packet.charAt(packet.length() - 1) + packet.substring(0, packet.length() - 1);
    }

    public static int getAmountOfOnes(String str) {
        return Integer.parseInt(str.charAt(0) + "") + Integer.parseInt(str.charAt(1) + "") + Integer.parseInt(str.charAt(2) + "");
    }

    public static void main(String[] args) {
        Encoder encoder = new Encoder();

        List<String> packetsToSend = encoder.encodeMessage("hello");



        String buffer = "";

        for (int i = 0; i < packetsToSend.size(); i++) {
            String str = packetsToSend.get(i).substring(0, packetsToSend.get(i).length() - 3);
            String add = packetsToSend.get(i).substring(packetsToSend.get(i).length() - 3, packetsToSend.get(i).length());
            if (encoder.findCRC(str, add).equals("000") == true) {
                buffer += str;
            } else {
                int sdvig = 0;


            }
        }

        System.out.println(encoder.convertToArray("hello"));
        System.out.println(buffer);

        List<String> symbols = getPackets(buffer);
        for (int i = 0; i < symbols.size(); i++) {
            System.out.print(getSymbolFromBits(symbols.get(i)));
        }

        System.out.println();
        
    }
}
