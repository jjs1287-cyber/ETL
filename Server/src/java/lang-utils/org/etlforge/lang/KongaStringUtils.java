  
                             
   
                                        
                                        
                                     
                                       
   
package org.etlforge.lang;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.StringTokenizer;
import org.apache.commons.lang.StringUtils;
import org.etlforge.lang.IntParser;

public final class KongaStringUtils {
    public static final Comparator<String> NATURAL_ORDER = new NaturalOrder();

    public static int compareLength(String string, String string2) {
        return Ints.compare((int)string.length(), (int)string2.length());
    }

    public static boolean areEqual(String string, String string2) {
        return string == null ? string2 == null : string.equals(string2);
    }

    public static boolean stringsDiffer(String string, String string2) {
        return !KongaStringUtils.areEqual(string, string2);
    }

    public static String replaceStringIgnoringCase(String string, String string2, String string3) {
        int n;
        int n2 = 0;
        int n3 = string2.length();
        StringBuffer stringBuffer = new StringBuffer(1000);
        String string4 = string2.toUpperCase();
        String string5 = string.toUpperCase();
        while ((n = string5.indexOf(string4, n2)) != -1) {
            if (n > n2) {
                stringBuffer.append(string.substring(n2, n));
            }
            stringBuffer.append(string3);
            n2 += n3;
        }
        if (n2 == 0) {
            return string;
        }
        if (n2 < string.length()) {
            stringBuffer.append(string.substring(n2));
        }
        return stringBuffer.toString();
    }

    public static int compare(String string, String string2) {
        if (string == null) {
            return string2 == null ? 0 : -1;
        }
        if (string2 == null) {
            return 1;
        }
        return string.compareTo(string2);
    }

    public static String unescape(String string, char c) {
        StringTokenizer stringTokenizer = new StringTokenizer(string, new String(new char[]{c}), true);
        StringBuffer stringBuffer = new StringBuffer();
        boolean bl = true;
        while (stringTokenizer.hasMoreTokens()) {
            String string2 = stringTokenizer.nextToken();
            if (string2.charAt(0) == c) {
                if (bl) {
                    bl = false;
                    continue;
                }
                bl = true;
            }
            stringBuffer.append(string2);
        }
        return stringBuffer.toString();
    }

    public static boolean CSV_RequireQualifier(String string, String string2) {
        String string3 = string2 + "\r\n";
        if (string.isEmpty()) {
            return true;
        }
        if (string.charAt(0) == '\"') {
            return true;
        }
        int n = string.length();
        for (int i = 0; i < n; ++i) {
            char c = string.charAt(i);
            if (string3.indexOf(c) < 0) continue;
            return true;
        }
        return false;
    }

    public static String CSV_EscapeDoubleQuote(String string) {
        StringBuffer stringBuffer = new StringBuffer();
        StringTokenizer stringTokenizer = new StringTokenizer(string, "\"", true);
        while (stringTokenizer.hasMoreTokens()) {
            String string2 = stringTokenizer.nextToken();
            if (string2.charAt(0) == '\"') {
                stringBuffer.append(string2);
            }
            stringBuffer.append(string2);
        }
        return stringBuffer.toString();
    }

    public static String[] split(String string, String string2, char c) {
        Preconditions.checkNotNull((Object)string, (Object)"inputString");
        Preconditions.checkNotNull((Object)string2, (Object)"delimiter");
        Preconditions.checkArgument((c != '\u0000' ? 1 : 0) != 0, (Object)"escapeChar cannot be '\\0'");
        String string3 = string2 + c;
        StringTokenizer stringTokenizer = new StringTokenizer(string, string3, true);
        boolean bl = false;
        StringBuffer stringBuffer = new StringBuffer();
        ArrayList<String> arrayList = new ArrayList<String>();
        while (stringTokenizer.hasMoreTokens()) {
            String string4 = stringTokenizer.nextToken();
            if (string4.charAt(0) == c) {
                boolean bl2 = bl = !bl;
                if (bl) {
                    continue;
                }
            } else if (string2.indexOf(string4) >= 0) {
                if (!bl) {
                    arrayList.add(KongaStringUtils.unescape(stringBuffer.toString(), c));
                    stringBuffer.setLength(0);
                    continue;
                }
                bl = false;
            }
            stringBuffer.append(string4);
        }
        if (stringBuffer.length() > 0) {
            arrayList.add(stringBuffer.toString());
        }
        return arrayList.toArray(new String[0]);
    }

