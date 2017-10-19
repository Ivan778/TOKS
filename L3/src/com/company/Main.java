package com.company;

import java.util.ArrayList;
import java.util.List;

public class Main {

    /**
     * Create array of bits
     * @param str
     * @return
     */
    public static String convertToArray(String str) {
        String s = "";

        byte[] bytes = str.getBytes();
        for (byte b : bytes) {
            s += Integer.toBinaryString(b & 255 | 256).substring(1);
        }

        if (s.length() % 16 != 0) {
            s += "00000000";
        }

        return s;
    }

    /**
     * Get from byte symbol equivalent
     * @param str
     * @return
     */
    public static char getSymbolFromBits(String str) {
        int parseInt = Integer.parseInt(str, 2);

        return (char)parseInt;
    }

    /**
     * Create packets without Hamming bits
     * @param str
     * @return
     */
    public static List<String> getPackets(String str) {
        List<String> packets = new ArrayList<String>();

        for (int i = 0; i < str.length() / 16; i++) {
            String s = "";
            for (int j = i * 16; j < i * 16 + 16; j++) {
                s += str.charAt(j);
            }
            packets.add(s);
        }

        return packets;
    }

    public static List<String> insertBits(List<String> packets) {
        List<String> p = new ArrayList<String>();

        for (int i = 0; i < packets.size(); i++) {
            StringBuilder sB = new StringBuilder(packets.get(i));

            sB.insert(0, 'c');
            sB.insert(1, 'c');
            sB.insert(3, 'c');
            sB.insert(7, 'c');
            sB.insert(15, 'c');

            p.add(new String(sB));
        }

        return p;
    }

    public static int gI(char c) {
        return Character.getNumericValue(c);
    }

    public static int get1(String str) {
        int sum = 0;
        for (int i = 0; i < str.length(); i += 2) sum += gI(str.charAt(i));

        return sum;
    }

    public static int get2(String str) {
        return gI(str.charAt(1)) + gI(str.charAt(2)) +
                gI(str.charAt(5)) + gI(str.charAt(6)) +
                 gI(str.charAt(9)) + gI(str.charAt(10)) +
                  gI(str.charAt(13)) + gI(str.charAt(14)) +
                   gI(str.charAt(17)) + gI(str.charAt(18));
    }

    public static int get4(String str) {
        return gI(str.charAt(3)) + gI(str.charAt(4)) + gI(str.charAt(5)) + gI(str.charAt(6)) +
                gI(str.charAt(11)) + gI(str.charAt(12)) + gI(str.charAt(13)) + gI(str.charAt(14)) +
                gI(str.charAt(19)) + gI(str.charAt(20));
    }

    public static int get8(String str) {
        int sum = 0;
        for (int i = 7; i <= 14; i++) sum += gI(str.charAt(i));

        return sum;
    }

    public static int get16(String str) {
        int sum = 0;
        for (int i = 15; i <= 20; i++) sum += gI(str.charAt(i));

        return sum;
    }

    public static void setBits(List<String> packets) {
        for (int i = 0; i < packets.size(); i++) {
            if (get1(packets.get(i)) % 2 == 0) {
                StringBuilder temp = new StringBuilder(packets.get(i));
                temp.setCharAt(0, '0');
                packets.set(i, new String(temp));
            } else {
                StringBuilder temp = new StringBuilder(packets.get(i));
                temp.setCharAt(0, '1');
                packets.set(i, new String(temp));
            }

            if (get2(packets.get(i)) % 2 == 0) {
                StringBuilder temp = new StringBuilder(packets.get(i));
                temp.setCharAt(1, '0');
                packets.set(i, new String(temp));
            } else {
                StringBuilder temp = new StringBuilder(packets.get(i));
                temp.setCharAt(1, '1');
                packets.set(i, new String(temp));
            }

            if (get4(packets.get(i)) % 2 == 0) {
                StringBuilder temp = new StringBuilder(packets.get(i));
                temp.setCharAt(3, '0');
                packets.set(i, new String(temp));
            } else {
                StringBuilder temp = new StringBuilder(packets.get(i));
                temp.setCharAt(3, '1');
                packets.set(i, new String(temp));
            }

            if (get8(packets.get(i)) % 2 == 0) {
                StringBuilder temp = new StringBuilder(packets.get(i));
                temp.setCharAt(7, '0');
                packets.set(i, new String(temp));
            } else {
                StringBuilder temp = new StringBuilder(packets.get(i));
                temp.setCharAt(7, '1');
                packets.set(i, new String(temp));
            }

            if (get16(packets.get(i)) % 2 == 0) {
                StringBuilder temp = new StringBuilder(packets.get(i));
                temp.setCharAt(15, '0');
                packets.set(i, new String(temp));
            } else {
                StringBuilder temp = new StringBuilder(packets.get(i));
                temp.setCharAt(15, '1');
                packets.set(i, new String(temp));
            }
        }
    }

