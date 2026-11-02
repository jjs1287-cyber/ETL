package org.etlforge.integration.client.ui.interchange.entity.httpendpoint.page;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.factory.Request;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactoryCallback;
import org.etlforge.integration.client.ui.entity.selector.ExistingEntitySelectionUi;
import org.etlforge.integration.client.ui.entity.selector.ExistingEntityTreeSelectionUi;
import org.etlforge.integration.data.IntegrationData;
import org.etlforge.integration.data.ValidationListener;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.httpendpoint.TriggerableOperationFilter;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectPropertyChangeSupport;
import org.etlforge.integration.data.project.event.IntegrationProjectEvent;
import org.etlforge.integration.data.project.event.ProjectListener;
import org.etlforge.integration.data.validation.ValidationResult;
import org.etlforge.system.Disposable;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.selection.SelectionAction;

final class OperationSelectorPanel implements UiProvider, Disposable {
   private final InputPanel panel = new InputPanel();
   private IntegrationProject project;
   private OperationSelectorPanel.ProjectChangeHandler projectChangeHandler;
   private final ResponseModel responseModel;
   private final OperationTable table;
   private EntityExplorerSupport explorerSupport;
   private final OperationSelectorPanel.AddOperationAction addAction = new OperationSelectorPanel.AddOperationAction();
   private final SelectionAction removeAction;
   private final EditOperationAction editAction = new EditOperationAction();
   private boolean readOnly;

   public OperationSelectorPanel(InputPanel var1, ResponseModel var2) {
      this.responseModel = var2;
      this.removeAction = new RemoveOperationAction(var2);
      this.table = new OperationTable(var2, this.addAction, this.editAction, this.removeAction);
      if (var1 != null) {
         var1.addInputPanel(this.panel);
      }
   }

   @Override
   public void dispose() {
      this.panel.dispose();
      if (this.projectChangeHandler != null) {
         this.projectChangeHandler.uninstall(this.project);
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      BoxBuilder var2 = BoxBuilder.horizontal();
      var2.addAllWithSpace(10, this.addAction, this.editAction).glue().add(this.removeAction);
      BorderLayoutBuilder var3 = new BorderLayoutBuilder(0, 5);
      var3.center(this.table).south(var2).withBorder(StyleSheet.getDefaultTitleBorder("Operations To Trigger")).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.table.requestFocus();
   }

   public void setExplorerSupport(EntityExplorerSupport var1) {
      if (this.project != null) {
         this.projectChangeHandler.uninstall(this.project);
      }

      this.explorerSupport = var1;
      this.project = var1.getProject();
      this.projectChangeHandler = new OperationSelectorPanel.ProjectChangeHandler(this.project);
      this.addAction.setProject(this.project);
      this.editAction.setExplorerSupport(var1);
      this.setOperationLookup(this.project.getItemLookup());
   }

   public void setOperationLookup(IntegrationEntityLookup var1) {
      this.table.setOperationLookup(var1);
   }

   private final class AddOperationAction extends AbstractAction {
      private IntegrationProject project;

      public AddOperationAction() {
         super("Add Operation...");
         this.setEnabled(false);
      }

      public void setProject(IntegrationProject var1) {
         this.project = var1;
         this.setEnabled(var1 != null);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (this.project != null && !OperationSelectorPanel.this.readOnly) {
            ExistingEntitySelectionUi var2 = this.createSelector();
            var2.select(new Receiver<Operation>() {
               public void handle(Operation var1) {
                  if (var1 != null) {
                     OperationSelectorPanel.this.responseModel.addOperation(var1);
                  }
               }
            });
         }
      }

      private ExistingEntitySelectionUi<Operation> createSelector() {
         ExistingEntityTreeSelectionUi var1 = new ExistingEntityTreeSelectionUi<>(this.project, Operation.class, EntityType.Operation);
         var1.setFilter(OperationSelectorPanel.this.new OperationSelectionFilter());
         Action var2 = OperationSelectorPanel.this.explorerSupport
            .getCreateNewEntityAction(EntityType.Operation, Request.Source.OTHER, OperationSelectorPanel.this.new AddOperationCallback());
         var1.setCreateNewItemAction(var2);
         return var1;
      }
   }

   private class AddOperationCallback extends UiEntityFactoryCallback.Adapter {
      private AddOperationCallback() {
      }

      @Override
      public void postProjectInsertion(final IntegrationEntity var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               Operation var1x = (Operation)var1;
               OperationSelectorPanel.this.responseModel.addOperation(var1x);
               OperationSelectorPanel.this.explorerSupport.openPageFor(var1x);
            }
         });
      }
   }

   private final class OperationSelectionFilter implements EntityFilter {
      private OperationSelectionFilter() {
      }

      @Override
      public boolean apply(IntegrationEntity var1) {
         if (TriggerableOperationFilter.FILTER.apply(var1)) {
            Operation var2 = (Operation)var1;
            return !OperationSelectorPanel.this.responseModel.contains(var2.getID());
         } else {
            return false;
         }
      }
   }

   private final class ProjectChangeHandler extends ProjectListener.Adapter implements PropertyChangeListener, ValidationListener {
      private final ProjectPropertyChangeSupport changeSupport;

      public ProjectChangeHandler(IntegrationProject var2) {
         this.changeSupport = new ProjectPropertyChangeSupport(var2);
         this.changeSupport.addPropertyChangeListener("Name", this);
         this.changeSupport.addValidationListener(this);
         var2.addProjectListener(this);
      }

      public void uninstall(IntegrationProject var1) {
         this.changeSupport.dispose();
         var1.removeProjectListener(this);
      }

      @Override
      public void entitiesDeleted(IntegrationProjectEvent var1) {
         for (IntegrationEntity var3 : var1.getEntities()) {
            if (var3 instanceof Operation) {
               OperationSelectorPanel.this.responseModel.operationWasDeleted(((Operation)var3).getID());
            }
         }
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         this.repaintTable();
      }

      @Override
      public void validationComplete(IntegrationData var1, ValidationResult var2) {
         if (var1 instanceof Operation) {
            this.repaintTable();
         }
      }

      private void repaintTable() {
         OperationSelectorPanel.this.table.repaint();
      }
   }
}