    public static String[] split(String string) {
        Preconditions.checkNotNull((Object)string, (Object)"inputString");
        StringTokenizer stringTokenizer = new StringTokenizer(string, "\t", true);
        ArrayList<String> arrayList = new ArrayList<String>();
        String string2 = "";
        while (stringTokenizer.hasMoreTokens()) {
            String string3 = stringTokenizer.nextToken();
            if (string3.equals("\t")) {
                if (string2 == null) {
                    string2 = "";
                }
                arrayList.add(string2);
                string2 = null;
                continue;
            }
            int n = string3.indexOf(92);
            if (n > 0) {
                int n2 = 0;
                int n3 = string3.length();
                StringBuilder stringBuilder = new StringBuilder(string2);
                while (n > 0) {
                    stringBuilder.append(string3.substring(n2, n));
                    switch (string3.charAt(n + 1)) {
                        case 'n': {
                            stringBuilder.append('\n');
                            break;
                        }
                        case 'r': {
                            stringBuilder.append('\r');
                            break;
                        }
                        case 't': {
                            stringBuilder.append('\t');
                        }
                    }
                    n2 = n + 2;
                    if (n2 >= n3) break;
                    n = string3.indexOf(92, n2);
                }
                string2 = stringBuilder.toString();
                continue;
            }
            string2 = string3;
        }
        if (string2 != null) {
            arrayList.add(string2);
        }
        return arrayList.toArray(new String[0]);
    }

    public static String[] splitWithFirstTab(String string) {
        ArrayList<String> arrayList = new ArrayList<String>();
        int n = string.indexOf(9);
        if (n >= 0) {
            arrayList.add(string.substring(0, n));
            String string2 = string.substring(n + 1);
            arrayList.add(string2);
        }
        return arrayList.toArray(new String[arrayList.size()]);
    }

    public static int parseInt(String string, int n) {
        return IntParser.parse(string, n);
    }

