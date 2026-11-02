package org.etlforge.integration.client.ui.connector.mscrm.endpoint.page;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import java.util.Properties;
import javax.annotation.Nullable;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.integration.client.entity.valuecache.ValueCache;
import org.etlforge.integration.client.ui.connector.AdditionalParametersPanel;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.integration.data.binding.ui.BoundPropertyFactory;
import org.etlforge.integration.data.binding.ui.PropertyValueHistory;
import org.etlforge.integration.data.connector.ConnectorEndpoint;
import org.etlforge.integration.data.connector.MSCrmEndpoint;
import org.etlforge.system.Disposable;
import org.etlforge.system.DisposableCollection;
import org.etlforge.ui.input.ComboBoxInputField;
import org.etlforge.ui.input.DefaultNonEmptyTextInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField;
import org.etlforge.ui.layout.NColumnPanel;
import org.etlforge.ui.widget.text.KongaTextField;

final class AdditionalMSCrmParamsPanel implements AdditionalParametersPanel {
   private final DefaultNonEmptyTextInputField orgUniqueName = new DefaultNonEmptyTextInputField(
      "Organization Unique Name", new KongaTextField(20), "Organization Unique Name"
   );
   private final TextInputField domainName = new TextInputField("Domain Name", new KongaTextField(20), "Domain Name");
   private final ComboBoxInputField typeSelectorCombo = InputFieldFactory.getComboBoxInputField(
      "Connectivity Type", new JComboBox<>(MSCrmEndpoint.ConnectivityType.values()), new JLabel("Connectivity Type")
   );

   public AdditionalMSCrmParamsPanel() {
   }

   @Override
   public Properties getParameters() {
      Properties var1 = new Properties();
      var1.put("organizationUniqueName", this.orgUniqueName.getValue());
      var1.put("domain", this.domainName.getValue());
      var1.put("authType", ((MSCrmEndpoint.ConnectivityType)this.typeSelectorCombo.getSelectedItem()).getApiName());
      return var1;
   }

   @Override
   public void setParameters(@Nullable Properties var1) {
      if (var1 == null) {
         this.orgUniqueName.setValue("");
         this.domainName.setValue("");
         this.typeSelectorCombo.setSelectedItem(MSCrmEndpoint.ConnectivityType.defaultType());
      } else {
         String var2 = var1.getProperty("authType");
         MSCrmEndpoint.ConnectivityType var3 = Strings.isNullOrEmpty(var2)
            ? MSCrmEndpoint.ConnectivityType.defaultType()
            : MSCrmEndpoint.ConnectivityType.fromApiName(var2);
         this.typeSelectorCombo.setSelectedItem(var3);
         this.orgUniqueName.setValue(var1.getProperty("organizationUniqueName"));
         this.domainName.setValue(var1.getProperty("domain"));
      }
   }

   @Override
   public void layoutIn(NColumnPanel var1) {
      var1.addRow(new JLabel("Connectivity type:"), this.typeSelectorCombo.getInputComponent(), null);
      var1.addRow(this.orgUniqueName.getLabel(), this.orgUniqueName.getInputComponent(), null);
      var1.addRow(this.domainName.getLabel(), this.domainName.getInputComponent(), null);
   }

   @Override
   public void registerAsChangeSource(InputPanel var1) {
      var1.addInputField(this.orgUniqueName, this.orgUniqueName, this.orgUniqueName);
      var1.addInputField(this.domainName);
      var1.addInputField(this.typeSelectorCombo);
   }

   @Override
   public ImmutableList<BoundProperty> getBoundProperties() {
      return ImmutableList.of(
         BoundPropertyFactory.textProperty("organizationUniqueName", this.orgUniqueName),
         BoundPropertyFactory.textProperty("domain", this.domainName),
         BoundPropertyFactory.comboProperty("authType", this.typeSelectorCombo)
      );
   }

   @Override
   public Disposable installAutoCompletion(ValueCache<? extends ConnectorEndpoint> var1) {
      DisposableCollection var2 = new DisposableCollection();
      var2.add(PropertyValueHistory.installAutoCompletion(this.orgUniqueName, var1, "organizationUniqueName"));
      var2.add(PropertyValueHistory.installAutoCompletion(this.domainName, var1, "domain"));
      return var2;
   }
}
