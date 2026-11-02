package org.etlforge.integration.client.ldap.ui;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.ldap.structure.AttributeComparators;
import org.etlforge.integration.ldap.structure.LdapStructureAttribute;
import org.etlforge.integration.ldap.structure.LdapStructureEntryNode;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.selection.IndexedSelection;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;

public final class LdapAttributeListViewer implements AttributeViewer {
   private final LdapStructureEntryNode entry;
   private final KongaList list;

   public LdapAttributeListViewer(LdapStructureEntryNode var1) {
      this.entry = Preconditions.checkNotNull(var1);
      this.list = this.createList(var1);
   }

   public LdapStructureEntryNode getEntry() {
      return this.entry;
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.list.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.list.requestFocus();
   }

   public KongaList asSelectionSource() {
      return this.list;
   }

   @Override
   public LdapStructureAttribute[] getSelectedAttributes() {
      IndexedSelection var1 = this.list.getSelection();
      if (var1.isEmpty()) {
         return new LdapStructureAttribute[0];
      }

      ArrayList var2 = new ArrayList();

      for (Object var4 : var1.items()) {
         if (var4 instanceof LdapStructureAttribute) {
            var2.add((LdapStructureAttribute)var4);
         }
      }

      return (LdapStructureAttribute[]) var2.toArray(new LdapStructureAttribute[var2.size()]);
   }

   @Override
   public void setDragEnabled() {
   }

   private KongaList createList(LdapStructureEntryNode var1) {
      List var2 = var1.getSelectedAttributes();
      Collections.sort(var2, AttributeComparators.nameOrder(true));
      KongaListFactory var3 = ComponentFactories.newKongaListFactory();
      var3.setCellRenderer(new AttributeListRenderer());
      return var3.newList(var2);
   }
}
