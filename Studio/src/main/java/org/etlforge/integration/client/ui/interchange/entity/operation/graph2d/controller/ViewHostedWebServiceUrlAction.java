package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.undo.UndoRedoControllers;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.operation.HostedWebServiceOperationType;
import org.etlforge.ui.Empty;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.util.UiUtils;

final class ViewHostedWebServiceUrlAction extends AbstractAction {
   private final OperationWrapperNode opNode;

   public ViewHostedWebServiceUrlAction(OperationWrapperNode var1) {
      super(Strings.get("ViewHostedWebServiceUrl.Name"));
      Preconditions.checkArgument(var1.getPipeline().getType() == HostedWebServiceOperationType.TYPE, "opNode must be of type HostedWebService");
      this.opNode = var1;
      this.putValue("ShortDescription", Strings.get("ViewHostedWebServiceUrl.ToolTip"));
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ViewHostedWebServiceUrlAction.this.showDialog();
         }
      });
   }

   private void showDialog() {
      KongaDialog var1 = new KongaDialog(UiUtils.getActiveWindow(), Strings.get("ViewHostedWebServiceUrl.Title"));
      this.createContentPane(var1);
      var1.enableUndoRedoForTextComponents(UndoRedoControllers.textFactory());
      var1.pack();
      var1.setResizable(false);
      var1.manageLocation();
      var1.setVisible(true);
   }

   private void createContentPane(KongaDialog var1) {
      Operation var2 = this.opNode.getDataObject().getActivity();
      HostedWebServiceUrlWidget var3 = new HostedWebServiceUrlWidget(var2.getID());
      BoxBuilder var4 = BoxBuilder.horizontal();
      var4.add(var3.getCopyToClipboardButton()).glue().add(var1.createCloseButton()).withEmptyBorder(20, 0, 0, 0);
      BorderLayoutBuilder var5 = new BorderLayoutBuilder();
      var5.north(var3).south(var4);
      var5.withBorder(Empty.border(10));
      var5.asContentPaneFor(var1);
      var1.setFocusedComponent(var3);
   }
}
