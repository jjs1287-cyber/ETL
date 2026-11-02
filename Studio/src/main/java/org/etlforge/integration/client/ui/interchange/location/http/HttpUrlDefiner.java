package org.etlforge.integration.client.ui.interchange.location.http;

import javax.swing.event.DocumentListener;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.integration.client.ui.interchange.location.DataLocationPanelLayout;
import org.etlforge.integration.data.location.HttpLocation;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.text.contentassist.StringValueHistory;

final class HttpUrlDefiner implements HttpLocationPanelPart {
   private final HttpUrlInputField urlField;

   public HttpUrlDefiner(InputPanel var1) {
      this.urlField = this.createInputField(var1);
   }

   private HttpUrlInputField createInputField(InputPanel var1) {
      HttpUrlInputField var2 = new HttpUrlInputField(Strings.get("Http.URL.Name"), Strings.get("Http.URL.Label"));
      var1.addInputField(var2, var2, var2);
      return var2;
   }

   public void addChangeListener(DocumentListener var1) {
      this.urlField.addDocumentListener(var1);
   }

   public boolean isHttps() {
      return this.urlField.getValue().toLowerCase().startsWith("https://");
   }

   @Override
   public void layout(TwoColumnLayout var1) {
      var1.addRow(InputFieldFactory.getCapsule(this.urlField));
   }

   @Override
   public void addToLayout(DataLocationPanelLayout var1) {
      var1.add(this.urlField);
   }

   public void requestFocus() {
      this.urlField.requestFocus();
   }

   public void applyTo(HttpLocation var1) {
      var1.setUrl(this.urlField.getValue().trim());
   }

   public void reset(HttpLocation var1) {
      this.urlField.setValue(var1.getUrl());
   }

   @Override
   public void dispose() {
      this.urlField.dispose();
   }

   public void setHistory(StringValueHistory var1) {
      this.urlField.setHistory(var1);
   }
}
