package org.etlforge.integration.client.ui.connector;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.entity.wizard.TreeWidget;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPanel;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanel;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanelException;
import org.etlforge.integration.data.connector.AbstractConnectorCall;
import org.etlforge.integration.data.connector.ConnectorEndpoint;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public final class ConnectorCallPanel<F extends AbstractConnectorCall<E, ?>, E extends ConnectorEndpoint> extends IntegrationEntityPanel {
   private final TreeWidget<E> endpointSelector;
   private final ConnectorCallStructuresPanel structureTrees;
   private Optional<UiProvider> controlPanel = Optional.absent();
   private final ImmutableList<? extends FunctionProperty> functionProperties;

   public ConnectorCallPanel(
      ApplicationWindow var1, F var2, TreeWidget<E> var3, ConnectorCallStructuresPanel var4, ImmutableList<? extends FunctionProperty> var5
   ) {
      super(var1, var2);
      this.endpointSelector = var3;
      this.structureTrees = Preconditions.checkNotNull(var4, "structureTrees");
      this.functionProperties = Preconditions.checkNotNull(var5, "functionProperties");
      this.addInputField(var3.asInputField("Endpoint"));
   }

   public void setControlPanel(UiProvider var1) {
      this.controlPanel = Optional.of(var1);
   }

   @Override
   protected void applyOtherPropertiesThanTheName() throws IntegrationDataPanelException {
      ConnectorEndpoint var1 = this.endpointSelector.getSelectedEntity();
      if (var1 == null) {
         throw new IntegrationDataPanelException("Must select an endpoint.");
      }

      AbstractConnectorCall var2 = (AbstractConnectorCall)this.getDisplayedObject();
      var2.setEndpoint(var1);
   }

   @Override
   public void display() {
      this.doLayout();
      super.display();
   }

   private void doLayout() {
      TwoColumnPanel var1 = this.layoutFields();
      if (this.controlPanel.isPresent()) {
         var1.withBorder(Empty.border(20, 0, 0, 0));
         BoxBuilder var2 = BoxBuilder.horizontal().add(InvisiblePanel.of(var1)).glue().add(Horizontal.Align.east(this.controlPanel.get())).deeplyNonOpaque();
         BorderLayoutBuilder.from(this.getUi(), 0, 8).north(var2).center(this.structureTrees).nonOpaque().container();
      } else {
         IntegrationDataPanel.Layout var3 = this.getDefaultLayoutWithoutNameField();
         var3.add(var1);
         var3.add(this.structureTrees, true);
      }
   }

   private TwoColumnPanel layoutFields() {
      TwoColumnPanel var1 = TwoColumnPanel.forFormEntry(5, 10).nonOpaque();
      TextInputField2 var2 = this.getNameField();
      var2.layoutIn(var1);
      this.endpointSelector.layoutWithLabelInTwoColumns(var1);

      for (FunctionProperty var4 : this.functionProperties) {
         var4.layoutIn(var1);
      }

      return var1;
   }
}
