package org.etlforge.integration.client.ui.connector.sap.endpoint.page;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import java.util.Properties;
import org.etlforge.application.ui.widget.TextFieldCompletionSupport;
import org.etlforge.integration.client.entity.valuecache.ValueCache;
import org.etlforge.integration.client.ui.connector.AdditionalParametersPanel;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.integration.data.binding.ui.BoundPropertyFactory;
import org.etlforge.integration.data.binding.ui.PropertyValueHistory;
import org.etlforge.integration.data.connector.ConnectorEndpoint;
import org.etlforge.system.Disposable;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField;
import org.etlforge.ui.layout.NColumnPanel;
import org.etlforge.ui.text.contentassist.StringValueHistory;
import org.etlforge.ui.widget.text.KongaTextField;

final class AdditionalSapParamsPanel implements AdditionalParametersPanel {
   private final ImmutableList<AdditionalSapParamsPanel.Field> fields = ImmutableList.of(
      new AdditionalSapParamsPanel.Field("System number", "systemnumber"),
      new AdditionalSapParamsPanel.Field("Client", "client"),
      new AdditionalSapParamsPanel.Field("Language", "lang")
   );

   @Override
   public void layoutIn(NColumnPanel var1) {
      assert var1.getColumnCount() == 3;

      for (AdditionalSapParamsPanel.Field var3 : this.fields) {
         var3.layoutIn(var1);
      }
   }

   @Override
   public void registerAsChangeSource(InputPanel var1) {
      for (AdditionalSapParamsPanel.Field var3 : this.fields) {
         var1.addInputField(var3.field);
      }
   }

   @Override
   public Properties getParameters() {
      Properties var1 = new Properties();

      for (AdditionalSapParamsPanel.Field var3 : this.fields) {
         var3.applyTo(var1);
      }

      return var1;
   }

   @Override
   public void setParameters(Properties var1) {
      for (AdditionalSapParamsPanel.Field var3 : this.fields) {
         var3.syncUi(var1);
      }
   }

   @Override
   public ImmutableList<BoundProperty> getBoundProperties() {
      return FluentIterable.from(this.fields).transform(new Function<AdditionalSapParamsPanel.Field, BoundProperty>() {
         public BoundProperty apply(AdditionalSapParamsPanel.Field var1) {
            return var1.asBoundProperty();
         }
      }).toImmutableList();
   }

   @Override
   public Disposable installAutoCompletion(ValueCache<? extends ConnectorEndpoint> var1) {
      for (AdditionalSapParamsPanel.Field var3 : this.fields) {
         var3.installAutoCompletion(var1);
      }

      return new Disposable() {
         @Override
         public void dispose() {
            for (AdditionalSapParamsPanel.Field var2 : AdditionalSapParamsPanel.this.fields) {
               var2.dispose();
            }
         }
      };
   }

   private static class Field {
      private final String property;
      public final TextInputField field;
      private TextFieldCompletionSupport autoCompletion;

      public Field(String var1, String var2) {
         this.property = var2;
         this.field = new TextInputField(var1, new KongaTextField(20), var1);
         this.field.setAutoTrim(true);
      }

      public void applyTo(Properties var1) {
         var1.setProperty(this.property, this.field.getValue());
      }

      public void syncUi(Properties var1) {
         String var2 = var1 == null ? null : var1.getProperty(this.property);
         this.field.setValue(var2);
      }

      public void layoutIn(NColumnPanel var1) {
         var1.addRow(this.field.getLabel(), this.field.getInputComponent(), null);
      }

      public BoundProperty asBoundProperty() {
         return BoundPropertyFactory.textProperty(this.property, this.field);
      }

      public void installAutoCompletion(ValueCache<? extends ConnectorEndpoint> var1) {
         StringValueHistory var2 = PropertyValueHistory.of(var1, this.property);
         this.autoCompletion = new TextFieldCompletionSupport(this.field.getInputComponent(), var2);
      }

      public void dispose() {
         if (this.autoCompletion != null) {
            this.autoCompletion.dispose();
         }
      }
   }
}
