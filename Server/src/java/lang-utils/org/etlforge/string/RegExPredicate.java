  
                             
   
                                        
                                    
   
package org.etlforge.string;

import com.google.common.base.Predicate;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegExPredicate
implements Predicate<String>,
Serializable {
    private final Pattern pattern;
    private final boolean exactMatch;

    public RegExPredicate(String string) {
        this(string, true, 0);
    }

    public RegExPredicate(String string, boolean bl) {
        this(string, bl, 0);
    }

    public RegExPredicate(String string, int n) {
        this(string, true, n);
    }

    public RegExPredicate(String string, boolean bl, int n) {
        this.pattern = Pattern.compile(string, n);
        this.exactMatch = bl;
    }

    public boolean apply(String string) {
        Matcher matcher = this.pattern.matcher(string);
        return this.exactMatch ? matcher.matches() : matcher.find();
    }
}

