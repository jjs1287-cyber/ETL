package org.etlforge.integration.client.ui.validation.view;

import org.etlforge.integration.client.ui.entity.EditEntityAction;
import org.etlforge.integration.client.ui.entity.EntityPageDisplayer;
import org.etlforge.integration.client.ui.validation.EntityValidationTreeDisplayer;
import org.etlforge.integration.client.ui.validation.resources.ValidationUiResources;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.validation.ValidationModel;
import org.etlforge.integration.data.validation.EntityCollectionValidationResult;
import org.etlforge.integration.data.validation.ValidationStatus;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;

final class ValidationDisplayer implements UiProvider, Disposable {
   private ValidationModel model;
   private final Tree tree;
   private final EntityValidationTreeDisplayer problemList = new EntityValidationTreeDisplayer();
   private IntegrationEntity selectedEntity;
   private final EditEntityAction editEntityAction = new EditEntityAction();

   public ValidationDisplayer(ValidationModel var1) {
      this.tree = new Tree(var1);
      this.tree.setDefaultAction(this.editEntityAction);
      this.tree.addSelectionListener(new ValidationDisplayer.TreeSelectionListenerImpl());
      this.problemList.setIncludeRoot(true);
      this.clearProblemList();
      this.setModel(var1);
   }

   private void clearProblemList() {
      this.problemList.clear(ValidationUiResources.getString("Displayer.Details.Instruction"));
   }

   public void setModel(ValidationModel var1) {
      this.model = var1;
      this.tree.setModel(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      Grid var2 = Grid.oneRow(10);
      var2.addAll(this.tree, this.problemList);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.tree.requestFocus();
   }

   @Override
   public void dispose() {
      this.tree.dispose();
      this.model = null;
   }

   public void collapseTree() {
      this.tree.collapseAll();
   }

   public void expandTree() {
      this.tree.expandAll();
   }

   public IntegrationEntity getSelectedEntity() {
      return this.selectedEntity;
   }

   public void setPageDisplayer(EntityPageDisplayer var1) {
      this.editEntityAction.setPageDisplayer(var1);
   }

   public void addSelectionListener(SelectionListener var1) {
      this.tree.addSelectionListener(var1);
   }

   private class TreeSelectionListenerImpl implements SelectionListener {
      private TreeSelectionListenerImpl() {
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         OrderedSelection var2 = (OrderedSelection)var1.getSelection();
         ValidationDisplayer.this.selectedEntity = var2.isEmpty() ? null : (IntegrationEntity)var2.head();
         EntityCollectionValidationResult var3 = this.getProblems();
         if (var3 != null) {
            ValidationDisplayer.this.problemList.display(var3);
         } else {
            ValidationDisplayer.this.clearProblemList();
         }
      }

      private EntityCollectionValidationResult getProblems() {
         if (ValidationDisplayer.this.selectedEntity != null) {
            EntityCollectionValidationResult var1 = ValidationDisplayer.this.model.getCompoundValidationState(ValidationDisplayer.this.selectedEntity);
            if (var1.status() != ValidationStatus.VALID) {
               return var1;
            }
         }

         return null;
      }
   }
}
