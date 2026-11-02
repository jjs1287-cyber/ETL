package org.etlforge.integration.client.ui.connector.netsuite.endpoint.page;

import com.google.common.collect.ImmutableList;
import java.util.Properties;
import javax.annotation.Nullable;
import org.etlforge.integration.client.ClientApplicationProperties;
import org.etlforge.integration.client.entity.valuecache.ValueCache;
import org.etlforge.integration.client.ui.connector.AdditionalParametersPanel;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.integration.data.binding.ui.BoundPropertyFactory;
import org.etlforge.integration.data.binding.ui.PropertyValueHistory;
import org.etlforge.integration.data.connector.ConnectorEndpoint;
import org.etlforge.system.Disposable;
import org.etlforge.system.DisposableCollection;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.DefaultNonEmptyTextInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.NColumnPanel;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.text.KongaTextField;

final class AdditionalNetSuiteParamsPanel implements AdditionalParametersPanel {
   private static final boolean MULTIPLE_WSDLS_SUPPORTED = ClientApplicationProperties.getInstance().getBoolean("Netsuite.SwitchWsdl.Enabled", false);
   private final NetSuiteWsdlDownloadUrlInputField wsdlDownloadUrlField = new NetSuiteWsdlDownloadUrlInputField(
      "wsdldownloadurl", new KongaTextField(40), "Wsdl Download Url"
   );
   private final DefaultNonEmptyTextInputField emailField = new DefaultNonEmptyTextInputField("Email", new KongaTextField(30), "Email");
   private final CheckBoxInputField sandboxCheckBox = new CheckBoxInputField("Sandbox", new KongaCheckBox("Sandbo&x"));

   public AdditionalNetSuiteParamsPanel() {
      this.wsdlDownloadUrlField.setAutoTrim(true);
      this.emailField.setAutoTrim(true);
   }

   @Override
   public Properties getParameters() {
      Properties var1 = new Properties();
      var1.put("email", this.emailField.getValue());
      var1.put("wsdldownloadurl", this.wsdlDownloadUrlField.getValue());
      var1.put("sandbox", this.sandboxCheckBox.isSelected() ? "1" : "0");
      return var1;
   }

   @Override
   public void setParameters(@Nullable Properties var1) {
      if (var1 == null) {
         this.wsdlDownloadUrlField.setValue("");
         this.emailField.setValue("");
         this.sandboxCheckBox.setSelected(false);
      } else {
         this.wsdlDownloadUrlField.setValue(var1.getProperty("wsdldownloadurl"));
         this.emailField.setValue(var1.getProperty("email"));
         String var2 = var1.getProperty("sandbox", "0");
         this.sandboxCheckBox.setSelected(var2.equals("1"));
      }
   }

   @Override
   public void layoutIn(NColumnPanel var1) {
      var1.addRow(this.emailField.getLabel(), this.emailField.getInputComponent(), null);
      if (MULTIPLE_WSDLS_SUPPORTED) {
         var1.addRow("Default Download Url", "https://webservices.netsuite.com/wsdl/v2013_1_0/netsuite.wsdl", null);
         var1.addRow(this.wsdlDownloadUrlField.getLabel(), this.wsdlDownloadUrlField.getInputComponent(), null);
      }

      var1.addRow(null, this.sandboxCheckBox.getInputComponent(), null);
   }

   @Override
   public void registerAsChangeSource(InputPanel var1) {
      var1.addInputField(this.sandboxCheckBox);
      var1.addInputField(this.emailField, this.emailField, this.emailField);
      var1.addInputField(this.wsdlDownloadUrlField, this.wsdlDownloadUrlField, this.wsdlDownloadUrlField);
   }

   @Override
   public ImmutableList<BoundProperty> getBoundProperties() {
      return ImmutableList.of(
         BoundPropertyFactory.toggleProperty("sandbox", this.sandboxCheckBox),
         BoundPropertyFactory.textProperty("email", this.emailField),
         BoundPropertyFactory.textProperty("wsdldownloadurl", this.wsdlDownloadUrlField)
      );
   }

   @Override
   public Disposable installAutoCompletion(ValueCache<? extends ConnectorEndpoint> var1) {
      DisposableCollection var2 = new DisposableCollection();
      var2.add(PropertyValueHistory.installAutoCompletion(this.emailField, var1, "email"));
      var2.add(PropertyValueHistory.installAutoCompletion(this.wsdlDownloadUrlField, var1, "wsdldownloadurl"));
      return var2;
   }
}
