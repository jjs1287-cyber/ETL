  
                             
   
                                        
                                  
   
package org.etlforge.string;

import com.google.common.collect.Maps;
import java.io.Serializable;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.etlforge.string.StringMatcher;

public final class WildcardStringMatcher
implements StringMatcher,
Serializable {
    private final boolean caseSensitive;
    private Map<String, Pattern> cache;
    private boolean leadingMatchSufficient = true;

    public WildcardStringMatcher() {
        this(true);
    }

    public WildcardStringMatcher(boolean bl) {
        this.caseSensitive = bl;
    }

    public void setLeadingMatchSufficient(boolean bl) {
        this.leadingMatchSufficient = bl;
    }

    @Override
    public boolean matches(String string, String string2) {
        String string3 = this.caseSensitive ? string2 : string2.toLowerCase();
        Pattern pattern = this.getPattern(string);
        Matcher matcher = pattern.matcher(string3);
        if (matcher.find(0)) {
            int n = matcher.start();
            return n >= 0;
        }
        return false;
    }

    public void setCachingEnabled(boolean bl) {
        if (bl) {
            if (this.cache == null) {
                this.cache = Maps.newHashMap();
            }
        } else {
            this.cache = null;
        }
    }

    private Pattern getPattern(String string) {
        Pattern pattern;
        if (!this.caseSensitive) {
            string = string.toLowerCase();
        }
        if (this.cache != null && (pattern = this.cache.get(string)) != null) {
            return pattern;
        }
        return this.createPattern(string);
    }

    private Pattern createPattern(String string) {
        String string2 = this.createRegEx(string);
        Pattern pattern = Pattern.compile(string2, 32);
        if (this.cache != null) {
            this.cache.put(string, pattern);
        }
        return pattern;
    }

    private String createRegEx(String string) {
        boolean bl = false;
        if (this.leadingMatchSufficient) {
            if (string.endsWith(" ")) {
                string = string.substring(0, string.length() - 1);
                bl = true;
            }
        } else {
            bl = true;
        }
        StringBuilder stringBuilder = new StringBuilder("\\A");
        StringTokenizer stringTokenizer = new StringTokenizer(string, "*?", true);
        while (stringTokenizer.hasMoreTokens()) {
            String string2 = stringTokenizer.nextToken();
            char c = string2.charAt(0);
            if (c == '*') {
                stringBuilder.append(".*");
                continue;
            }
            if (c == '?') {
                stringBuilder.append(".");
                continue;
            }
            stringBuilder.append(Pattern.quote(string2));
        }
        if (bl) {
            stringBuilder.append("\\z");
        }
        return stringBuilder.toString();
    }
}

