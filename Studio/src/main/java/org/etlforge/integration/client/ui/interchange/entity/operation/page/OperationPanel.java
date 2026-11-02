package org.etlforge.integration.client.ui.interchange.entity.operation.page;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPanel;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.OperationGraph2D;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;

public class OperationPanel extends IntegrationEntityPanel {
   private final OperationGraphPanel graphPanel;
   private final OperationGraphSaver graphSaver;

   public OperationPanel(ApplicationWindow var1, Operation var2, OperationGraphPanel var3, OperationGraphSaver var4) {
      super(var1, var2);
      this.graphPanel = var3;
      this.graphSaver = var4;
      this.addInputPanel(var3);
      BorderLayoutBuilder.from(this.getUi()).center(var3.getUi());
      UiUtils.setOpaqueRecursivelySkipDefaults(this.getUi(), false);
   }

   public OperationGraph2D getGraph() {
      return this.graphPanel.getGraph();
   }

   public Operation getDisplayedObject() {
      return (Operation)super.getDisplayedObject();
   }

   @Override
   protected void applyOtherPropertiesThanTheName() {
      this.graphPanel.save(this.graphSaver);
   }

   @Override
   public void setInitialFocus() {
      this.graphPanel.getGraph().requestFocus();
   }

   @Override
   public void display() {
      super.display();
      this.graphPanel.prepareForFirstDisplay();
   }

   public void addOperationGraphListener(OperationGraphListener var1) {
      this.graphPanel.addOperationGraphListener(var1);
   }

   public void removeOperationGraphListener(OperationGraphListener var1) {
      this.graphPanel.removeOperationGraphListener(var1);
   }
}