    public static String collapseSpaces(String string) {
        if (string.indexOf("  ") == -1) {
            return string;
        }
        StringBuilder stringBuilder = new StringBuilder();
        int n = string.length();
        boolean bl = false;
        for (int i = 0; i < n; ++i) {
            char c = string.charAt(i);
            if (c == ' ') {
                if (bl) continue;
                bl = true;
            } else {
                bl = false;
            }
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    public static String unquote(String string) {
        if (string == null) {
            return null;
        }
        int n = string.length();
        if (n < 2) {
            return string;
        }
        if (string.charAt(0) == '\'' && string.charAt(n - 1) == '\'' || string.charAt(0) == '\"' && string.charAt(n - 1) == '\"') {
            return string.substring(1, n - 1);
        }
        return string;
    }

    public static String findLongestCommonStartFragment(Iterable<String> iterable, boolean bl) {
        String string = null;
        for (String string2 : iterable) {
            char c;
            char c2;
            if (string == null) {
                string = string2;
                continue;
            }
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < string.length() && i < string2.length() && KongaStringUtils.isSame(c2 = string.charAt(i), c = string2.charAt(i), bl); ++i) {
                stringBuilder.append(c2);
            }
            if (stringBuilder.length() == 0) {
                return "";
            }
            string = stringBuilder.toString();
        }
        return string;
    }

    private static boolean isSame(char c, char c2, boolean bl) {
        return c == c2 || bl && Character.toLowerCase(c) == Character.toLowerCase(c2);
    }

    public static String escapeDoubleQuotes(String string) {
        int n = string.indexOf(34);
        if (n == -1) {
            return string;
        }
        StringBuilder stringBuilder = new StringBuilder();
        int n2 = 0;
        while (n >= 0) {
            stringBuilder.append(string.substring(n2, n + 1));
            n2 = n;
            n = string.indexOf(34, n + 1);
        }
        stringBuilder.append(string.substring(n2));
        return stringBuilder.toString();
    }

    public static String removeStartAndEnd(String string, String string2) {
        return KongaStringUtils.removeStartAndEnd(string, string2, string2);
    }

    public static String removeStartAndEnd(String string, String string2, String string3) {
        if (StringUtils.isEmpty((String)string)) {
            return string;
        }
        String string4 = StringUtils.removeStart((String)string, (String)string2);
        return StringUtils.removeEnd((String)string4, (String)string3);
    }

    public static String removeStartAndEndIfBothPresent(String string, String string2) {
        return KongaStringUtils.removeStartAndEndIfBothPresent(string, string2, string2);
    }

    public static String removeStartAndEndIfBothPresent(String string, String string2, String string3) {
        if (StringUtils.isEmpty((String)string)) {
            return string;
        }
        if (string.startsWith(string2) && string.endsWith(string3)) {
            return string.substring(string2.length(), string.length() - string3.length());
        }
        return string;
    }

    public static String unwrap(String string) {
        return string.substring(1, string.length() - 1);
    }

    public static int indexOfAny(String string, String string2) {
        return KongaStringUtils.indexOfAny(string, string2, 0);
    }

    public static int indexOfAny(String string, String string2, int n) {
        if (n >= string.length()) {
            return -1;
        }
        for (int i = Math.max(n, 0); i < string.length(); ++i) {
            if (string2.indexOf(string.charAt(i)) < 0) continue;
            return i;
        }
        return -1;
    }

    public static int lastIndexOfAny(String string, String string2) {
        return KongaStringUtils.lastIndexOfAny(string, string2, string.length() - 1);
    }

    public static int lastIndexOfAny(String string, String string2, int n) {
        for (int i = Math.min(n, string.length() - 1); i >= 0; --i) {
            if (string2.indexOf(string.charAt(i)) < 0) continue;
            return i;
        }
        return -1;
    }

    public static String[] splitInTwo(String string, String string2) {
        int n = string.indexOf(string2);
        if (n < 0) {
            return new String[]{string, ""};
        }
        return new String[]{string.substring(0, n), string.substring(n)};
    }

    public static String insertAt(String string, String string2, String string3) {
        String[] stringArray = KongaStringUtils.splitInTwo(string, string3);
        return stringArray[0] + string2 + stringArray[1];
    }

    public static String ensureEndsWith(String string, String string2) {
        return string.endsWith(string2) ? string : string + string2;
    }

    public static String toHexString(byte by) {
        return String.format("%02x", by & 0xFF);
    }

    public static String toHexString(byte[] byArray) {
        StringBuilder stringBuilder = new StringBuilder(byArray.length);
        for (byte by : byArray) {
            stringBuilder.append(KongaStringUtils.toHexString(by));
        }
        return stringBuilder.toString();
    }

    public static String toHexString(byte[] byArray, int n, int n2) {
        if (n == 0 && n2 == byArray.length) {
            return KongaStringUtils.toHexString(byArray);
        }
        StringBuilder stringBuilder = new StringBuilder(n2);
        int n3 = n + n2;
        for (int i = n; i < n3; ++i) {
            stringBuilder.append(KongaStringUtils.toHexString(byArray[i]));
        }
        return stringBuilder.toString();
    }

    public static int positionOfLastNoneWhitespaceBefore(String string, int n) {
        for (int i = n - 1; i >= 0; --i) {
            char c = string.charAt(i);
            if (Character.isWhitespace(c)) continue;
            return i;
        }
        return -1;
    }

    public static boolean hasXmlEscape(String string) {
        return string.indexOf(38) >= 0 && string.indexOf(59) > 0;
    }

    public static String substringAfterIfPresent(String string, String string2) {
        int n = string.indexOf(string2);
        if (n == -1) {
            return string;
        }
        return string.substring(n + string2.length());
    }

    public static String substringAfterLastIfPresent(String string, String string2) {
        int n = string.lastIndexOf(string2);
        if (n == -1) {
            return string;
        }
        return string.substring(n + string2.length());
    }

    private KongaStringUtils() {
    }

    private static final class NaturalOrder
    implements Comparator<String>,
    Serializable {
        private NaturalOrder() {
        }

        @Override
        public int compare(String string, String string2) {
            return string.compareTo(string2);
        }

        public String toString() {
            return "Natural order";
        }
    }
}

