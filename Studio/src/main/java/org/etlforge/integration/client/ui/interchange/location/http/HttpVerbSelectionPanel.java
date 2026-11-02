package org.etlforge.integration.client.ui.interchange.location.http;

import javax.swing.JPanel;
import org.etlforge.integration.client.ui.interchange.location.DataLocationPanelLayout;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.location.HttpLocation;
import org.etlforge.integration.http.HttpVerb;
import org.etlforge.ui.input.ComboBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.widget.combobox.EnumComboBox;
import org.etlforge.ui.widget.panel.InvisiblePanel;

final class HttpVerbSelectionPanel extends InputPanel implements HttpLocationPanelPart {
   private final SourceTarget sourceOrTarget;
   private final EnumComboBox<HttpVerb> options;

   public HttpVerbSelectionPanel(InputPanel var1, HttpLocation var2, SourceTarget var3) {
      this.sourceOrTarget = var3;
      this.options = new EnumComboBox<>(HttpLocation.getSupportedVerbs());
      this.options.setPrototypeDisplayValue("DELETE  ");
      this.options.setSelectedItem(getVerb(var2, var3));
      this.addInputField(new ComboBoxInputField("Verb", this.options));
      var1.addInputPanel(this);
      this.setSendInputChangeEvents(true);
      this.setSendUndoEvents(true);
   }

   @Override
   public void layout(TwoColumnLayout var1) {
      var1.addRow(this.getUi());
   }

   @Override
   public void addToLayout(DataLocationPanelLayout var1) {
      var1.add(null, this.getUi(), true);
   }

   public void applyTo(HttpLocation var1) {
      var1.setHttpVerb(this.options.getSelectedItem());
   }

   public void reset(HttpLocation var1) {
      this.options.setSelectedItem(getVerb(var1, this.sourceOrTarget));
   }

   @Override
   public JPanel getUi() {
      BoxBuilder var1 = BoxBuilder.horizontal();
      var1.addAllWithSpace(10, Strings.get("Location.Http.Label.Verb"), this.options);
      return InvisiblePanel.of(var1);
   }

   private static HttpVerb getVerb(HttpLocation var0, SourceTarget var1) {
      HttpVerb var2 = var0.getHttpVerb();
      if (var2 == null || !HttpLocation.getSupportedVerbs().contains(var2)) {
         var2 = HttpLocation.getDefaultVerb(var1);
      }

      return var2;
   }
}
