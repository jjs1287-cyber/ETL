package org.etlforge.integration.client.ui.structure.editor.ldap.tree;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.structure.LdapStructure;
import org.etlforge.integration.ldap.structure.AttributeUse;
import org.etlforge.integration.ldap.structure.LdapStructureAttribute;

final class InitialSelectionState {
   private final LdapStructure struct;
   private final Collection<ClassNameNode> classNodes;
   private final SourceTarget st;

   public InitialSelectionState(LdapStructure var1, Collection<ClassNameNode> var2, SourceTarget var3) {
      this.struct = var1;
      this.classNodes = var2;
      this.st = var3;
   }

   public void apply() {
      if (this.struct != null) {
         this.setInitialSelectionFromExistingStructure();
      } else {
         this.setInitialSelectionForNewStructure();
      }
   }

   private void setInitialSelectionFromExistingStructure() {
      HashSet var1 = Sets.newHashSet(this.struct.getObjectClassOids());
      Set var2 = this.struct.getSelectedAttributeNames();
      Set var3 = this.struct.getExtendedAttributes();

      for (ClassNameNode var5 : this.classNodes) {
         if (var1.contains(var5.getOid())) {
            this.setSelectionStateForKnownObjectClass(var2, var3, var5);
         } else {
            this.setSelectionStateForNewObjectClass(var5);
         }
      }
   }

   private void setSelectionStateForKnownObjectClass(Set<String> var1, Set<LdapStructureAttribute> var2, ClassNameNode var3) {
      for (AttributeSelectionNode var5 : var3.getAttributeNodes()) {
         LdapStructureAttribute var6 = var5.getUserObject();
         boolean var7 = var1 == null || var1.contains(var6.getName()) || var2.contains(var6);
         var5.setSelected(var7);
      }
   }

   private void setSelectionStateForNewObjectClass(ClassNameNode var1) {
      for (AttributeSelectionNode var3 : var1.getAttributeNodes()) {
         if (this.st == SourceTarget.Source) {
            var3.setSelected(false);
         } else {
            LdapStructureAttribute var4 = var3.getUserObject();
            boolean var5 = var4.getUse() == AttributeUse.REQUIRED;
            var3.setSelected(var5);
         }
      }
   }

   private void setInitialSelectionForNewStructure() {
      for (ClassNameNode var2 : this.classNodes) {
         this.setSelectionStateForNewObjectClass(var2);
      }
   }
}
