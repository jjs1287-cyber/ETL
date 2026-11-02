package org.etlforge.integration.client.entity.search;

import com.google.common.base.Predicate;
import java.util.List;

public abstract class StringListProperty extends AbstractSearchableProperty<StringList> {
   private StringList strings;

   public StringListProperty(String var1) {
      this(var1, new StringList());
   }

   public StringListProperty(String var1, StringList var2) {
      super(var1, StringList.class);
      this.strings = var2;
   }

   protected final void setStrings(StringList var1) {
      this.strings = var1;
   }

   protected final List<String> getStrings() {
      return this.strings;
   }

   public final boolean contains(Predicate<String> var1) {
      for (String var3 : this.strings) {
         if (var1.apply(var3)) {
            return true;
         }
      }

      return false;
   }
}
