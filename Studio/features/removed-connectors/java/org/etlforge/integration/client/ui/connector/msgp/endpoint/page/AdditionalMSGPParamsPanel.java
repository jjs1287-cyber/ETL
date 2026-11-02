package org.etlforge.integration.client.ui.connector.msgp.endpoint.page;

import com.google.common.collect.ImmutableList;
import java.util.Properties;
import javax.annotation.Nullable;
import org.etlforge.integration.client.entity.valuecache.ValueCache;
import org.etlforge.integration.client.ui.connector.AdditionalParametersPanel;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.integration.data.binding.ui.BoundPropertyFactory;
import org.etlforge.integration.data.binding.ui.PropertyValueHistory;
import org.etlforge.integration.data.connector.ConnectorEndpoint;
import org.etlforge.system.Disposable;
import org.etlforge.system.DisposableCollection;
import org.etlforge.ui.input.DefaultNonEmptyTextInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.NColumnPanel;
import org.etlforge.ui.widget.text.KongaTextField;

final class AdditionalMSGPParamsPanel implements AdditionalParametersPanel {
   private final DefaultNonEmptyTextInputField databaseName = new DefaultNonEmptyTextInputField("Database", new KongaTextField(20), "Database");
   private final DefaultNonEmptyTextInputField serviceURL = new DefaultNonEmptyTextInputField("Service URL", new KongaTextField(20), "Service URL");

   public AdditionalMSGPParamsPanel() {
   }

   @Override
   public Properties getParameters() {
      Properties var1 = new Properties();
      var1.put("databasename", this.databaseName.getValue());
      var1.put("service_url", this.serviceURL.getValue());
      return var1;
   }

   @Override
   public void setParameters(@Nullable Properties var1) {
      if (var1 == null) {
         this.databaseName.setValue("");
         this.serviceURL.setValue("http://localhost:8001/GP");
      } else {
         this.databaseName.setValue(var1.getProperty("databasename"));
         this.serviceURL.setValue(var1.getProperty("service_url"));
      }
   }

   @Override
   public void layoutIn(NColumnPanel var1) {
      var1.addRow(this.databaseName.getLabel(), this.databaseName.getInputComponent(), null);
      var1.addRow(this.serviceURL.getLabel(), this.serviceURL.getInputComponent(), null);
   }

   @Override
   public void registerAsChangeSource(InputPanel var1) {
      var1.addInputField(this.databaseName, this.databaseName, this.databaseName);
      var1.addInputField(this.serviceURL, this.serviceURL, this.serviceURL);
   }

   @Override
   public ImmutableList<BoundProperty> getBoundProperties() {
      return ImmutableList.of(
         BoundPropertyFactory.textProperty("databasename", this.databaseName), BoundPropertyFactory.textProperty("service_url", this.serviceURL)
      );
   }

   @Override
   public Disposable installAutoCompletion(ValueCache<? extends ConnectorEndpoint> var1) {
      DisposableCollection var2 = new DisposableCollection();
      var2.add(PropertyValueHistory.installAutoCompletion(this.databaseName, var1, "databasename"));
      var2.add(PropertyValueHistory.installAutoCompletion(this.serviceURL, var1, "service_url"));
      return var2;
   }
}
