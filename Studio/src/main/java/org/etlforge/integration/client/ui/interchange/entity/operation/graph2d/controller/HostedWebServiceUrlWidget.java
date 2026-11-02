package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.event.DocumentEvent;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.webservicecall.HostedWebServiceConfiguration;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.NColumnPanel;
import org.etlforge.ui.util.CopyToClipboardButton;
import org.etlforge.ui.util.Fonts;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.DocumentContentChangeListener;
import org.etlforge.ui.widget.text.IntegerDocument;
import org.etlforge.ui.widget.text.KongaTextField;
import org.etlforge.ui.widget.text.TextComponentStringSupplier;

final class HostedWebServiceUrlWidget implements UiProvider {
   private final OperationId opId;
   private final KongaTextField serverField;
   private final KongaTextField portField;
   private final KongaTextField urlField;

   public HostedWebServiceUrlWidget(OperationId var1) {
      this.opId = var1;
      this.serverField = this.createServerField();
      this.portField = this.createPortField();
      this.urlField = this.createUrlField();
      this.updateUrl();
      this.listenToChanges();
   }

   private KongaTextField createServerField() {
      KongaTextField var1 = createField("&Server:", 20);
      HostedWebServiceUrlWidget.InsertLocalHostAction var2 = new HostedWebServiceUrlWidget.InsertLocalHostAction();
      var1.addActionToContextMenu(var2);
      ActionKeyBinding.install(var2, var1);
      ActionKeyBinding.install(new HostedWebServiceUrlWidget.UseDebugServerAction(), var1);
      return var1;
   }

   private static KongaTextField createField(String var0, int var1) {
      KongaTextField var2 = new KongaTextField(var1);
      var2.setLabel(var0);
      var2.setSelectAllWhenFocused(true);
      return var2;
   }

   private KongaTextField createPortField() {
      KongaTextField var1 = createField("&Port:", 6);
      var1.setDocument(new IntegerDocument());
      var1.setText("46908");
      return var1;
   }

   private KongaTextField createUrlField() {
      KongaTextField var1 = createField("&URL:", 50);
      var1.setEditable(false);
      Fonts.convertToBold(var1);
      var1.setBackground(org.etlforge.application.ui.look.Colors.FIELD_BACKGROUND);
      var1.setForeground(org.etlforge.application.ui.look.Colors.TEXT_FOREGROUND);
      return var1;
   }

   private void updateUrl() {
      String var1 = this.serverField.getText().trim();
      String var2 = this.portField.getText().trim();
      String var3 = HostedWebServiceConfiguration.getWebServiceUrl(var1, var2, this.opId);
      this.urlField.setText(var3);
      this.urlField.setCaretPosition(0);
   }

   public UiProvider getCopyToClipboardButton() {
      return new CopyToClipboardButton(new TextComponentStringSupplier(this.urlField));
   }

   private void listenToChanges() {
      HostedWebServiceUrlWidget.UrlConstructor var1 = new HostedWebServiceUrlWidget.UrlConstructor();
      this.serverField.getDocument().addDocumentListener(var1);
      this.portField.getDocument().addDocumentListener(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      NColumnPanel var2 = new NColumnPanel(4, GridBagLayoutPanel.Anchor.WEST);
      var2.setXWeights(0.0, 1.0, 0.0, 0.0);
      var2.setFills(GridBagLayoutPanel.Fill.NONE, GridBagLayoutPanel.Fill.HORIZONTAL, GridBagLayoutPanel.Fill.NONE, GridBagLayoutPanel.Fill.HORIZONTAL);
      var2.setVGap(16).setHGaps(5, 10, 5, 0);
      var2.addRow(this.serverField.getLabel(), this.serverField, this.portField.getLabel(), this.portField);
      var2.setVGap(0);
      var2.add(this.urlField.getLabel()).add(this.urlField, 3);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.serverField.requestFocusInWindow();
   }

   private class InsertLocalHostAction extends AbstractAction {
      public InsertLocalHostAction() {
         super("\"localhost\"");
         this.putValue("AcceleratorKey", KeyUtils.getCtrlDown(76));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         HostedWebServiceUrlWidget.this.serverField.setText("localhost");
      }
   }

   private class UrlConstructor extends DocumentContentChangeListener {
      private UrlConstructor() {
      }

      @Override
      public void contentChanged(DocumentEvent var1) {
         HostedWebServiceUrlWidget.this.updateUrl();
      }
   }

   private class UseDebugServerAction extends AbstractAction {
      public UseDebugServerAction() {
         this.putValue("AcceleratorKey", KeyUtils.getCtrlDown(68));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         HostedWebServiceUrlWidget.this.serverField.setText("localhost");
         HostedWebServiceUrlWidget.this.portField.setText("8888");
      }
   }
}

