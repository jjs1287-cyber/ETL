  
                             
   
package org.etlforge.string;

import java.util.regex.Pattern;

public final class RegexUtils {
    public static String guidRegex() {
        String string = "[0-9a-fA-F]";
        String string2 = String.format("%s{8}-%s{4}-%s{4}-%s{4}-%s{12}", string, string, string, string, string);
        return string2;
    }

    public static Pattern guidPattern() {
        String string = RegexUtils.guidRegex();
        return Pattern.compile(string, 2);
    }

    private RegexUtils() {
    }
}

