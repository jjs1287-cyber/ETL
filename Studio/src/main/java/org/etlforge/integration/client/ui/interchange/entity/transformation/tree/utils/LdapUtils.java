package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils;

import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings.NodeMapping;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.mapping.NodePath;
import org.etlforge.lang.KongaStringUtils;

public final class LdapUtils {
   public static boolean isLdapClassEntry(TreeComponent var0, Node var1) {
      return var0.m_bIsLdapTarget && var1.isFolder()
         ? var1.m_parent != null
            && var1.m_parent.getName().equalsIgnoreCase("LDAP")
            && var1.m_parent.m_parent != null
            && var1.m_parent.m_parent.m_parent == null
         : false;
   }

   public static boolean isLdapAttribute(TreeComponent var0, Node var1) {
      return var1.isFolder() && var1.m_parent != null && isLdapClassEntry(var0, var1.m_parent);
   }

   public static boolean isLdapClassEntryCommand(TreeComponent var0, Node var1) {
      return var0.m_bIsLdapTarget && !var1.isFolder() && var1.getDisplayName().equals("Entry command")
         ? var1.m_parent != null && isLdapClassEntry(var0, var1.m_parent)
         : false;
   }

   public static boolean isLdapAttributeCommand(TreeComponent var0, Node var1) {
      return var0.m_bIsLdapTarget && !var1.isFolder() && var1.getDisplayName().equals("Attribute command")
         ? var1.m_parent != null && isLdapAttribute(var0, var1.m_parent)
         : false;
   }

   public static boolean isLdapAttributeReplaceAll(TreeComponent var0, Node var1) {
      return isLdapAttribute(var0, var1) && (var1.m_CROM.getType() & 512L) != 0L;
   }

   public static boolean isLdapAttributeDelete(TreeComponent var0, Node var1) {
      return isLdapAttribute(var0, var1) && (var1.m_CROM.getType() & 256L) != 0L;
   }

   public static void setLdapAttributeReplaceAll(TreeComponent var0, Node var1) {
      TreeMapper var2 = var0.getTreeMapper();
      if (var2 != null) {
         boolean var3 = isLdapAttributeDelete(var0, var1);
         boolean var4 = !var3 && isLdapAttributeReplaceAll(var0, var1);
         if (!var4) {
            String var5 = KongaStringUtils.unwrap(var1.m_deName);
            String var6 = var5 + "Attribute?command";
            String var7 = var5 + "Old?value";
            String var8 = "Replace all";
            Node var9 = var0.findNode(var6);
            Node var10 = var0.findNode(var7);
            var1.m_CROM.clearType(768L);
            if (var9 == null) {
               var9 = insertAttributeCommand(var0, var1);
            }

            if (var9 != null) {
               var1.m_CROM.addType(512L);
               NodeMapping var11 = var2.getNodeMappingFactory().newMapping(new NodePath(var6), var8);
               var2.getMappingManager().addMapping(var11);
               if (var10 != null) {
                  var0.getTreeMapper().deleteMapping(new NodePath(var7));
               }

               var2.updateMappingFlag();
               var2.updateMapAreaTables(var6, var8);
               var2.getMappingManager().changeHighLighted(var11);
               var2.setDirty(true);
            }
         }
      }
   }

   public static void setLdapAttributeRemove(TreeComponent var0, Node var1, boolean var2) {
      TreeMapper var3 = var0.getTreeMapper();
      if (var3 != null) {
         boolean var4 = isLdapAttributeDelete(var0, var1);
         boolean var5 = !var4 && isLdapAttributeReplaceAll(var0, var1);
         String var6 = KongaStringUtils.unwrap(var1.m_deName);
         String var7 = var6 + "Attribute?command";
         String var8 = var6 + "Old?value";
         String var9 = "Remove";
         Node var10 = var0.findNode(var7);
         Node var11 = var0.findNode(var8);
         if (var2) {
            if (var4) {
               return;
            }

            if (var10 == null) {
               var10 = insertAttributeCommand(var0, var1);
            }

            if (var10 == null) {
               return;
            }

            var1.m_CROM.clearType(512L);
            var1.m_CROM.addType(256L);
            NodeMapping var12 = var3.getNodeMappingFactory().newMapping(new NodePath(var7), var9);
            var3.getMappingManager().addMapping(var12);
            var0.getTreeMapper().deleteMapping(new NodePath(var6));
            if (var11 == null && var1.isLoopNode()) {
               insertOldValueNode(var0, var1);
            }

            var3.updateMappingFlag();
            var3.updateMapAreaTables(var7, var9);
            var3.getMappingManager().changeHighLighted(var12);
            var3.setDirty(true);
         } else {
            if (!var4 && !var5) {
               return;
            }

            var1.m_CROM.clearType(768L);
            if (var10 != null) {
               var0.getTreeMapper().deleteMapping(new NodePath(var7));
               var1.removeChild(var10);
               var3.updateMappingFlag();
               var3.getMappingManager().changeHighLighted(null);
               var3.setDirty(true);
            }

            if (var1.isLoopNode() && var11 == null) {
               insertOldValueNode(var0, var1);
            }
         }
      }
   }

   public static Node insertAttributeCommand(TreeComponent var0, Node var1) {
      if (!isLdapAttribute(var0, var1)) {
         return null;
      }

      CROM var2 = new CROM(var1.m_CROM, false, "Attribute command", 1, var1.m_CROM.cromType);
      var2.addType(36L);
      var2.setValueType("String");
      return new Node(var0, var1, var2);
   }

   public static Node insertOldValueNode(TreeComponent var0, Node var1) {
      if (!isLdapAttribute(var0, var1)) {
         return null;
      }

      CROM var2 = new CROM(var1.m_CROM, false, "Old value", 1, var1.m_CROM.cromType);
      var2.addType(36L);
      var2.setValueType("String");
      return new Node(var0, var1, var2);
   }

   public static void setLdapAttributeType(TreeComponent var0, Node var1, String var2) {
      if (var1.m_parent != null) {
         if (isLdapAttribute(var0, var1.m_parent)) {
            var1.m_parent.m_CROM.clearType(768L);
            if (var2.equals("Remove")) {
               var1.m_parent.m_CROM.addType(256L);
            }

            if (var2.equals("Replace all")) {
               var1.m_parent.m_CROM.addType(512L);
            }
         }
      }
   }

   public static boolean mapDisabled(TreeComponent var0, Node var1) {
      boolean var2 = var1.m_parent != null && isLdapAttributeDelete(var0, var1.m_parent) && var1.m_CROM.getName().equals("");
      return var2 ? true : var1.m_parent != null && isLdapAttributeReplaceAll(var0, var1.m_parent) && var1.m_CROM.getName().equals("Old?value");
   }

   private LdapUtils() {
   }
}
