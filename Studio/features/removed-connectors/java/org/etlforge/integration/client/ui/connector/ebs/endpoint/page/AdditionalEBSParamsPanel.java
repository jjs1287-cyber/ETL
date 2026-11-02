package org.etlforge.integration.client.ui.connector.ebs.endpoint.page;

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

final class AdditionalEBSParamsPanel implements AdditionalParametersPanel {
   private final DefaultNonEmptyTextInputField connectURL = new DefaultNonEmptyTextInputField("Connect URL", new KongaTextField(20), "Connect URL");

   public AdditionalEBSParamsPanel() {
   }

   @Override
   public Properties getParameters() {
      Properties var1 = new Properties();
      var1.put("connecturl", this.connectURL.getValue());
      return var1;
   }

   @Override
   public void setParameters(@Nullable Properties var1) {
      if (var1 == null) {
         this.connectURL.setValue("ec2-23-20-205-167.compute-1.amazonaws.com:1521:vision");
      } else {
         this.connectURL.setValue(var1.getProperty("connecturl"));
      }
   }

   @Override
   public void layoutIn(NColumnPanel var1) {
      var1.addRow(this.connectURL.getLabel(), this.connectURL.getInputComponent(), null);
   }

   @Override
   public void registerAsChangeSource(InputPanel var1) {
      var1.addInputField(this.connectURL, this.connectURL, this.connectURL);
   }

   @Override
   public ImmutableList<BoundProperty> getBoundProperties() {
      return ImmutableList.of(BoundPropertyFactory.textProperty("connecturl", this.connectURL));
   }

   @Override
   public Disposable installAutoCompletion(ValueCache<? extends ConnectorEndpoint> var1) {
      DisposableCollection var2 = new DisposableCollection();
      var2.add(PropertyValueHistory.installAutoCompletion(this.connectURL, var1, "connecturl"));
      return var2;
   }
}
