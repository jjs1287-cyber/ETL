package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils;

import java.awt.EventQueue;
import org.apache.commons.lang.StringUtils;

public final class NodeNameUtils {
   public static void setUserDefinedName(TreeComponent var0, Node var1, String var2) {
      String var3 = null;
      if (var2 == null || var0.m_flat) {
         var3 = var2;
      } else if (var1.isXml() || LdapUtils.isLdapAttribute(var0, var1)) {
         String var4 = var1.m_CROM.getNodeTypeMarker();
         if (var2.startsWith(var4)) {
            var3 = var2;
         } else {
            var3 = var4 + " " + var2;
         }
      } else if (!var0.m_flat) {
         var3 = var2 + var1.getCardinalityPostFix();
      }

      if (var3 != null) {
         var0.actualNodeDisplayNames.put(var1, var3);
      } else {
         var0.actualNodeDisplayNames.remove(var1);
      }
   }

   public static String getUserDefinedName(TreeComponent var0, Node var1) {
      String var2 = var0.actualNodeDisplayNames.get(var1);
      if (var2 == null) {
         return null;
      } else if (var1.isXml() || LdapUtils.isLdapAttribute(var0, var1)) {
         String var4 = var1.m_CROM.getNodeTypeMarker();
         return StringUtils.substringAfter(var2, var4).trim();
      } else if (!var0.m_flat) {
         String var3 = var1.getCardinalityPostFix();
         return StringUtils.substringBeforeLast(var2, var3).trim();
      } else {
         return var2;
      }
   }

   public static void askUserForNewName(final TreeComponent var0, final Node var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            NodeRenamer var1x = new NodeRenamer(var0, var1);
            var1x.rename();
         }
      });
   }

   private NodeNameUtils() {
   }
}
