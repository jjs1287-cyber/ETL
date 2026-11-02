package org.etlforge.integration.client.ui.structure.editor.ldap;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.integration.client.ldap.ui.SelectedObjectClasses;
import org.etlforge.integration.client.ldap.ui.SelectedObjectStructures;
import org.etlforge.integration.client.ui.structure.editor.ldap.tree.AttributeSelectionTree;
import org.etlforge.integration.data.structure.LdapStructure;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.OneColumnPanel;

final class ClassStructureReviewUi implements UiProvider {
   private final ClassStructureReviewPage page;
   private final ClassStructureReviewModel model;
   private final Action refreshAction;
   private final AttributeSelectionTree structureTree;

   public ClassStructureReviewUi(ClassStructureReviewPage var1, ClassStructureReviewModel var2) {
      this.page = var1;
      this.model = var2;
      this.refreshAction = new ClassStructureReviewUi.RefreshStructuresAction();
      var2.addPropertyChangeListener(new ClassStructureReviewUi.ModelListener());
      this.structureTree = new AttributeSelectionTree(var2.getTreeModel(), PackageResources.Wizard.StructureReviewer.TREE_TITLE);
      this.structureTree.setLdapCache(var2.getLdapCache());
      this.structureTree.setRefreshAction(this.refreshAction);
   }

   @Override
   public void decorate(UiCanvas var1) {
      ClassStructureReviewUi.Layout var2 = new ClassStructureReviewUi.Layout();
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.structureTree.requestFocus();
   }

   public void applyTo(LdapStructure var1) {
      this.structureTree.applyTo(var1);
   }

   private class Layout {
      private final OneColumnPanel col = new OneColumnPanel().withInsets(0, 5, 5, 5).fillOut(ClassStructureReviewUi.this.structureTree);

      public Layout() {
      }

      public void decorate(UiCanvas var1) {
         var1.display(BorderLayoutBuilder.fillWith(this.col.deeplyNonOpaque()));
      }
   }

   private class ModelListener implements PropertyChangeListener {
      private ModelListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         String var2 = var1.getPropertyName();
         if ("classes".equals(var2)) {
            ClassStructureReviewUi.this.refreshAction.setEnabled(var1.getNewValue() != null);
         } else if ("treeModel".equals(var2)) {
            ClassStructureReviewUi.this.structureTree.setModel(ClassStructureReviewUi.this.model.getTreeModel());
         }
      }
   }

   private class RefreshStructuresAction extends AbstractAction implements Receiver<SelectedObjectStructures> {
      public RefreshStructuresAction() {
         ActionDecorators.REFRESH.decorate(this);
         this.setEnabled(false);
      }

      public void handle(SelectedObjectStructures var1) {
         ClassStructureReviewUi.this.model.setSelectedStructures(var1);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               RefreshStructuresAction.this.refresh();
            }
         });
      }

      private void refresh() {
         SelectedObjectClasses var1 = ClassStructureReviewUi.this.model.getSelectedClasses();
         if (var1 != null) {
            StructureCacheRefresher var2 = new StructureCacheRefresher(
               ClassStructureReviewUi.this.model.getLdapCache(), ClassStructureReviewUi.this.page.getWaitService()
            );
            var2.refresh(var1, this);
         }
      }
   }
}
