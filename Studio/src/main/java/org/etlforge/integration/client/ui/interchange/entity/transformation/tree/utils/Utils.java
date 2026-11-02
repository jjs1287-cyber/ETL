package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.data.script.ScriptUtils;
import org.etlforge.integration.data.script.Transform;
import org.etlforge.integration.structure.mapping.PathSeparator;
import org.etlforge.lang.KongaStringUtils;

public final class Utils {
   public static String removeBrackets(String var0) {
      return KongaStringUtils.removeStartAndEndIfBothPresent(var0, "[", "]");
   }

   public static String replaceString(String var0, String var1, String var2) {
      int var3 = 0;
      int var4 = var1.length();
      int var5 = var2.length();
      String var6 = var0;

      while ((var3 = var6.indexOf(var1, var3)) >= 0) {
         var6 = var6.substring(0, var3) + var2 + var6.substring(var3 + var4);
         var3 += var5;
      }

      return var6;
   }

   public static List<String[]> readMapFile(List<String> var0) {
      ArrayList var1 = Lists.newArrayList();

      for (String var3 : var0) {
         String[] var4 = KongaStringUtils.splitWithFirstTab(var3);
         var1.add(var4);
      }

      return var1;
   }

   public static Set<Node> getSourceExpressionList(TreeComponent var0, String var1, Transform var2) {
      HashSet var3 = Sets.newHashSet();
      List var4 = var2.getRpnTokenVector(var1);
      if (var4 != null) {
         for (Object __etlItem49 : Transform.getDEVector(var4)) {
            String var6 = (String) __etlItem49;
            var6 = ScriptUtils.stripInstanceTag(var6);
            Node var7;
            if ((var7 = var0.findNode_expandTreeIfNotExist(var6)) != null) {
               var3.add(var7);
            }
         }
      }

      return var3;
   }

   public static Node insertMultipleMapping(Node var0, int var1) {
      TreeComponent var2 = var0.getTreeComponent();
      var2.setDirty();
      String var3 = removeBrackets(var0.m_deName);
      int var4 = var3.length();
      int var5 = var4 - 1;

      for (int var6 = var4 - 2; var6 >= 0; var6--) {
         char var7 = var3.charAt(var6);
         if (PathSeparator.isSeparator(var7)) {
            break;
         }

         if (var7 == '#') {
            var5 = var6;
            break;
         }
      }

      String var15;
      if (var2.m_flat) {
         if (var3.equals("")) {
            var15 = "#" + var1;
         } else {
            var15 = var3.substring(0, var3.length() - 1) + "#" + var1 + var3.charAt(var3.length() - 1);
         }
      } else {
         var15 = var3.substring(0, var5) + "#" + var1 + var3.charAt(var4 - 1);
      }

      if (var2.m_deHashtable.get("[" + var15 + "]") != null) {
         return null;
      }

      Node var9 = var0;
      if (var2.m_flat) {
         for (int var10 = var1 - 1; var10 > 0; var10--) {
            String var16;
            if (var3.equals("")) {
               var16 = "#" + var10;
            } else {
               var16 = var3.substring(0, var3.length() - 1) + "#" + var10 + var3.charAt(var3.length() - 1);
            }

            Node var8 = var2.m_deHashtable.get("[" + var16 + "]");
            if (var8 != null) {
               var9 = var8;
               break;
            }
         }
      } else {
         for (int var20 = var1 - 1; var20 > 0; var20--) {
            String var17 = var3.substring(0, var5) + "#" + var20 + var3.charAt(var4 - 1);
            Node var19 = var2.m_deHashtable.get("[" + var17 + "]");
            if (var19 != null) {
               var9 = var19;
               break;
            }
         }
      }

      Node var21 = var0.m_parent.insertElement(var0, var0.m_parent.getIndexOfChild(var9) + 1, var1);
      String var18 = var0.getDisplay();
      int var11 = 0;
      int var13 = 0;
      int var12;
      if ((var12 = var18.indexOf(35)) >= 0) {
         var13 = var18.length();
         var11 = var12 + 1;

         while (var11 < var13 && Character.isDigit(var18.charAt(var11))) {
            var11++;
         }

         if (var11 == var13 && var13 > var12) {
            var18 = var18.substring(0, var12);
         }
      }

      var21.setDisplay(var18 + "#" + var1);
      var2.m_root.reOrder(0);
      TreeMapper var14 = var2.getTreeMapper();
      if (var14 != null) {
         var14.getMappingManager().reOrder();
      }

      var21.setMultipleMapFlag();
      var0.setHasMultipleMapFlag();
      var2.repaint();
      return var21;
   }

   public static Node getMultipleMappingOriginal(Node var0) {
      return var0 != null && var0.isMultipleMap() ? var0.getTreeComponent().findNode(ScriptUtils.stripInstanceTag(var0.m_deName)) : null;
   }

   private Utils() {
   }
}
