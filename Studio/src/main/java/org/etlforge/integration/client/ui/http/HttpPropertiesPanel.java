package org.etlforge.integration.client.ui.http;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.integration.dataelement.ui.DeTemplateTransferSupport;
import org.etlforge.integration.http.HttpConnector;
import org.etlforge.integration.http.HttpProperties;
import org.etlforge.integration.http.HttpVerb;
import org.etlforge.integration.http.SslVersion;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.text.KongaTextArea;

public final class HttpPropertiesPanel extends InputPanel {
   private final CheckBoxInputField enableContentEncodingField;
   private final CheckBoxInputField enableChunkedTransferEncodingField;
   private final CheckBoxInputField sendExpect100ContinueField;
   private final SslVersionSelector sslVersionSelector;
   private final KongaTextArea requestHeadersBox;

   public HttpPropertiesPanel(HttpConnector var1) {
      this(var1.getHttpProperties(), var1.getSslVersion(), var1.getRequestHeaders());
      this.setSendExpect100ContinueEnabled(var1.getHttpVerb() == HttpVerb.POST);
   }

   public HttpPropertiesPanel(HttpProperties var1, SslVersion var2, String var3) {
      this.enableContentEncodingField = this.createField("contentEncoding", var1.isContentEncodingEnabled());
      this.enableChunkedTransferEncodingField = this.createField("chunkedTransferEncoding", var1.isChunkedTransferEncodingEnabled());
      this.sendExpect100ContinueField = this.createField("sendExpect100", var1.isSendingExpect100Continue());
      makeExclusive(this.enableChunkedTransferEncodingField, this.sendExpect100ContinueField);
      this.sslVersionSelector = this.createSslVersionSelector(var2);
      this.requestHeadersBox = this.createRequestHeadersBox(var3);
      this.layout();
      this.setSendInputChangeEvents(true);
      this.setSendUndoEvents(true);
   }

   private CheckBoxInputField createField(String var1, boolean var2) {
      String var3 = Strings.get("HttpPropertiesPanel." + var1);
      CheckBoxInputField var4 = InputFieldFactory.getCheckBoxInputField(var1, var3, var2);
      this.addInputField(var4);
      return var4;
   }

   private static void makeExclusive(CheckBoxInputField var0, CheckBoxInputField var1) {
      JCheckBox var2 = var0.getInputComponent();
      JCheckBox var3 = var1.getInputComponent();
      var2.addActionListener(new HttpPropertiesPanel.ExclusiveChoiceOptionListener(var2, var3));
      var3.addActionListener(new HttpPropertiesPanel.ExclusiveChoiceOptionListener(var3, var2));
   }

   private SslVersionSelector createSslVersionSelector(SslVersion var1) {
      SslVersionSelector var2 = new SslVersionSelector(var1);
      var2.registerAsChangeSource(this);
      return var2;
   }

   private KongaTextArea createRequestHeadersBox(String var1) {
      KongaTextArea var2 = new KongaTextArea(var1, 4, 20);
      var2.setLabel(Strings.get("HttpPropertiesPanel.requestHeaders"));
      var2.setLineWrap(false);
      var2.setWrapStyleWord(false);
      var2.disableTabs();
      DeTemplateTransferSupport.installDropHandler(var2);
      this.addInputField(new TextInputField("request header", var2));
      return var2;
   }

   public HttpProperties getDefinedProperties() {
      return new HttpProperties(
         this.enableContentEncodingField.isSelected(), this.enableChunkedTransferEncodingField.isSelected(), this.sendExpect100ContinueField.isSelected()
      );
   }

   public SslVersion getSslVersion() {
      return this.sslVersionSelector.getSelectedVersion();
   }

   public String getRequestHeaders() {
      return this.requestHeadersBox.getText().trim();
   }

   public void setSendExpect100ContinueEnabled(boolean var1) {
      this.sendExpect100ContinueField.setEnabled(var1);
   }

   public void requestFocus() {
      this.enableContentEncodingField.requestFocus();
   }

   private void layout() {
      TwoColumnPanel var1 = new TwoColumnPanel();
      this.enableContentEncodingField.layoutIn(var1);
      this.enableChunkedTransferEncodingField.layoutIn(var1);
      this.sendExpect100ContinueField.layoutIn(var1);
      BoxBuilder var2 = Horizontal.box().addAllWithSpace(48, var1, this.sslVersionSelector);
      BorderLayoutBuilder.from(this.getUi(), 0, 10).north(InvisiblePanel.of(var2)).center(this.requestHeadersBox.asUiProvider());
   }

   private static class ExclusiveChoiceOptionListener implements ActionListener {
      private final JCheckBox listenedTo;
      private final JCheckBox other;

      public ExclusiveChoiceOptionListener(JCheckBox var1, JCheckBox var2) {
         this.listenedTo = var1;
         this.other = var2;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (this.listenedTo.isSelected()) {
            this.other.setSelected(false);
         }
      }
   }
}
