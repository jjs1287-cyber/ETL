  
                             
   
                                        
                                    
   
package org.etlforge.string;

import com.google.common.base.Predicate;
import java.io.Serializable;

public final class StringPredicates {
    public static final Predicate<String> contains(String string) {
        return new Contains(string);
    }

    public static final Predicate<String> containsIgnoreCase(String string) {
        return new ContainsIgnoreCase(string);
    }

    public static Predicate<String> startsWith(String string) {
        return new StartsWith(string);
    }

    public static Predicate<String> endsWith(String string) {
        return new EndsWith(string);
    }

    private StringPredicates() {
    }

    private static final class EndsWith
    implements Predicate<String>,
    Serializable {
        private final String suffix;

        private EndsWith(String string) {
            this.suffix = string;
        }

        public boolean apply(String string) {
            return string != null && string.endsWith(this.suffix);
        }

        public String toString() {
            return "ends-with \"" + this.suffix + "\"";
        }
    }

    private static final class StartsWith
    implements Predicate<String>,
    Serializable {
        private final String prefix;

        private StartsWith(String string) {
            this.prefix = string;
        }

        public boolean apply(String string) {
            return string != null && string.startsWith(this.prefix);
        }

        public String toString() {
            return "starts-with \"" + this.prefix + "\"";
        }
    }

    private static final class ContainsIgnoreCase
    implements Predicate<String>,
    Serializable {
        private final String searchString;

        public ContainsIgnoreCase(String string) {
            this.searchString = string.toLowerCase();
        }

        public boolean apply(String string) {
            return string != null && string.toLowerCase().contains(this.searchString);
        }

        public String toString() {
            return "contains (ignoring case) \"" + this.searchString + "\"";
        }
    }

    private static final class Contains
    implements Predicate<String>,
    Serializable {
        private final String searchString;

        public Contains(String string) {
            this.searchString = string;
        }

        public boolean apply(String string) {
            return string != null && string.contains(this.searchString);
        }

        public String toString() {
            return "contains \"" + this.searchString + "\"";
        }
    }
}

