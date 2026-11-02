package org.etlforge.integration.client.ui.structure.editor.xml.wizard;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.selection.AbstractSelectionAction;
import org.etlforge.ui.util.KeyValueToolTip;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.decorator.ListDecor;
import org.etlforge.ui.util.decorator.ListDecorator;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.list.ListSelectionMode;
import org.etlforge.xml.ExpandedName;
import org.etlforge.xml.KongaXmlUtils;

final class RootList implements UiProvider {
   private final RootSelector selector;
   private final RootSelectorContainer container;
   private final KongaList rootList;

   public RootList(RootSelector var1, RootSelectorContainer var2) {
      assert var1 != null;
      assert var2 != null;
      this.selector = var1;
      this.container = var2;
      this.rootList = this.createRootList();
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.rootList.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.rootList.requestFocus();
   }

   public void setActive(boolean var1) {
      this.rootList.setEnabled(var1);
   }

   public void setSelectedRoot(final ExpandedName var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            RootList.this.setSelectedRootImpl(var1);
         }
      });
   }

   private void setSelectedRootImpl(ExpandedName var1) {
      assert EventQueue.isDispatchThread() : "setSelectedRootImpl must run on the EDT";
      ListModel var2 = this.rootList.getModel();
      int var3 = 0;

      for (int var4 = var2.getSize(); var3 < var4; var3++) {
         RootList.ListItem var5 = (RootList.ListItem)var2.getElementAt(var3);
         if (var5.element.equals(var1)) {
            this.rootList.setSelectedIndex(var3);
            return;
         }
      }
   }

   public void setAvailableRoots(ExpandedName[] var1) {
      final ListModel var2 = this.createListModel(var1);
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            RootList.this.rootList.setModel(var2);
            RootList.this.rootList.selectFirst();
         }
      });
   }

   public ExpandedName getSelectedRoot() {
      Object var1 = this.rootList.getSelection().head();
      return var1 instanceof RootList.ListItem ? ((RootList.ListItem)var1).element : null;
   }

   private void applySelection() {
      if (this.hasSelection()) {
         this.container.applySelectionRequested(this.selector);
      }
   }

   private boolean hasSelection() {
      return !this.rootList.getSelection().isEmpty();
   }

   private KongaList createRootList() {
      KongaListFactory var1 = ComponentFactories.newKongaListFactory();
      var1.setPrototypeValue(this.getPrototypeValue());
      var1.setVisibleRowCount(10);
      var1.setSelectionMode(ListSelectionMode.SINGLE_SELECTION);
      KongaList var2 = var1.newList();
      var2.setDefaultAction(new AbstractSelectionAction() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            RootList.this.applySelection();
         }
      });
      return var2;
   }

   private ListModel createListModel(ExpandedName[] var1) {
      Arrays.sort(var1, KongaXmlUtils.SORT_EXPANDED_NAME_BY_LOCAL_PART);
      DefaultListModel var2 = new DefaultListModel();

      for (ExpandedName var6 : var1) {
         var2.addElement(new RootList.ListItem(var6));
      }

      return var2;
   }

   private Object getPrototypeValue() {
      StringBuilder var1 = new StringBuilder();

      for (int var2 = 0; var2 < 30; var2++) {
         var1.append("W");
      }

      return var1;
   }

   private static final class ListItem implements ListDecorator {
      private final ExpandedName element;
      private final String tooltip;

      public ListItem(ExpandedName var1) {
         this.element = var1;
         this.tooltip = KeyValueToolTip.create(
            new Object[]{PackageResources.NAMESPACE_URI, PackageResources.LOCAL_NAME}, new Object[]{var1.getNamespace(), var1.getLocalName()}
         );
      }

      @Override
      public void decorate(ListDecor var1, boolean var2) {
         var1.setText(this.element.getLocalName());
         var1.setToolTip(this.tooltip);
      }

      @Override
      public String toString() {
         return this.element.getLocalName();
      }
   }
}
