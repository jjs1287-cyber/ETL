package org.etlforge.integration.client.ui.connector.mscrm.endpoint.page;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import java.util.Collections;
import javax.swing.Action;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.entity.valuecache.ValueCache;
import org.etlforge.integration.client.ui.connector.EndpointParametersPanel;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPanel;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanel;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanelException;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.integration.data.connector.MSCrmEndpoint;
import org.etlforge.integration.etlforgeconnector.EndpointParameters;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.border.TitledToolbar;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public final class MSCrmEndpointPanel extends IntegrationEntityPanel {
   private Optional<Action> testConnection = Optional.absent();
   private final EndpointParametersPanel parametersPanel = new EndpointParametersPanel("Discovery Service &URL");
   private final ValueCache<MSCrmEndpoint> valueCache;

   public MSCrmEndpointPanel(ApplicationWindow var1, MSCrmEndpoint var2, ValueCache<MSCrmEndpoint> var3) {
      super(var1, var2);
      this.valueCache = var3;
      this.parametersPanel.setAdditionalParametersPanel(new AdditionalMSCrmParamsPanel());
      this.parametersPanel.setIncludePortField(false);
      EndpointParameters var4 = var2.getConnectionParameters();
      this.parametersPanel.setParameters(var4);
      this.parametersPanel.registerAsChangeSource(this);
      this.parametersPanel.installAutoCompletion(var3);
   }

   public void setTestConnectionAction(Action var1) {
      this.testConnection = Optional.of(var1);
   }

   @Override
   public void display() {
      super.display();
      this.doLayout();
   }

   private void doLayout() {
      JPanel var1 = InvisiblePanel.of(this.parametersPanel);
      UiUtils.setOpaqueRecursivelySkipDefaults(var1, false);
      TitledToolbar var2 = new TitledToolbar(var1, "Connection Parameters");
      StyleSheet.configureTitledToolbar(var2);
      if (this.testConnection.isPresent()) {
         var2.addButton(new KongaButton(this.testConnection.get()).setUseSmallActionIcon(true));
      }

      IntegrationDataPanel.Layout var3 = this.getDefaultLayout();
      var3.add(var2);
      var3.glue();
   }

   @Override
   protected void applyOtherPropertiesThanTheName() throws IntegrationDataPanelException {
      MSCrmEndpoint var1 = (MSCrmEndpoint)this.getDisplayedObject();
      EndpointParameters var2 = this.parametersPanel.getEndpointParameters();
      var1.setConnectionParameters(var2);
      this.valueCache.update(Collections.singleton(var1));
   }

   public ImmutableList<BoundProperty> getBoundProperties() {
      return this.parametersPanel.getBoundProperties();
   }

   @Override
   public void dispose() {
      super.dispose();
      this.parametersPanel.dispose();
   }
}
