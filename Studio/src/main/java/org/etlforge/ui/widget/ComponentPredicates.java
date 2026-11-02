package org.etlforge.ui.widget;

import com.google.common.base.Predicate;
import java.awt.Component;

public enum ComponentPredicates implements Predicate<Component> {
   ENABLED {
      public boolean apply(Component var1) {
         return var1.isEnabled();
      }
   };

   ComponentPredicates() {
   }
}
