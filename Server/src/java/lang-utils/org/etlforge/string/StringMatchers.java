  
                             
   
package org.etlforge.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.etlforge.string.StringMatcher;

public enum StringMatchers implements StringMatcher
{
    EQUALS{

        @Override
        public boolean matches(String string, String string2) {
            return string.equals(string2);
        }
    }
    ,
    EQUALS_IGNORE_CASE{

        @Override
        public boolean matches(String string, String string2) {
            return string.equalsIgnoreCase(string2);
        }
    }
    ,
    CONTAINS{

        @Override
        public boolean matches(String string, String string2) {
            return string2 != null && string.length() > 0 && string2.indexOf(string) >= 0;
        }
    }
    ,
    CONTAINS_IGNORE_CASE{

        @Override
        public boolean matches(String string, String string2) {
            return string2 != null && CONTAINS.matches(string.toLowerCase(), string2.toLowerCase());
        }
    }
    ,
    STARTS_WITH{

        @Override
        public boolean matches(String string, String string2) {
            return string2 != null && string.length() > 0 && string2.startsWith(string);
        }
    }
    ,
    STARTS_WITH_IGNORE_CASE{

        @Override
        public boolean matches(String string, String string2) {
            return string2 != null && STARTS_WITH.matches(string.toLowerCase(), string2.toLowerCase());
        }
    }
    ,
    ENDS_WITH{

        @Override
        public boolean matches(String string, String string2) {
            return string2 != null && string.length() > 0 && string2.endsWith(string);
        }
    }
    ,
    ENDS_WITH_IGNORE_CASE{

        @Override
        public boolean matches(String string, String string2) {
            return string2 != null && ENDS_WITH.matches(string.toLowerCase(), string2.toLowerCase());
        }
    }
    ,
    REGEX{

        @Override
        public boolean matches(String string, String string2) {
            return string2 != null && Pattern.matches(string, string2);
        }
    }
    ,
    REGEX_IGNORE_CASE{

        @Override
        public boolean matches(String string, String string2) {
            Pattern pattern = Pattern.compile(string, 2);
            Matcher matcher = pattern.matcher(string2);
            return matcher.matches();
        }
    };

}

