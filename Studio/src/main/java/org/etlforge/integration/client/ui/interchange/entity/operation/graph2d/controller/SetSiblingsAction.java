package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.RootPaneContainer;
import org.etlforge.application.ui.BusyWorker;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.operation.siblings.SiblingListSelector;
import org.etlforge.integration.client.ui.operation.siblings.SiblingOperationsInfo;
import org.etlforge.integration.data.entity.DependencyStore;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.operation.HostedWebServiceOperationType;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.WebServiceCallActivity;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.dialog.UiProviderDialogDisplayer;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.wait.WindowWaitService;

final class SetSiblingsAction extends AbstractSingleObjectSelectionAction<OperationWrapperNode> {
   private final OperationGraphController controller;

   public SetSiblingsAction(OperationGraphController var1) {
      this(var1, null);
   }

   public SetSiblingsAction(OperationGraphController var1, OperationWrapperNode var2) {
      super(OperationWrapperNode.class, Strings.get("SetSiblings.Name"));
      this.controller = var1;
      this.putValue("ShortDescription", Strings.get("SetSiblings.ToolTip"));
      this.setSelection(SelectionFactory.newOrderedSelection(var2));
   }

   protected void run(final OperationWrapperNode var1, ActionEvent var2) {
      (new BusyWorker(this.getWaitService()) {
         @Override
         protected void doWork() {
            SetSiblingsAction.this.executeImpl(var1);
         }
      }).run();
   }

   private void executeImpl(OperationWrapperNode var1) {
      WebServiceCall var2 = this.getWebServiceCall(var1);
      if (var2 != null) {
         this.selectSiblings(var1, var2);
      }
   }

   protected boolean isEnabledFor(OperationWrapperNode var1) {
      return this.getWebServiceCall(var1) != null;
   }

   private WebServiceCall getWebServiceCall(OperationWrapperNode var1) {
      OperationPipeline var2 = var1.getPipeline();
      if (var2.getType() == HostedWebServiceOperationType.TYPE) {
         WebServiceCallActivity var3 = var2.getFirstActivityOfType(WebServiceCallActivity.class);
         return var3.getContent();
      } else {
         return null;
      }
   }

   private void selectSiblings(OperationWrapperNode var1, WebServiceCall var2) {
      SiblingListSelector var3 = new SiblingListSelector(this.controller.getProject(), var2, var1.getDataObject().getActivity(), this.getCurrentSiblings(var1));
      KongaDialog var4 = UiProviderDialogDisplayer.createDialog(
         this.controller.getGraph().getWindowOwner(),
         var3,
         Strings.get("SiblingSelector.Title"),
         Dialog.DEFAULT_MODALITY_TYPE,
         KongaDialog.OK_CANCEL,
         SetSiblingsAction.class
      );
      var4.setOkButtonAsDefault();
      var4.setVisible(true);
      if (var4.wasOkPressed()) {
         this.applySelection(var3, var1);
      }
   }

   private void applySelection(SiblingListSelector var1, OperationWrapperNode var2) {
      this.controller.setSiblings(var2.getDataObject(), var1.getSelectedOperations());
   }

   private List<Operation> getCurrentSiblings(OperationWrapperNode var1) {
      IntegrationProject var2 = this.controller.getProject();
      DependencyStore var3 = var2.getCurrentDependencies();

      try {
         SiblingOperationsInfo var4 = new SiblingOperationsInfo(var2.getItemLookup(), var3);
         return var4.getSiblingOperations(var1.getDataObject());
      } finally {
         var3.dispose();
      }
   }

   private WaitService getWaitService() {
      Window var1 = this.controller.getGraph().getWindowOwner();
      if (var1 instanceof WaitService) {
         return (WaitService)var1;
      } else {
         return var1 instanceof RootPaneContainer ? new WindowWaitService((RootPaneContainer)var1) : WaitService.NONE;
      }
   }
}
