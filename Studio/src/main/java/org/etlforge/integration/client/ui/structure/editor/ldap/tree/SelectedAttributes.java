package org.etlforge.integration.client.ui.structure.editor.ldap.tree;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.etlforge.integration.data.structure.ldap.AttributeContributions;
import org.etlforge.integration.ldap.structure.LdapStructureAttribute;
import org.etlforge.integration.ldap.structure.LdapStructureEntry;
import org.etlforge.integration.ldap.structure.NumericOid;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.util.name.Nameables;

final class SelectedAttributes {
   private final AttributeSelectionModel model;
   private final Map<NumericOid, String[]> selected;

   public SelectedAttributes(AttributeSelectionModel var1) {
      this.model = var1;
      this.selected = Maps.newHashMap();

      for (KongaTreeNode var3 : var1.getRoot().children()) {
         this.processEntry((ClassNameNode)var3);
      }
   }

   private void processEntry(ClassNameNode var1) {
      AttributeContributions var2 = this.model.getAttributeContributions(var1.getOid());
      ArrayList var3 = Lists.newArrayList();
      boolean var4 = false;

      for (KongaTreeNode var6 : var1.children()) {
         if (var6 instanceof AttributeSelectionNode) {
            AttributeSelectionNode var7 = (AttributeSelectionNode)var6;
            if (var7.isSelected()) {
               LdapStructureAttribute var8 = var7.getUserObject();
               if (!var2.isExtendedAttribute(var8)) {
                  var3.add(var7.getUserObject().getName());
               }
            } else {
               var4 = true;
            }
         }
      }

      NumericOid var9 = var1.getOid();
      if (var4) {
         this.selected.put(var9, (String[]) var3.toArray(new String[var3.size()]));
      } else {
         this.selected.put(var9, null);
      }
   }

   public LdapStructureEntry[] getDefinedEntries() {
      ArrayList var1 = Lists.newArrayList();

      for (NumericOid var3 : this.model.getClassOids()) {
         String[] var4 = this.selected.get(var3);
         NumericOid[] var5 = this.model.getAuxiliaryClassesFor(var3);
         AttributeContributions var6 = this.model.getAttributeContributions(var3);
         LdapStructureAttribute[] var7 = var6.getExtendedAttributes();
         LdapStructureEntry var8 = new LdapStructureEntry(var3, var4, var5, var7);
         var1.add(var8);
      }

      return (LdapStructureEntry[]) var1.toArray(new LdapStructureEntry[var1.size()]);
   }

   public void applyTo(AttributeSelectionModel var1) {
      for (KongaTreeNode var3 : var1.getRoot().children()) {
         ClassNameNode var4 = (ClassNameNode)var3;
         if (this.selected.containsKey(var4.getOid())) {
            Set var5 = this.getNamesOfAttributesToSelectInTheTree(var4);

            for (AttributeSelectionNode var7 : var4.getAttributeNodes()) {
               boolean var8 = var5 == null || var5.contains(var7.getUserObject().getName());
               var7.setSelected(var8);
            }
         }
      }
   }

   private Set<String> getNamesOfAttributesToSelectInTheTree(ClassNameNode var1) {
      String[] var2 = this.selected.get(var1.getOid());
      if (var2 == null) {
         return null;
      }

      HashSet var3 = Sets.newHashSet(var2);
      AttributeContributions var4 = this.model.getAttributeContributions(var1.getOid());
      if (var4 != null) {
         Nameables.addNames(var3, var4.getExtendedAttributes());
      }

      return var3;
   }
}
