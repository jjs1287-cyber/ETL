  
                             
   
                                        
                                        
                                    
   
package org.etlforge.string;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.io.Serializable;
import org.etlforge.string.StringMatcher;

public final class StringMatcherPredicate
implements Predicate<String>,
Serializable {
    private final StringMatcher matcher;
    private final String searchString;

    public StringMatcherPredicate(StringMatcher stringMatcher, String string) {
        this.matcher = (StringMatcher)Preconditions.checkNotNull((Object)stringMatcher, (Object)"matcher");
        this.searchString = string;
    }

    public boolean apply(String string) {
        return this.matcher.matches(this.searchString, string);
    }
}

