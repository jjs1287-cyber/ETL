  
                             
   
                                        
                                   
                                  
                                        
                                       
   
package org.etlforge.string;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import org.apache.commons.lang.StringUtils;

public abstract class PrefixRemover
implements Function<String, String>,
Serializable {
    protected final String prefix;

    public static PrefixRemover withCase(String string) {
        return new WithCase(string);
    }

    public static PrefixRemover ignoringCase(String string) {
        return new IgnoringCase(string);
    }

    private PrefixRemover(String string) {
        this.prefix = (String)Preconditions.checkNotNull((Object)string);
    }

    private static class IgnoringCase
    extends PrefixRemover {
        public IgnoringCase(String string) {
            super(string);
        }

        public String apply(String string) {
            return StringUtils.removeStartIgnoreCase((String)string, (String)this.prefix);
        }

        public String toString() {
            return Objects.toStringHelper(PrefixRemover.class).add("prefix ignoring case", (Object)this.prefix).toString();
        }
    }

    private static class WithCase
    extends PrefixRemover {
        public WithCase(String string) {
            super(string);
        }

        public String apply(String string) {
            return StringUtils.removeStart((String)string, (String)this.prefix);
        }

        public String toString() {
            return Objects.toStringHelper(PrefixRemover.class).add("prefix with case", (Object)this.prefix).toString();
        }
    }
}

