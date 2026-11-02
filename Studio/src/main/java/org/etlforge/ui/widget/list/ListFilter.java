package org.etlforge.ui.widget.list;

public interface ListFilter {
   ListFilter ACCEPT_ALL = new ListFilter() {
      @Override
      public boolean test(Object var1) {
         return true;
      }
   };

   boolean test(Object var1);

   final class ToStringContains implements ListFilter {
      private final String fragment;

      public ToStringContains(String var1) {
         this.fragment = var1.toLowerCase();
      }

      @Override
      public boolean test(Object var1) {
         return var1 != null && var1.toString().toLowerCase().contains(this.fragment);
      }

      @Override
      public String toString() {
         return "toString contains: " + this.fragment;
      }
   }
}
