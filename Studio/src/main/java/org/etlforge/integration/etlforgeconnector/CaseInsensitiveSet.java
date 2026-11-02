package org.etlforge.integration.etlforgeconnector;

import com.google.common.collect.Sets;
import java.io.Serializable;
import java.util.Set;

public class CaseInsensitiveSet implements Serializable {
   private final Set<String> set = Sets.newHashSet();

   public CaseInsensitiveSet(Set<String> var1) {
      for (String var3 : var1) {
         this.put(var3);
      }
   }

   public void put(String var1) {
      this.set.add(var1.toLowerCase());
   }

   public boolean contains(String var1) {
      return this.set.contains(var1.toLowerCase());
   }
}
