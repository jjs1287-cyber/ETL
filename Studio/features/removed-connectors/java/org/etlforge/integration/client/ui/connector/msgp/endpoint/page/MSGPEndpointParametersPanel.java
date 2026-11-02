package org.etlforge.integration.client.ui.connector.msgp.endpoint.page;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.Properties;
import org.etlforge.application.ui.widget.DefaultPortDefiner;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.application.ui.widget.TextFieldCompletionSupport;
import org.etlforge.integration.client.entity.valuecache.ValueCache;
import org.etlforge.integration.client.ui.connector.AdditionalParametersPanel;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanelException;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.integration.data.binding.ui.BoundPropertyFactory;
import org.etlforge.integration.data.binding.ui.PropertyValueHistory;
import org.etlforge.integration.data.connector.ConnectorEndpoint;
import org.etlforge.integration.etlforgeconnector.EndpointParameters;
import org.etlforge.integration.etlforgeconnector.ServerParams;
import org.etlforge.integration.etlforgeconnector.UserCredentials;
import org.etlforge.system.Disposable;
import org.etlforge.system.DisposableCollection;
import org.etlforge.system.Disposables;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.NColumnPanel;
import org.etlforge.ui.text.contentassist.StringValueHistory;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.util.net.Port;

public final class MSGPEndpointParametersPanel implements UiProvider {
   private final TextInputField2 hostField;
   private final DefaultPortDefiner portField = new DefaultPortDefiner();
   private final DisposableCollection autoCompleters = new DisposableCollection();
   private AdditionalParametersPanel additionalParamsPanel = new MSGPEndpointParametersPanel.EmptyAdditionalParamsPanel();
   private boolean includePortField = true;

   public MSGPEndpointParametersPanel() {
      this.hostField = InputFieldFactory.getNonEmptyTextInputField("Server Name", KongaLabel.withMnemonic("&Server Name"), 30);
      this.hostField.setAutoTrim(true);
   }

   public void setAdditionalParametersPanel(AdditionalParametersPanel var1) {
      this.additionalParamsPanel = Preconditions.checkNotNull(var1);
   }

   public void registerAsChangeSource(InputPanel var1) {
      var1.addInputField(this.hostField, this.hostField, this.hostField);
      this.portField.registerAsChangeSource(var1);
      this.additionalParamsPanel.registerAsChangeSource(var1);
   }

   public void setIncludePortField(boolean var1) {
      this.includePortField = var1;
   }

   public void setParameters(EndpointParameters var1) {
      if (var1 == null) {
         this.hostField.setValue("");
         this.portField.setPort(Port.DEFAULT);
         this.additionalParamsPanel.setParameters(null);
      } else {
         this.hostField.setValue(var1.getServer().getHost());
         this.portField.setPort(var1.getServer().getPort());
         this.additionalParamsPanel.setParameters(var1.getAdditionalParams());
      }
   }

   public EndpointParameters getEndpointParameters() throws IntegrationDataPanelException {
      String var1 = this.hostField.getValue();
      if (var1.isEmpty()) {
         throw new IntegrationDataPanelException("Must provide a host name.");
      }

      Port var2 = this.getPort();
      ServerParams var3 = new ServerParams(var1, var2);
      UserCredentials var4 = UserCredentials.usernameOnly("");
      Properties var5 = this.additionalParamsPanel.getParameters();
      return new EndpointParameters(var3, var4, var5);
   }

   private Port getPort() throws IntegrationDataPanelException {
      Port var1 = Port.DEFAULT;
      if (this.includePortField) {
         var1 = this.portField.getPort();
         if (var1 == Port.INVALID) {
            throw new IntegrationDataPanelException("Invalid port number.");
         }
      }

      return var1;
   }

   public ImmutableList<BoundProperty> getBoundProperties() {
      return new Builder<BoundProperty>()
         .add(BoundPropertyFactory.textProperty("host", this.hostField))
         .addAll(this.additionalParamsPanel.getBoundProperties())
         .build();
   }

   @Override
   public void decorate(UiCanvas var1) {
      NColumnPanel var2 = new NColumnPanel(3);
      var2.setHGap(5);
      var2.setAnchors(GridBagLayoutPanel.Anchor.EAST, GridBagLayoutPanel.Anchor.WEST, GridBagLayoutPanel.Anchor.WEST);
      var2.setWeights(0, 0.0, 0.0).setWeights(1, 1.0, 0.0).setWeights(2, 0.0, 0.0);
      var2.setFills(GridBagLayoutPanel.Fill.NONE, GridBagLayoutPanel.Fill.HORIZONTAL, GridBagLayoutPanel.Fill.HORIZONTAL);
      this.addToLayout(var2, this.hostField, null);
      if (this.includePortField) {
         var2.addRow(null, InvisiblePanel.of(this.portField), null);
      }

      this.additionalParamsPanel.layoutIn(var2);
      var2.decorate(var1);
   }

   private void addToLayout(NColumnPanel var1, InputField var2, Object var3) {
      var1.addRow(var2.getLabel(), var2.getInputComponent(), var3);
   }

   @Override
   public void requestFocus() {
      this.hostField.requestFocus();
   }

   public void installAutoCompletion(ValueCache<? extends ConnectorEndpoint> var1) {
      StringValueHistory var2 = PropertyValueHistory.of(var1, "host");
      this.autoCompleters.add(new TextFieldCompletionSupport(this.hostField.getInputComponent(), var2));
      this.autoCompleters.add(this.additionalParamsPanel.installAutoCompletion(var1));
   }

   public void dispose() {
      this.autoCompleters.dispose();
   }

   private static class EmptyAdditionalParamsPanel implements AdditionalParametersPanel {
      private EmptyAdditionalParamsPanel() {
      }

      @Override
      public Properties getParameters() {
         return new Properties();
      }

      @Override
      public void setParameters(Properties var1) {
      }

      @Override
      public void layoutIn(NColumnPanel var1) {
      }

      @Override
      public void registerAsChangeSource(InputPanel var1) {
      }

      @Override
      public ImmutableList<BoundProperty> getBoundProperties() {
         return ImmutableList.of();
      }

      @Override
      public Disposable installAutoCompletion(ValueCache<? extends ConnectorEndpoint> var1) {
         return Disposables.NULL;
      }
   }
}
