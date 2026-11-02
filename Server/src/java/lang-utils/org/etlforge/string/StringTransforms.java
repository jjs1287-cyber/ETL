  
                             
   
                                        
                                   
   
package org.etlforge.string;

import com.google.common.base.Function;

public enum StringTransforms implements Function<String, String>
{
    TO_LOWER_CASE{

        public String apply(String string) {
            return string.toLowerCase();
        }
    }
    ,
    TRIM{

        public String apply(String string) {
            return string.trim();
        }
    };

}

