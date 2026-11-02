package org.etlforge.integration.client.ui.interchange.entity.httpendpoint.page;

import com.google.common.collect.ImmutableSet;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPanel;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanel;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.integration.data.entity.HttpEndpoint;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.util.UiUtils;

public class HttpEndpointPanel extends IntegrationEntityPanel {
   private final HttpEndpoint endpoint;
   private final ResponseModel responseModel;
   private final UrlPanel urlPanel;
   private final OperationSelectorPanel opSelectorPanel;
   private final RequestPanel requestPanel;
   private final ResponsePanel responsePanel;

   public HttpEndpointPanel(ApplicationWindow var1, HttpEndpoint var2) {
      super(var1, var2);
      this.endpoint = var2;
      this.urlPanel = new UrlPanel(var2, this);
      this.responseModel = this.createResponseModel();
      this.opSelectorPanel = new OperationSelectorPanel(this, this.responseModel);
      this.requestPanel = new RequestPanel(var2, this);
      this.responsePanel = new ResponsePanel(this.responseModel, this);
      this.setSendInputChangeEvents(true);
      this.setSendUndoEvents(true);
   }

   public HttpEndpoint getDisplayedObject() {
      return (HttpEndpoint)super.getDisplayedObject();
   }

   public void setEntityExplorerSupport(EntityExplorerSupport var1) {
      this.opSelectorPanel.setExplorerSupport(var1);
      this.responsePanel.setOperationLookup(var1.getProject().getItemLookup());
   }

   public void setOperationLookup(IntegrationEntityLookup var1) {
      this.responsePanel.setOperationLookup(var1);
      this.opSelectorPanel.setOperationLookup(var1);
   }

   @Override
   protected void applyOtherPropertiesThanTheName() {
      HttpEndpoint var1 = this.getDisplayedObject();
      this.urlPanel.apply(var1);
      this.requestPanel.apply(var1);
      this.responseModel.applyTo(var1);
   }

   @Override
   public void dispose() {
      super.dispose();
      this.opSelectorPanel.dispose();
   }

   @Override
   public void display() {
      this.doLayout();
      super.display();
   }

   private void doLayout() {
      IntegrationDataPanel.Layout var1 = this.getDefaultLayout();
      var1.addToTopGrid(this.urlPanel.getLabel(), this.urlPanel.getFieldForLayout());
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 8);
      var2.center(this.opSelectorPanel);
      TwoColumnPanel var3 = new TwoColumnPanel();
      var3.withLeftFill(GridBagLayoutPanel.Fill.BOTH);
      var3.withRightFill(GridBagLayoutPanel.Fill.BOTH);
      var3.withRightWeights(1.0, 0.0);
      var3.addRow(this.requestPanel, this.responsePanel);
      var2.south(var3);
      var1.add(var2, true);
      UiUtils.setOpaqueRecursivelySkipDefaults(this.getUi(), false);
   }

   private ResponseModel createResponseModel() {
      ResponseModel var1 = new ResponseModel(this.endpoint);
      var1.addPropertyChangeListener(new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            HttpEndpointPanel.this.fireInputChanged(null, null);
         }
      });
      var1.addUndoRedoListener(new UndoableEditListener() {
         @Override
         public void undoableEditHappened(UndoableEditEvent var1) {
            HttpEndpointPanel.this.fireUndoableEditEvent(var1);
         }
      });
      return var1;
   }

   public ImmutableSet<? extends BoundProperty> getBoundProperties() {
      return ImmutableSet.of(this.urlPanel.asBoundProperty(), this.requestPanel.asBoundProperty(), this.responsePanel.asBoundProperty());
   }
}
