package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import com.google.common.collect.Sets;
import java.util.Set;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;

final class WarningRules {
   private static final Set<Integer> warningRules = Sets.newHashSet();

   private static void load() {
      addDataTypeWarningPairs(0, 5);
      addDataTypeWarningPairs(1, 5);
      addDataTypeWarningPairs(4, 5);
      addDataTypeWarningPairs(3, 5);
      addDataTypeWarningPairs(2, 5);
      addDataTypeWarningPairs(9, 0);
      addDataTypeWarningPairs(9, 1);
      addDataTypeWarningPairs(9, 2);
      addDataTypeWarningPairs(9, 3);
      addDataTypeWarningPairs(9, 5);
      addDataTypeWarningPairs(6, 5);
   }

   public static void addDataTypeWarningPairs(int var0, int var1) {
      int var2 = combineIndex(var0, var1);
      warningRules.add(var2);
   }

   private static int combineIndex(int var0, int var1) {
      return var0 | var1 << 16;
   }

   public static boolean hasDataTypeWarning(Node var0, Node var1) {
      int var2 = var0.getCROM().getDataType();
      int var3 = var1.getCROM().getDataType();
      int var4 = combineIndex(var2, var3);
      return warningRules.contains(var4);
   }

   private WarningRules() {
   }

   static {
      load();
   }
}
