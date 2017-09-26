import sun.jvm.hotspot.runtime.Bytes;

import java.util.ArrayList;
import java.util.List;

class EncryptPacket {
    private String destinationAddress, sourceAddress;

    public EncryptPacket(String destinationAddress, String sourceAddress) {
        this.destinationAddress = destinationAddress;
        this.sourceAddress = sourceAddress;
    }

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

    /**
     * Создаёт Header из адресов (названий) портов
     * @return Собственно готовый Header
     */
    private List<Byte> createHeader() {
        List<Byte> header = new ArrayList<Byte>();
        header.add((byte) 0x7E);

        List<Byte> dA = new ArrayList<Byte>();
        dA.addAll(createPayload(destinationAddress));

        List<Byte> sA = new ArrayList<Byte>();
        sA.addAll(createPayload(sourceAddress));

        header.addAll(dA);
        while (header.size() < 31) {
            header.add((byte) 32);
        }

        header.addAll(sA);
        while (header.size() < 61) {
            header.add((byte) 32);
        }

        return header;
    }

    /**
     * Преобразует строку в List<Byte> и кодирует её в соответствии с алгоритмом байт-стаффинга
     * @param data Собственно строка
     * @return Собственно обработанная строка
     */
    private List<Byte> createPayload(String data) {
        byte[] temp = data.getBytes();
        List<Byte> buffer = new ArrayList<Byte>();

        for (int i = 0; i < temp.length; i++) {
            if (temp[i] != 0x7E && temp[i] != 0x7D) {
                buffer.add(temp[i]);
            } else {
                if (temp[i] == 0x7E) {
                    buffer.add((byte)0x7D);
                    buffer.add((byte)0x5E);
                } else {
                    buffer.add((byte)0x7D);
                    buffer.add((byte)0x5D);
                }
            }
        }

        return buffer;
    }

    /**
     * Создаёт пакет на основе присланнх ему данных
     * @param payload Данные на передачу
     * @return Собственно пакет
     */
    private List<Byte> createPacket(List<Byte> payload) {
        List<Byte> packet = new ArrayList<Byte>();

        packet.addAll(createHeader());
        packet.addAll(payload);

        return packet;
    }

    private List<List<Byte>> getPackets(String data) {
        // Сюда будем складировать готовые пакеты
        List<List<Byte>> packets = new ArrayList<List<Byte>>();
        // Создаём обработанные данные
        List<Byte> payload = createPayload(data);

        int endPoint = (payload.size() / 100) - 1;
        if (endPoint < 0) {
            packets.add(createPacket(payload));
            return packets;
        }

        boolean flag = false;
        List<Byte> p = new ArrayList<Byte>();

        for (int i = 0; i <= endPoint; i++) {
            if (flag == false) {
                for (int j = i * 100; j < i * 100 + 100; j++) {
                    p.add(payload.get(j));
                }
                if (p.get(99) == 0x7D) {
                    p.add(payload.get(i * 100 + 100 - 1));
                    flag = true;
                }
                packets.add(createPacket(p));
                p.clear();
            } else {
                for (int j = i * 100 + 1; j < i * 100 + 100; j++) {
                    p.add(payload.get(j));
                }
                if (p.get(99) == 0x7D) {
                    p.add(payload.get(i * 100 + 100 - 1));
                    flag = true;
                } else {
                    flag = false;
                }
                packets.add(createPacket(p));
                p.clear();
            }
        }
        endPoint ++;
        if (endPoint * 100 < payload.size()) {
            if (flag == false) {
                for (int i = endPoint * 100; i < payload.size(); i++) {
                    p.add(payload.get(i));
                }
                while (p.size() < 100) {
                    p.add((byte) 32);
                }
                packets.add(createPacket(p));
                p.clear();
            } else {
                for (int i = endPoint * 100 + 1; i < payload.size(); i++) {
                    p.add(payload.get(i));
                }
                while (p.size() < 100) {
                    p.add((byte) 32);
                }
                packets.add(createPacket(p));
                p.clear();
            }
        }

        return packets;
    }

    public List<byte[]> packetsList(String data) {
        List<byte[]> packets = new ArrayList<byte[]>();
        List<List<Byte>> p = getPackets(data);

        for (int i = 0; i < p.size(); i++) {
            packets.add(toByteArray(p.get(i)));
        }

        return packets;
    }

}
