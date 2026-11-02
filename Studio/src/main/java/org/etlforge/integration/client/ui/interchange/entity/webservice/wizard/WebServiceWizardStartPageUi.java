package org.etlforge.integration.client.ui.interchange.entity.webservice.wizard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import org.etlforge.application.ui.job.JobMessageListener;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.ui.entity.wizard.WizardNameInputField;
import org.etlforge.integration.client.ui.interchange.entity.webservice.wsdl.WsdlProcessingCallback;
import org.etlforge.integration.client.ui.interchange.entity.webservice.wsdl.WsdlSelectorPanel;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.system.Disposable;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.undo.UndoRedoInstaller;
import org.etlforge.ui.widget.DocumentContentChangeListener;

final class WebServiceWizardStartPageUi implements UiProvider, Disposable {
   private final WebServiceWizardStartPage startPage;
   private InputPanel inputPanel;
   private TextInputField2 nameField;
   private WsdlSelectorPanel wsdlSelectorPanel;
   private boolean isDisplayed;

   public WebServiceWizardStartPageUi(WebServiceWizardStartPage var1, WebServiceCall var2, Folder var3) {
      this.startPage = var1;
      this.createComponents(var2, var3);
      this.createInputPanel();
      this.layoutInputPanel();
      this.registerListeners();
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.inputPanel.getUi());
   }

   @Override
   public void requestFocus() {
      JComponent var1 = this.getComponentToFocus();
      if (var1 != null) {
         var1.requestFocusInWindow();
      }

      if (!this.isDisplayed) {
         this.wsdlSelectorPanel.downloadExistingWsdls();
      }

      this.isDisplayed = true;
   }

   public void installUndoRedo(UndoRedoInstaller var1) {
      var1.install(this.nameField.getInputComponent());
      this.wsdlSelectorPanel.installUndoRedo(var1);
   }

   public String getName() {
      return this.nameField.getValue();
   }

   private JComponent getComponentToFocus() {
      return this.nameField.getInputComponent();
   }

   public void processSelectedWsdlFile(WsdlProcessingCallback var1, JobMessageListener var2) {
      this.wsdlSelectorPanel.processSelectedWsdlFile(var1, var2);
   }

   @Override
   public void dispose() {
      this.wsdlSelectorPanel.dispose();
   }

   private void createComponents(WebServiceCall var1, Folder var2) {
      this.nameField = new WizardNameInputField(Strings.mnemonicString("Wizard.StartPage.Label.Name"), var1, var2);
      this.nameField.setValue(var1.getName());
      this.wsdlSelectorPanel = this.createWsdlSelector(var2);
   }

   private void registerListeners() {
      WebServiceWizardStartPageUi.FieldListener var1 = new WebServiceWizardStartPageUi.FieldListener();
      this.nameField.getDocument().addDocumentListener(var1);
      ((JTextField)this.nameField.getInputComponent()).addActionListener(var1);
      this.wsdlSelectorPanel.addChangeListener(var1);
      this.wsdlSelectorPanel.addActionListener(var1);
   }

   private WsdlSelectorPanel createWsdlSelector(IntegrationEntity var1) {
      ManagedProject var2 = ProjectUtils.getManagedProject(var1);
      assert var2 != null : "The WebServiceCall about to be created is not associated with a project";
      return new WsdlSelectorPanel(var2.getFileStore());
   }

   private void checkNextPageEnabled() {
      boolean var1 = this.inputPanel.isInputValid() && this.wsdlSelectorPanel.inputIsValid();
      this.startPage.setCanContinue(var1);
   }

   private void createInputPanel() {
      this.inputPanel = new InputPanel();
      this.inputPanel.addInputField(this.nameField, this.nameField, this.nameField);
   }

   private void layoutInputPanel() {
      OneColumnPanel var1 = new OneColumnPanel();
      this.addNamePanel(var1);
      this.addWsdlPanel(var1);
      BorderLayoutBuilder.from(this.inputPanel.getUi()).center(var1);
   }

   private void addNamePanel(OneColumnPanel var1) {
      BoxBuilder var2 = BoxBuilder.horizontal();
      var2.addAllWithSpace(5, this.nameField.getLabel(), this.nameField.getInputComponent());
      var2.withBorder(Empty.border(0, 0, 20, 0));
      var1.add(var2.invisible());
   }

   private void addWsdlPanel(OneColumnPanel var1) {
      var1.setWeightX(1.0);
      var1.add(WizardStyleSheet.SMALL_CAPTION_STYLE.makeLabel(Strings.get("Wizard.StartPage.Label.WSDL")));
      this.wsdlSelectorPanel.setBorder(Empty.border(10, 10, 0, 20));
      var1.fillOut(this.wsdlSelectorPanel);
   }

   private class FieldListener extends DocumentContentChangeListener implements PropertyChangeListener, ActionListener {
      private FieldListener() {
      }

      @Override
      public void contentChanged(DocumentEvent var1) {
         this.handleChange();
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         this.handleChange();
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         WebServiceWizardStartPageUi.this.startPage.getWizard().nextPageRequested();
      }

      private void handleChange() {
         WebServiceWizardStartPageUi.this.inputPanel.decorateFields();
         WebServiceWizardStartPageUi.this.checkNextPageEnabled();
      }
   }
}
