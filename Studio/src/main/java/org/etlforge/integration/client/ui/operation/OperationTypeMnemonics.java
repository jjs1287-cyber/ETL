package org.etlforge.integration.client.ui.operation;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.integration.data.entity.operation.OperationType;
import org.etlforge.ui.MnemonicString;

public final class OperationTypeMnemonics {
   private static final Map<OperationType, MnemonicString> strings = construct();

   public static MnemonicString get(OperationType var0) {
      return strings.get(var0);
   }

   private static Map<OperationType, MnemonicString> construct() {
      HashMap var0 = Maps.newHashMap();
      HashSet var1 = Sets.newHashSet();

      for (OperationType var3 : OperationType.values()) {
         String var4 = var3.toString();
         int var5 = getIndex(var4, var1);
         String var6 = var4.substring(0, var5) + "&" + var4.substring(var5);
         var0.put(var3, ApplicationResources.MNEMONIC_FACTORY.fromString(var6));
      }

      return var0;
   }

   private static int getIndex(String var0, Set<Character> var1) {
      int var2;
      for (var2 = 0; var2 < var0.length(); var2++) {
         char var3 = var0.charAt(var2);
         if (Character.isLetter(var3)) {
            char var4 = Character.toUpperCase(var3);
            if (!var1.contains(var4)) {
               var1.add(var4);
               break;
            }
         }
      }

      return var2;
   }

   private OperationTypeMnemonics() {
   }
}
