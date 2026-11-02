package org.etlforge.integration.client.ui.entity.search;

import org.etlforge.integration.client.entity.search.ReplaceableProperty;
import org.etlforge.integration.data.entity.IntegrationEntity;

abstract class Replacer<T> {
   public static Replacer getReplacer(PropertySearchField var0, SearchOptions var1) {
      ReplaceableProperty var2 = (ReplaceableProperty)var0.getProperty();
      Class var3 = var2.getValueType();
      if (var3.equals(String.class)) {
         String var4 = (String)Utils.getValue(var0);
         return new Replacer.StringReplacer(var2, var4, var1);
      } else {
         return new Replacer.DefaultReplacer(var2);
      }
   }

   public abstract void doReplace(IntegrationEntity var1, T var2);

   private static class DefaultReplacer extends Replacer {
      private final ReplaceableProperty property;

      public DefaultReplacer(ReplaceableProperty var1) {
         this.property = var1;
      }

      @Override
      public void doReplace(IntegrationEntity var1, Object var2) {
         this.property.setValue(var1, var2);
      }
   }

   private static class StringReplacer extends Replacer<String> {
      private final ReplaceableProperty<String> property;
      private final String searchString;
      private final SearchOptions options;

      public StringReplacer(ReplaceableProperty<String> var1, String var2, SearchOptions var3) {
         this.property = var1;
         this.searchString = var2;
         this.options = var3;
      }

      public void doReplace(IntegrationEntity var1, String var2) {
         String var3 = this.property.getValue(var1);
         String var4 = this.options.replaceValue(var3, this.searchString, var2);
         this.property.setValue(var1, var4);
      }
   }
}
