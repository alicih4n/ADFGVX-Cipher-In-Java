import java.util.*;

public class ADFGVXCipher {

    private static final char[][] CIPHER_TABLE = {
        {'N', 'A', '1', 'C', '3', 'H'},
        {'8', 'T', 'B', '2', 'O', 'M'},
        {'E', '5', 'W', 'R', 'P', 'D'},
        {'4', 'F', '6', 'G', '7', 'I'},
        {'9', 'J', '0', 'K', 'L', 'Q'},
        {'S', 'U', 'V', 'X', 'Y', 'Z'}
    };

    private static final char[] ROW_LABELS = {'A', 'D', 'F', 'G', 'V', 'X'};

    private static String encodeToADFGVX(String plaintext) {
        Map<Character, String> lookup = new HashMap<>();
        
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                lookup.put(CIPHER_TABLE[i][j], "" + ROW_LABELS[i] + ROW_LABELS[j]);
            }
        }

        plaintext = plaintext.toUpperCase();
        StringBuilder encoded = new StringBuilder();

        for (char c : plaintext.toCharArray()) {
            encoded.append(lookup.getOrDefault(c, ""));
        }

        return encoded.toString();
    }

    private static List<String> createTranspositionTable(String encodedText, String key) {
        int columns = key.length();
        int rows = (int) Math.ceil((double) encodedText.length() / columns);
        List<String> table = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            int start = i * columns;
            int end = Math.min(start + columns, encodedText.length());
            table.add(encodedText.substring(start, end));
        }

        while (table.get(table.size() - 1).length() < columns) {
            table.set(table.size() - 1, table.get(table.size() - 1) + " ");
        }

        return table;
    }

    private static String transposeAndEncrypt(List<String> table, String key) {
        char[] keyArray = key.toCharArray();
        Integer[] columnIndices = new Integer[keyArray.length];

        for (int i = 0; i < keyArray.length; i++) {
            columnIndices[i] = i;
        }

        Arrays.sort(columnIndices, Comparator.comparingInt(o -> keyArray[o]));

        StringBuilder ciphertext = new StringBuilder();

        for (int col : columnIndices) {
            for (String row : table) {
                if (col < row.length() && row.charAt(col) != ' ') {
                    ciphertext.append(row.charAt(col));
                }
            }
        }

        return ciphertext.toString();
    }

    public static String adfgvxEncrypt(String plaintext, String key) {
        String encodedText = encodeToADFGVX(plaintext);
        List<String> table = createTranspositionTable(encodedText, key);
        return transposeAndEncrypt(table, key);
    }

    public static void main(String[] args) {
        String plaintext = "attackat1200am";
        String key = "PRIVACY";

        String ciphertext = adfgvxEncrypt(plaintext, key);
        System.out.println("Ciphertext: " + ciphertext);
    }
}
