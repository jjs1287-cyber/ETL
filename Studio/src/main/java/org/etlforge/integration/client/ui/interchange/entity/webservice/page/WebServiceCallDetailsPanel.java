package org.etlforge.integration.client.ui.interchange.entity.webservice.page;

import java.awt.Point;
import java.util.Arrays;
import java.util.Collection;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.integration.client.ui.http.HttpPropertiesEditor;
import org.etlforge.integration.client.ui.http.LaunchPropertiesEditorAction;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.integration.data.binding.ui.BoundPropertyFactory;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.http.HttpConnector;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.overlay.OverlayPositioner;
import org.etlforge.ui.widget.overlay.OverlayRelativePositioner;
import org.etlforge.ui.widget.text.KongaTextField;

final class WebServiceCallDetailsPanel extends InputPanel {
   private final TextInputField2 webServiceUrlField;
   private final JTextField wsdlUrlField;
   private final TextInputField soapActionField;
   private final JTextField serviceField;
   private final JTextField portField;
   private final JTextField bindingField;
   private final JTextField operationField;
   private final HttpPropertiesEditor httpPropertiesEditor;
   private final JButton editWsdlButton;
   private JButton httpPropertiesButton;

   public WebServiceCallDetailsPanel(WebServiceCall var1) {
      byte var2 = 40;
      byte var3 = 20;
      String var4 = Strings.get("Page.Label.Url");
      this.webServiceUrlField = InputFieldFactory.getNonEmptyTextInputField(var4, var4, var2);
      this.wsdlUrlField = getNonEditableField(var2);
      String var5 = Strings.get("Page.Label.SoapAction");
      this.soapActionField = new TextInputField(var5, new KongaTextField(var2), new JLabel(var5));
      this.serviceField = getNonEditableField(var3);
      this.portField = getNonEditableField(var3);
      this.bindingField = getNonEditableField(var3);
      this.operationField = getNonEditableField(var3);
      this.editWsdlButton = new JButton("Upload New WSDL...");
      this.editWsdlButton.setEnabled(false);
      this.httpPropertiesEditor = new WebServiceCallDetailsPanel.HttpPropertiesEditorImpl(var1);
      this.httpPropertiesEditor.addInputChangeListener(this);
      this.httpPropertiesEditor.addUndoRedoListener(this);
      this.layoutComponents();
      if (var1 != null) {
         this.setValues(var1);
      }

      this.addInputFields();
      UiUtils.setOpaqueRecursivelySkipDefaults(this.getUi(), false);
   }

   public void applyValuesTo(WebServiceCall var1) {
      var1.setUrl(this.webServiceUrlField.getValue());
      var1.setSoapAction(this.soapActionField.getValue());
      this.httpPropertiesEditor.apply();
   }

   public Collection<BoundProperty> getBoundPropertyControls() {
      return Arrays.asList(
         BoundPropertyFactory.textProperty("webservice_url", this.webServiceUrlField),
         new BoundWsdlPropertyControl(this.wsdlUrlField),
         new BoundOperationInfoPropertyControl(
            this.serviceField, this.portField, this.bindingField, this.operationField, (JTextField)this.soapActionField.getInputComponent()
         ),
         BoundPropertyFactory.textProperty("soapaction", this.soapActionField)
      );
   }

   public void setEditWsdlAction(Action var1) {
      this.editWsdlButton.setAction(var1);
   }

   private void layoutComponents() {
      TwoColumnPanel var1 = new TwoColumnPanel();
      JComponent var2 = InputFieldFactory.getCapsule(this.webServiceUrlField);
      JComponent var3 = InputFieldFactory.getLabeledComponent(this.serviceField, Strings.get("Page.Label.Service"));
      var1.addRow(var2, var3);
      JComponent var4 = InputFieldFactory.getLabeledComponent(this.wsdlUrlField, Strings.get("Page.Label.WsdlUrl"));
      JComponent var5 = InputFieldFactory.getLabeledComponent(this.portField, Strings.get("Page.Label.Port"));
      var1.addRow(var4, var5);
      JComponent var6 = InputFieldFactory.getCapsule(this.soapActionField);
      JComponent var7 = InputFieldFactory.getLabeledComponent(this.bindingField, Strings.get("Page.Label.Binding"));
      var1.addRow(var6, var7);
      BoxBuilder var8 = BoxBuilder.vertical();
      var8.addAllWithSpace(5, TextStyles.AdminHint.makeLabel(Strings.get("Page.Label.StructureChange")), this.editWsdlButton);
      JComponent var9 = InputFieldFactory.getLabeledComponent(this.operationField, Strings.get("Page.Label.Operation"));
      var1.addRow(var8.container(), var9);
      this.addAdvancedPropertiesButton(var1);
      this.wrap(var1);
   }

   private void addAdvancedPropertiesButton(TwoColumnPanel var1) {
      LaunchPropertiesEditorAction var2 = new LaunchPropertiesEditorAction(this.httpPropertiesEditor, Strings.get("Page.AdvancedProperties.Name"), null);
      var2.putValue("ShortDescription", Strings.get("Page.AdvancedProperties.ToolTip"));
      this.httpPropertiesButton = new KongaButton(var2);
      var2.setTrigger(this.httpPropertiesButton);
      var1.addRow(null, this.httpPropertiesButton);
   }

   private void addInputFields() {
      this.addInputField(this.webServiceUrlField, this.webServiceUrlField, this.webServiceUrlField);
      this.addInputField(this.soapActionField);
   }

   private void setValues(WebServiceCall var1) {
      this.webServiceUrlField.setValue(var1.getUrl());
      this.wsdlUrlField.setText(var1.getWsdlFile().getLocator());
      this.soapActionField.setValue(var1.getProperty("soapaction"));
      this.serviceField.setText(var1.getProperty("service"));
      this.portField.setText(var1.getProperty("port"));
      this.bindingField.setText(var1.getProperty("binding"));
      this.operationField.setText(var1.getProperty("operation"));
   }

   private static JTextField getNonEditableField(int var0) {
      KongaTextField var1 = new KongaTextField(var0);
      var1.setEditable(false);
      var1.setToolTipText(Strings.get("Page.Non-Editable"));
      return var1;
   }

   private class HttpPropertiesEditorImpl extends HttpPropertiesEditor {
      public HttpPropertiesEditorImpl(HttpConnector var2) {
         super(var2);
      }

      @Override
      protected OverlayPositioner getOverlayPositioner() {
         return new OverlayRelativePositioner(WebServiceCallDetailsPanel.this.httpPropertiesButton, new Point(8, 8));
      }

      @Override
      protected void overlayRemoved() {
         WebServiceCallDetailsPanel.this.httpPropertiesButton.requestFocusInWindow();
      }
   }
}
