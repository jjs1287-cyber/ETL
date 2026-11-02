package org.etlforge.integration.client.ui.structure.editor.xml.wizard;

import java.awt.EventQueue;
import javax.swing.JComponent;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.util.option.OptionSelector;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.xml.ExpandedName;

final class SingleNamespaceRootSelector implements RootSelector {
   private final RootList rootList;

   public SingleNamespaceRootSelector(RootSelectorContainer var1, ExpandedName[] var2) {
      assert var1 != null;
      assert var2 != null && var2.length > 1;
      this.rootList = new RootList(this, var1);
      this.rootList.setAvailableRoots(var2);
   }

   @Override
   public ExpandedName getSelectedRoot() {
      return this.rootList.getSelectedRoot();
   }

   @Override
   public void setSelectedRoot(ExpandedName var1) {
      if (var1 != null) {
         this.rootList.setSelectedRoot(var1);
      }
   }

   @Override
   public void setActive(boolean var1) {
      this.rootList.setActive(var1);
      if (var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               SingleNamespaceRootSelector.this.requestFocus();
            }
         });
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      JComponent var2 = this.createUi();
      var1.display(var2);
   }

   private JComponent createUi() {
      return InvisiblePanel.of(this.rootList);
   }

   @Override
   public void requestFocus() {
      this.rootList.requestFocus();
   }

   @Override
   public void decorate(OptionSelector var1) {
      var1.setText(PackageResources.RootSelector.SINGLE_NAMESPACE_LABEL);
   }
}
