import java.util.List;

public class EncryptData {
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

    public byte[] encryptData(String str) {


        return new byte[1];
    }

}
