package org.hisrc.ptbatch.pte.util;

public class FilenameUtils {

    private FilenameUtils() {
    }

    public static String toFileSystemSafeName(String name, boolean dirSeparators,
                    int maxFileLength) {
        int size = name.length();
        StringBuffer rc = new StringBuffer(size * 2);
        for (int i = 0; i < size; i++) {
            char c = name.charAt(i);
            boolean valid = c >= 'a' && c <= 'z';
            valid = valid || (c >= 'A' && c <= 'Z');
            valid = valid || (c >= '0' && c <= '9');
            valid = valid || (c == '_') || (c == '-') || (c == '.') || (c == '#')
                            || (dirSeparators && ((c == '/') || (c == '\\')));

            if (valid) {
                rc.append(c);
            } else {
                // Encode the character using hex notation
                rc.append('#');
                rc.append(HexUtils.toHexFromInt(c, true));
            }
        }
        String result = rc.toString();
        if (result.length() > maxFileLength) {
            result = result.substring(result.length() - maxFileLength, result.length());
        }
        return result;
    }

}
