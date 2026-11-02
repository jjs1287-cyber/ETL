package org.etlforge.integration.client.ui.admin.core.console;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import org.etlforge.collections.Collections2;
import org.etlforge.collections.KongaMaps;
import org.etlforge.system.KeyValue;

public final class AdminConsolePartDisplayOrder {
   private static final String PREF_NAME = "PartDisplayOrder";
   private static final String SEPARATOR = ",";
   private static final Function<AdminConsolePart, String> PART_TO_ID = new Function<AdminConsolePart, String>() {
      public String apply(AdminConsolePart var1) {
         return var1.getId();
      }
   };
   private final KeyValue<String, String> stringStore;

   public AdminConsolePartDisplayOrder(KeyValue<String, String> var1) {
      this.stringStore = var1;
   }

   public List<AdminConsolePart> getDisplayOrder(Collection<? extends AdminConsolePart> var1) {
      LinkedHashMap var2 = KongaMaps.toLinkedHashMap(var1, PART_TO_ID);
      ArrayList var3 = Lists.newArrayList();

      for (String var7 : this.getConsoleIDsInOrder()) {
         AdminConsolePart var8 = (AdminConsolePart)var2.remove(var7);
         if (var8 != null) {
            var3.add(var8);
         }
      }

      var3.addAll(var2.values());
      return var3;
   }

   private String[] getConsoleIDsInOrder() {
      String var1 = this.stringStore.get("PartDisplayOrder", null);
      return var1 != null ? var1.split(",") : new String[0];
   }

   public void updateOrder(List<AdminConsolePart> var1) {
      String var2 = Collections2.join(var1, PART_TO_ID, ",");
      this.stringStore.set("PartDisplayOrder", var2);
   }
}
