package org.etlforge.integration.data.script;

import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.lang.KongaStringUtils;

public final class GrammarUtils {
   public static String idToPrefix(Class<? extends IntegrationEntityId> var0) {
      FunctionRegistry var1 = FunctionRegistry.getRegistry();
      String var2 = var1.getPrefixForProjectItemArgumentId(var0).orNull();
      return var2 == null ? null : var2 + ".";
   }

   public static int getIndexOfNextId(String var0, int var1) {
      FunctionRegistry var2 = FunctionRegistry.getRegistry();
      Iterable var3 = var2.getAllProjectItemArgumentPrefixes();
      int var4 = -1;

      for (Object __etlItem26 : var3) {
         String var6 = (String) __etlItem26;
         String var7 = "\"" + var6 + ".";
         int var8 = var0.indexOf(var7, var1);
         if (var8 >= 0) {
            if (var4 < 0) {
               var4 = var8;
            } else {
               var4 = var4 < var8 ? var4 : var8;
            }
         }
      }

      return var4;
   }

   private static String stripTrailingBrace(String var0) {
      return StringUtils.removeEnd(var0, "(");
   }

   public static Set<? extends IntegrationEntityId> getAllIdsFromTransformation(Transformation var0) {
      HashSet var1 = Sets.newHashSet();
      Transform var2 = new Transform();

      for (String var5 : var0.getMapList()) {
         String[] var6 = KongaStringUtils.splitWithFirstTab(var5);
         String var7 = var6[1];
         String var8 = var6[0];
         String var9 = "The script is for the target tree node '" + var8 + "' of the transformation '" + var0.getName() + "'.";

         try {
            var1.addAll(getAllIdsFromScript(var2, var7, var9));
         } catch (Exception var11) {
            ErrorLog.internal(GrammarUtils.class, "Error parsing the mapping expressions for the target node " + var8 + " in the Transformation " + var0, var11);
         }
      }

      return var1;
   }

   private static boolean isFunctionCallWithProjectItems(Transform.Token var0) {
      if (var0.m_id != 101) {
         return false;
      }

      String var1 = stripTrailingBrace(var0.getStr());
      return FunctionRegistry.getRegistry().doesFunctionUseProjectItems(var1);
   }

   public static Set<? extends IntegrationEntityId> getAllIdsFromScript(Transform var0, String var1, String var2) {
      HashSet var3 = Sets.newHashSet();
      List var4 = var0.tokenize(var1);
      if (var4 == null) return var3;
      if(var1.contains("<javascript>")) {
         for(String reference:NativeJavaScript.references(var1))if(reference.startsWith("P:")){
            int split=reference.indexOf(':',2);String name=reference.substring(2,split),value=reference.substring(split+1);
            Function function=FunctionRegistry.getRegistry().getFunctionByName(name.equals("DbLookup")?"DBLookup":name);
            if(function instanceof ProjectItemFunction){try{com.google.common.base.Optional<? extends IntegrationEntityId> id=((ProjectItemFunction)function).getItemId("\""+value+"\"");if(id.isPresent())var3.add(id.get());}catch(RuntimeException invalidReference){}}
         }
      }
      Iterator var5 = var4.iterator();

      while (var5.hasNext()) {
         Transform.Token var6 = (Transform.Token)var5.next();
         if (isFunctionCallWithProjectItems(var6)) {
            String var7 = stripTrailingBrace(var6.getStr());
            boolean var8 = var5.hasNext() && (var6 = (Transform.Token)var5.next()).m_id == 401;
            if (var8) {
               try {
                  ProjectItemFunction var9 = (ProjectItemFunction)FunctionRegistry.getRegistry().getFunctionByName(var7);
                  IntegrationEntityId var10 = var9.getItemId(var6.getStr()).orNull();
                  if (var10 == null) {
                     throw new Exception("No matching ProjectItemDescriptor was found.");
                  }

                  var3.add(var10);
               } catch (Exception var11) {
                  ErrorLog.log(GrammarUtils.class, "Could not resolve the argument '" + var6.getStr() + "' of '" + var7 + "' to an id. " + var2, var11);
               }
            }
         }
      }

      return var3;
   }

   private GrammarUtils() {
   }
}
