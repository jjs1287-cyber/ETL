package com.etlforge.integration.client.ui.sfdc.ucu.wizard;

import com.etlforge.integration.client.ui.sfdc.DateOptionsEditor;
import java.util.List;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.salesforce.soap.ui.SoapHeaderUi;
import org.etlforge.integration.salesforce.soap.ui.SoapHeaderUiCollection;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.widget.button.KongaButton;

final class OptionsPanel {
   private final DateOptionsEditor dateOptionsEditor;
   private final CheckBoxInputField insertNullsBox = new CheckBoxInputField("Insert null values", "Insert &null values");
   private final CheckBoxInputField compressionBox = new CheckBoxInputField("Compress", "&Compress");
   private final SoapHeaderUiCollection soapHeaderUis;

   public OptionsPanel(List<SoapHeaderUi> var1) {
      this(SoapHeaderUiCollection.of(var1));
   }

   public OptionsPanel(SoapHeaderUiCollection var1) {
      this.soapHeaderUis = var1;
      this.compressionBox.setSelected(true);
      this.dateOptionsEditor = new DateOptionsEditor();
   }

   public void registerAsChangeSource(InputPanel var1) {
      var1.addInputField(this.insertNullsBox);
      var1.addInputField(this.compressionBox);
      this.dateOptionsEditor.registerAsChangeSource(var1);
      this.soapHeaderUis.registerAsChangeSource(var1);
   }

   public void layoutIn(TwoColumnLayout var1) {
      var1.addRow(new KongaButton(this.dateOptionsEditor.getEditAction()), null);
      this.insertNullsBox.layoutIn(var1);
      this.compressionBox.layoutIn(var1);
      this.soapHeaderUis.layoutIn(var1);
   }

   public void requestFocus() {
      this.insertNullsBox.requestFocus();
   }

   public Options createOptions(Schedule var1, boolean var2) {
      return new Options(
         this.dateOptionsEditor.getParams(),
         this.insertNullsBox.isSelected(),
         this.compressionBox.isSelected(),
         this.soapHeaderUis.getSoapHeaders(),
         var1,
         var2
      );
   }
}
