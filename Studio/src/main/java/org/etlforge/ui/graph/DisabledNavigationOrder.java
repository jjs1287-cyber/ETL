package org.etlforge.ui.graph;

public class DisabledNavigationOrder implements NavigationOrder {
   @Override
   public Object next(Object var1) {
      return var1;
   }

   @Override
   public Object previous(Object var1) {
      return var1;
   }
}