    public static void getOriginalPackets(List<String> packets) {
        for (int i = 0; i < packets.size(); i++) {
            StringBuilder temp = new StringBuilder(packets.get(i));
            temp.deleteCharAt(15);
            temp.deleteCharAt(7);
            temp.deleteCharAt(3);
            temp.deleteCharAt(1);
            temp.deleteCharAt(0);
            packets.set(i, new String(temp));
        }
    }

    /**
     * Create encoded message
     * @param str
     * @return
     */
    public static List<String> encodeString(String str) {
        // Разбиваем всё на пакеты
        List<String> packets = getPackets(str);
        // Вставляем контрольные биты
        packets = insertBits(packets);
        // Вычисляем значения контрольных битов
        setBits(packets);

        return packets;
    }

    public static String decodePacket(String packet) {
        return "" + getSymbolFromBits(packet.substring(0, 8)) + getSymbolFromBits(packet.substring(9, 16));
    }

    public static String decodeString(List<String> packets) {
        String str = "";

        List<String> originalPackets = new ArrayList<String>(packets);

        /*
        StringBuilder t = new StringBuilder(originalPackets.get(0));
        t.setCharAt(2, '1');
        originalPackets.set(0, new String(t));
        */

        for (int i = 0; i < originalPackets.size(); i++) {
            System.out.print(originalPackets.get(i));
        }

        getOriginalPackets(originalPackets);
        //System.out.println(originalPackets.get(0));

        originalPackets = insertBits(originalPackets);
        setBits(originalPackets);
        //System.out.println(originalPackets.get(0));

        for (int i = 0; i < packets.size(); i++) {
            if (packets.get(i).equals(originalPackets.get(i)) == false) {
                int sum = 0;
                if (packets.get(i).charAt(0) != originalPackets.get(i).charAt(0)) {
                    sum += 1;
                }
                if (packets.get(i).charAt(1) != originalPackets.get(i).charAt(1)) {
                    sum += 2;
                }
                if (packets.get(i).charAt(3) != originalPackets.get(i).charAt(3)) {
                    sum += 4;
                }
                if (packets.get(i).charAt(7) != originalPackets.get(i).charAt(7)) {
                    sum += 8;
                }
                if (packets.get(i).charAt(15) != originalPackets.get(i).charAt(15)) {
                    sum += 16;
                }

                sum--;

                if (originalPackets.get(i).charAt(sum) == '1') {
                    StringBuilder temp = new StringBuilder(originalPackets.get(i));
                    temp.setCharAt(sum, '0');
                    originalPackets.set(i, new String(temp));
                } else {
                    StringBuilder temp = new StringBuilder(originalPackets.get(i));
                    temp.setCharAt(sum, '1');
                    originalPackets.set(i, new String(temp));
                }
            }
        }

        //System.out.println(originalPackets.get(0));
        getOriginalPackets(originalPackets);
        //System.out.println(originalPackets.get(0));

        System.out.println();

        for (int i = 0; i < originalPackets.size(); i++) {
            str += decodePacket(originalPackets.get(i));
        }

        if (str.charAt(str.length() - 1) == '\u0000') {
            str = str.substring(0, str.length() - 1);
        }

        return str;
    }

    public static void main(String[] args) {
        String str = convertToArray("Hello, Julia)");
        List<String> packets = encodeString(str);

        for (int i = 0; i < packets.size(); i++) {
            System.out.print(packets.get(i));
        }
        System.out.println();

        System.out.println(decodeString(packets));

    }
}
