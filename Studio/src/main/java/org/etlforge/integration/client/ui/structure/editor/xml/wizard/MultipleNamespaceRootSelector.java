package org.etlforge.integration.client.ui.structure.editor.xml.wizard;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import org.etlforge.lang.KongaStringUtils;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.option.OptionSelector;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.xml.ExpandedName;
import org.etlforge.xml.ExpandedNameBag;

final class MultipleNamespaceRootSelector implements RootSelector {
   private final ExpandedNameBag availableRoots;
   private final NamespaceSelector namespaceSelector;
   private final RootList rootList;
   private String selectedUri;

   public MultipleNamespaceRootSelector(RootSelectorContainer var1, ExpandedNameBag var2) {
      assert var1 != null;
      assert var2 != null && var2.getNumberOfNamespaces() > 1;
      this.availableRoots = var2;
      this.rootList = new RootList(this, var1);
      this.namespaceSelector = this.createNamespaceSelector();
   }

   private NamespaceSelector createNamespaceSelector() {
      final NamespaceSelector var1 = new NamespaceSelector(this.availableRoots.getNamespaces());
      var1.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1x) {
            String var2 = var1.getSelectedNamespaceUri();
            MultipleNamespaceRootSelector.this.handleNamespaceSelection(var2);
         }
      });
      var1.setInitialSelection();
      return var1;
   }

   private void handleNamespaceSelection(String var1) {
      if (!KongaStringUtils.areEqual(var1, this.selectedUri)) {
         this.selectedUri = var1;
         ExpandedName[] var2 = null;
         if (var1 != null) {
            var2 = this.availableRoots.getNamesFromNamespace(var1);
         } else {
            var2 = new ExpandedName[0];
         }

         this.rootList.setAvailableRoots(var2);
      }
   }

   @Override
   public ExpandedName getSelectedRoot() {
      return this.rootList.getSelectedRoot();
   }

   @Override
   public void setSelectedRoot(final ExpandedName var1) {
      if (var1 != null) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               MultipleNamespaceRootSelector.this.setSelectedRootImpl(var1);
            }
         });
      }
   }

   private void setSelectedRootImpl(ExpandedName var1) {
      assert EventQueue.isDispatchThread() : "setSelectedRootImpl must run on the EDT";
      this.namespaceSelector.setSelectedNamespaceUri(var1.getNamespace());
      this.rootList.setSelectedRoot(var1);
   }

   @Override
   public void setActive(boolean var1) {
      this.namespaceSelector.setActive(var1);
      this.rootList.setActive(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      JComponent var2 = this.createUi();
      var1.display(var2);
   }

   private JComponent createUi() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 8);
      var1.north(this.namespaceSelector).center(this.rootList).nonOpaque();
      return InvisiblePanel.of(var1);
   }

   @Override
   public void requestFocus() {
      this.rootList.requestFocus();
   }

   @Override
   public void decorate(OptionSelector var1) {
      var1.setText(PackageResources.RootSelector.MULTIPLE_NAMESPACE_LABEL);
   }
}
