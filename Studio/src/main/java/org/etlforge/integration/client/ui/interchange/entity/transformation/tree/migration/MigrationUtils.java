package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.migration;

import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.data.script.ScriptConstants;
import org.etlforge.integration.data.script.ScriptException;
import org.etlforge.integration.data.script.Transform;
import org.etlforge.integration.structure.mapping.PathSeparator;

public final class MigrationUtils {
   public static void migrateMapping(List<String[]> var0, Map<Node, Node> var1, Map<Node, Node> var2, Migration var3) throws ScriptException {
      HashMap var4 = Maps.newHashMap();

      for (Entry var6 : var1.entrySet()) {
         var4.put(((Node)var6.getKey()).m_deName, ((Node)var6.getValue()).m_deName);
      }

      HashMap var16 = Maps.newHashMap();

      for (Entry var7 : var2.entrySet()) {
         String var8 = ((Node)var7.getKey()).m_deName;
         String var9 = ((Node)var7.getValue()).m_deName;
         List var10 = (List)var16.get(var9);
         if (var10 == null) {
            var10 = new ArrayList();
            var16.put(var9, var10);
         }

         var10.add(var8);
      }

      ArrayList var18 = new ArrayList();

      for (String[] var21 : var0) {
         var18.add(var21);
      }

      var0.clear();

      for (Object __etlItem46 : var18) {
         String[] var22 = (String[]) __etlItem46;
         String var23 = var22[0];
         String var24 = var22[1];
         List var11 = (List)var16.get(var23);
         if (var11 != null) {
            for (Object __etlItem51 : var11) {
               String var13 = (String) __etlItem51;
               String var14 = "";
               if (!var13.equals("")) {
                  var14 = translateSourceDE(var24, var23, var4, var3);
                  String[] var15 = new String[]{var13, var14};
                  var0.add(var15);
               }
            }
         }
      }
   }

   public static String translateTargetDE(String var0, Map<String, String> var1) {
      String var2 = (String)var1.get(var0);
      if (var2 == null) {
         var2 = "";
      }

      return var2;
   }

   public static String translateSourceDE(String var0, String var1, Map<String, String> var2, Migration var3) throws ScriptException {
      StringBuilder var4 = new StringBuilder();
      int var5 = 0;

      int var6;
      while ((var6 = var0.indexOf("<trans>", var5)) >= 0) {
         var6 += ScriptConstants.BEGIN_TRANS_LENGTH;
         int var7 = Transform.find_END_TRANS_position(var0, var6);
         if (var7 < 0) {
            throw new ScriptException("Target:" + var1 + "\nMissing closing tag " + "</trans>" + "\nExpression:\n" + var0);
         }

         var4.append(var0.substring(var5, var6));
         Transform var8 = new Transform();
         var8.initTokenizer(var0, var6, var7);
         boolean var9 = true;
         boolean var10 = false;

         while (true) {
            Transform.Token var11;
            if ((var11 = var8.nextToken(var9, var10)) != null) {
               int var12 = var11.getId();
               if (var12 != -2) {
                  if (var12 == 400) {
                     return null;
                  }

                  if (var12 == 405) {
                     String var13 = var11.getStr();
                     String var14 = (String)var2.get(var13);
                     if (var14 != null) {
                        var4.append(var14);
                     } else {
                        StringTokenizer var15 = new StringTokenizer(var13, "#.", true);
                        String var16 = "";
                        StringBuilder var17 = new StringBuilder();
                        String var18 = "";
                        boolean var19 = false;

                        while (var15.hasMoreTokens()) {
                           String var20 = var15.nextToken();
                           if (var20.charAt(0) == '#') {
                              StringBuilder var21 = new StringBuilder("#");

                              while (var15.hasMoreTokens()) {
                                 var20 = var15.nextToken();
                                 var21.append(var20);
                                 if (PathSeparator.startsWithLoopSeparator(var20)) {
                                    break;
                                 }
                              }

                              var17.append(".");
                              int var22 = var18.length();
                              String var23 = (String)var2.get("[" + var17 + "]");
                              if (var23 == null) {
                                 var19 = true;
                              } else {
                                 if (var23.charAt(0) == '[') {
                                    var18 = var23.substring(1, var23.length() - 1);
                                 }

                                 int var24 = var18.length();
                                 if (PathSeparator.endsWithLoopSeparator(var18)) {
                                    var16 = var16 + var18.substring(var22, var24 - 1) + var21;
                                 } else {
                                    var16 = var16 + var18.substring(var22, var24);
                                 }
                              }
                           } else {
                              var17.append(var20);
                           }
                        }

                        int var28 = var18.length();
                        var18 = (String)var2.get(var17.toString());
                        if (var18 != null) {
                           if (var28 < var18.length()) {
                              var16 = var16 + var18.substring(var28, var18.length());
                           }

                           if (var19) {
                              String var29 = "Target: " + var1;
                              var16 = var3.manualConversion(var29, var13, var16);
                           }

                           if (var16 != null) {
                              var4.append(var16);
                           }
                        }

                        if (var16 == null || var18 == null) {
                           String var30 = "Target: " + var1;
                           String var31 = "Failed to translate node: " + var13 + "\nin the following expression:";
                           String var32 = "Do you want to skip the migration for this mapping?";
                           if (var3.checkToContinue(var30, var31, var0, var32)) {
                              return null;
                           } else {
                              throw new ScriptException(
                                 "Error in migrating the mapping\n\nThe target node: "
                                    + var1
                                    + "\nFailed to translate the node:\n"
                                    + var13
                                    + "\nin the expression\n"
                                    + var0
                              );
                           }
                        }
                     }
                  } else {
                     var4.append(var11.getStr());
                  }

                  if (var12 != 1100 && var12 != 1101) {
                     var10 = var12 == 320 || var12 == 406 || var12 == 408;
                     var9 = var12 == 320
                        || var12 == 100
                        || var12 == 101
                        || var12 == 500
                        || var12 == 407
                        || var12 == 600
                        || var12 == 601
                        || var12 == 602
                        || var12 == 603
                        || var12 == 604
                        || var12 == 605
                        || var12 == 200
                        || var12 == 310;
                     var8.setLastId(var12);
                  }
                  continue;
               }
            }

            var4.append("</trans>");
            var5 = var7 + ScriptConstants.END_TRANS_LENGTH;
            break;
         }
      }

      var4.append(var0.substring(var5));
      return var4.toString();
   }

   private MigrationUtils() {
   }
}
