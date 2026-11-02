package org.etlforge.integration.client.ui.interchange.entity.webservice.wizard;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.application.ui.wizard.WizardPageException;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.client.ui.interchange.entity.webservice.page.WebServiceCallRestrictionsPanel;
import org.etlforge.integration.client.ui.interchange.entity.webservice.page.WebServiceCallSecurityPanel;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.webservicecall.WebServiceInfo;
import org.etlforge.integration.data.structure.WebServiceOperationInfo;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.undo.UndoRedoInstaller;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.text.KongaTextField;

final class WebServiceWizardDetailsPage extends AbstractWebServiceWizardPage {
   private WebServiceInfo webServiceInfo;
   private WebServiceOperationInfo operationInfo;
   private final JTextField webServiceUrlField = new KongaTextField(30);
   private final JButton defaultUrlButton = new KongaButton();
   private final JTextField soapActionField = new KongaTextField(30);
   private final JButton defaultSoapActionButton = new KongaButton();
   private final WebServiceCallRestrictionsPanel restrictionsPanel;
   private final WebServiceCallSecurityPanel securityPanel = this.createSecurityPanel();
   private JComponent inputPanel;

   public WebServiceWizardDetailsPage(WebServiceWizard var1) {
      super(var1, lookupSubTitle());
      this.restrictionsPanel = this.createRestrictionsPanel();
      this.layoutComponents();
      this.installUndoRedo();
      this.setCanContinue(true);
   }

   public void setWebServiceInfo(WebServiceInfo var1) {
      this.webServiceInfo = var1;
      this.webServiceUrlField.setText(var1.getWebServiceURL());
      this.defaultUrlButton.setAction(new WebServiceWizardDetailsPage.SetDefaultAction(this.webServiceInfo.getWebServiceURL(), this.webServiceUrlField));
   }

   public void setOperationInfo(WebServiceOperationInfo var1) {
      this.operationInfo = var1;
      this.soapActionField.setText(this.operationInfo.getProperty("SoapAction"));
      this.defaultSoapActionButton
         .setAction(new WebServiceWizardDetailsPage.SetDefaultAction(this.operationInfo.getProperty("SoapAction"), this.soapActionField));
   }

   private void installUndoRedo() {
      UndoRedoInstaller var1 = this.getUndoRedoInstaller();
      var1.install(this.webServiceUrlField);
      var1.install(this.soapActionField);
      this.restrictionsPanel.installUndoRedo(var1);
      this.securityPanel.installUndoRedo(var1);
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout(this.getMainCaption(), this.getSubCaption());
      var1.add(this.inputPanel, true, true);
      return var1.asCenterBorderLayout();
   }

   @Override
   public void applyToWebServiceCall(WebServiceCall var1) throws WizardPageException {
      var1.setUrl(this.webServiceUrlField.getText());
      var1.setSoapAction(this.soapActionField.getText());
      var1.setTargetNamespace(this.webServiceInfo.getTargetNamespace());
      var1.setOtherNamespaces(this.webServiceInfo.getOtherNamespacesString());
      this.securityPanel.applyValuesTo(var1);

      try {
         var1.setCallRestrictions(this.restrictionsPanel.getCallRestrictions(var1));
      } catch (IllegalStateException var3) {
         throw new WizardPageException(this, var3);
      }
   }

   @Override
   public void requestFocus() {
      this.webServiceUrlField.requestFocusInWindow();
   }

   @Override
   protected String getMainCaption() {
      return Strings.get("Wizard.DetailsPage.MainCaption");
   }

   @Override
   protected String getSubCaption() {
      return Strings.get("Wizard.DetailsPage.SubCaption");
   }

   private static String lookupSubTitle() {
      return Strings.get("Wizard.DetailsPage.SubTitle");
   }

   private WebServiceCallSecurityPanel createSecurityPanel() {
      WebServiceCallSecurityPanel var1 = new WebServiceCallSecurityPanel();
      Border var2 = StyleSheet.getDefaultTitleBorder(Strings.get("Wizard.DetailsPage.Title.Security"));
      var1.setBorder(var2);
      return var1;
   }

   private WebServiceCallRestrictionsPanel createRestrictionsPanel() {
      WebServiceCallRestrictionsPanel var1 = new WebServiceCallRestrictionsPanel();
      Border var2 = StyleSheet.getDefaultTitleBorder(Strings.get("Wizard.DetailsPage.Title.Restrictions"));
      var1.setBorder(var2);
      return var1;
   }

   private void layoutComponents() {
      OneColumnPanel var1 = new OneColumnPanel();
      var1.withInsets(5, 5, 5, 5);
      var1.add(this.getUrlAndSoapActionPanel());
      var1.add(this.securityPanel.getUi());
      var1.add(this.restrictionsPanel.getUi());
      this.inputPanel = InvisiblePanel.of(var1);
   }

   private UiProvider getUrlAndSoapActionPanel() {
      return new OneColumnPanel()
         .withInsets(0, 5, 5, 5)
         .withBorder(StyleSheet.getDefaultTitleBorder(Strings.get("Wizard.DetailsPage.Title.UrlAndAction")))
         .setFill(OneColumnPanel.NONE)
         .add(TextStyles.InfoText.makeLabel(Strings.get("Wizard.DetailsPage.Default.Caption")))
         .withInsets(5, 5, 5, 5)
         .addAll(this.getUrlCapsule(), this.getSoapActionCapsule());
   }

   private JComponent getUrlCapsule() {
      BoxBuilder var1 = BoxBuilder.horizontal();
      var1.addAllWithSpace(10, this.webServiceUrlField, this.defaultUrlButton);
      return InputFieldFactory.getLabeledComponent(var1.container(), Strings.get("Wizard.DetailsPage.Label.URL"));
   }

   private JComponent getSoapActionCapsule() {
      BoxBuilder var1 = BoxBuilder.horizontal();
      var1.addAllWithSpace(10, this.soapActionField, this.defaultSoapActionButton);
      return InputFieldFactory.getLabeledComponent(var1.container(), Strings.get("Wizard.DetailsPage.Label.SoapAction"), 2);
   }

   private static class SetDefaultAction extends AbstractAction {
      private final String defaultValue;
      private final JTextComponent component;

      public SetDefaultAction(String var1, JTextComponent var2) {
         super(Strings.get("Wizard.DetailsPage.Default.Button"));
         this.defaultValue = var1;
         this.component = var2;
         this.putValue("ShortDescription", Strings.get("Wizard.DetailsPage.Default.ToolTip"));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.component.setText(this.defaultValue);
      }
   }
}
