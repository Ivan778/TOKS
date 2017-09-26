package Crypting;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class DecryptPacket {
    byte[] packet;
    String name;

    /**
     * Конвертирует List<Byte> в byte[]
     * @param in List<Byte>, который нужно конвертировать в byte[]
     * @return byte[]
     */
    private byte[] toByteArray(List<Byte> in) {
        final int n = in.size();
        byte ret[] = new byte[n];

        for (int i = 0; i < n; i++) {
            ret[i] = in.get(i);
        }

        return ret;
    }

    public DecryptPacket(byte[] packet) {
        this.packet = packet;
        this.name = name;
    }

    private void decryptData(List<Byte> temp) {
        for (int i = 0; i < temp.size(); i++) {
            if (temp.get(i) == 0x7D) {
                if (temp.get(i + 1) == 0x5E) {
                    temp.set(i, (byte) 0x7E);
                } else {
                    temp.set(i, (byte) 0x7D);
                }
                temp.remove(i + 1);
            }
        }
    }

    public List<String> decryptHeader() {
        List<String> headerParts = new ArrayList<String>();

        List<Byte> temp = new ArrayList<Byte>();

        for (int i = 1; i < 31; i++) {
            temp.add(packet[i]);
        }

        decryptData(temp);
        try {
            StringBuilder name = new StringBuilder(new String(toByteArray(temp), "UTF-8"));
            for (int i = 0; i < name.length(); i++) {
                if (name.charAt(i) == ' ') {
                    name.deleteCharAt(i);
                    i--;
                }
            }

            headerParts.add(name.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        temp.clear();

        for (int i = 31; i < 61; i++) {
            temp.add(packet[i]);
        }

        decryptData(temp);
        try {
            StringBuilder name = new StringBuilder(new String(toByteArray(temp), "UTF-8"));
            for (int i = 0; i < name.length(); i++) {
                if (name.charAt(i) == ' ') {
                    name.deleteCharAt(i);
                    i--;
                }
            }

            headerParts.add(name.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return headerParts;
    }

    public String decryptPayload() {
        String str = "";

        List<Byte> payload = new ArrayList<Byte>();
        for (int i = 61; i < packet.length; i++) {
            payload.add(packet[i]);
        }

        decryptData(payload);
        try {
            str = new String(toByteArray(payload), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return str;
    }

}
