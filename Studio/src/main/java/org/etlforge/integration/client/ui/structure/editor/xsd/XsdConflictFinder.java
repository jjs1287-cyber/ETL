package org.etlforge.integration.client.ui.structure.editor.xsd;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.filetransfer.client.ServerFile;
import org.etlforge.util.crypto.DigestUtils;

final class XsdConflictFinder {
   public List<ServerFile> findConflicts(CreateXsdResult var1, Set<ServerFile> var2) {
      ArrayList var3 = Lists.newArrayList();
      if (var2 != null && !var2.isEmpty()) {
         Map var4 = toMap(var2);

         for (File var6 : var1.getXsds().getAllFiles()) {
            ServerFile var7 = (ServerFile)var4.get(var6.getName());
            if (var7 != null && !this.areIdentical(var6, var7)) {
               var3.add(var7);
            }
         }
      }

      return var3;
   }

   private static Map<String, ServerFile> toMap(Set<ServerFile> var0) {
      HashMap var1 = Maps.newHashMap();

      for (ServerFile var3 : var0) {
         var1.put(var3.getName(), var3);
      }

      return var1;
   }

   private boolean areIdentical(File var1, ServerFile var2) {
      String var3 = var2.getMD5();
      if (StringUtils.isEmpty(var3)) {
         return false;
      }

      try {
         String var4 = DigestUtils.getMd5FileDigestString(var1);
         return var4.equals(var2.getMD5());
      } catch (Exception var5) {
         var5.printStackTrace();
         return false;
      }
   }
}
