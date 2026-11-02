package com.etlforge.integration.client.proxy.ui;

import com.google.common.base.Optional;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.UndoableEditListener;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.widget.password.PasswordField;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.IntegerInputField;
import org.etlforge.ui.input.TextInputField;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.NColumnPanel;
import org.etlforge.ui.text.NonEmptyTextEnablerCondition;
import org.etlforge.ui.widget.DocumentContentChangeListener;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.text.KongaTextField;
import org.etlforge.ui.widget.text.undo.UndoableEditTimer;
import org.etlforge.util.net.Port;
import org.etlforge.util.net.ProxySettings;

public final class ProxySettingsForm implements UiProvider {
   private final ProxySettingsForm.HostField hostField;
   private final IntegerInputField portField;
   private final TextInputField userField;
   private final PasswordField passwordField;
   private final TextInputField ntlmField;
   private final CheckBoxInputField laxCertPolicySelector;
   private final InputPanel inputPanel;
   private Receiver<Boolean> validStateReceiver = NoReceiver.create();
   private final ProxySettingsForm.ClearAction clearAction;
   private Optional<Action> saveAction = Optional.absent();

   public ProxySettingsForm() {
      Size.Columns var1 = Size.columns(20);
      this.hostField = new ProxySettingsForm.HostField(var1);
      this.portField = new IntegerInputField("Port", new KongaTextField(var1), 1, 65536, "P&ort:");
      this.portField.setRequired(false);
      this.userField = new TextInputField("User", new KongaTextField(var1), "&User:");
      this.passwordField = new PasswordField();
      this.passwordField.setShowPasswordText("Sho&w password");
      this.passwordField.setValidationEnabled(false);
      this.ntlmField = new TextInputField("NTLM", new KongaTextField(var1), "&NTLM Domain:");
      this.laxCertPolicySelector = new CheckBoxInputField("Certificate Policy", new KongaCheckBox("&Allow unverified certificates used by the proxy"));
      NonEmptyTextEnablerCondition.enableWhenFieldNotEmpty(this.laxCertPolicySelector, this.hostField.getInputComponent());
      this.clearAction = new ProxySettingsForm.ClearAction();
      this.inputPanel = this.createInputPanel();
      this.addValidationListener();
   }

   private InputPanel createInputPanel() {
      InputPanel var1 = new InputPanel();
      var1.addInputField(this.hostField, this.hostField, this.hostField);
      var1.addInputField(this.portField, this.portField, this.portField);
      var1.addInputField(this.userField);
      this.passwordField.addToInputPanel(var1);
      var1.addInputField(this.ntlmField);
      var1.addInputField(this.laxCertPolicySelector);
      var1.setSendInputChangeEvents(true);
      var1.setSendUndoEvents(true);
      return var1;
   }

   private void addValidationListener() {
      ProxySettingsForm.ValidationListener var1 = new ProxySettingsForm.ValidationListener();
      this.hostField.addDocumentListener(var1);
      this.portField.addDocumentListener(var1);
      this.userField.addDocumentListener(var1);
      this.passwordField.addDocumentListener(var1);
      this.ntlmField.addDocumentListener(var1);
   }

   private String getHost() {
      return this.hostField.getValue();
   }

   public void setSaveAction(Action var1) {
      this.saveAction = Optional.of(var1);
   }

   public void set(ProxySettings var1) {
      this.hostField.setValue(var1.getHost());
      int var2 = var1.getPort().getPort();
      if (var2 > 0) {
         this.portField.setValue(var2);
      } else {
         this.portField.setValue("");
      }

      this.userField.setValue(var1.getUser());
      this.passwordField.setPassword(var1.getPassword());
      this.ntlmField.setValue(var1.getNtlmDomain());
      this.laxCertPolicySelector.setSelected(var1.getCertPolicy() == ProxySettings.CertificatePolicy.TRUST_ALL);
      UndoableEditTimer.getTimer().sendNow();
   }

   public ProxySettings get() {
      return new ProxySettings(this.getHost(), this.getPort(), this.getUser(), this.passwordField.getPassword(), this.getNtlmDomain(), this.getCertPolicy());
   }

   private Port getPort() {
      try {
         return Port.valueOf(this.portField.getIntValue());
      } catch (NumberFormatException var2) {
         return Port.INVALID;
      }
   }

   private String getUser() {
      return this.userField.getValue().trim();
   }

   private String getNtlmDomain() {
      return this.ntlmField.getValue().trim();
   }

   private ProxySettings.CertificatePolicy getCertPolicy() {
      return this.laxCertPolicySelector.isEnabled() && this.laxCertPolicySelector.isSelected()
         ? ProxySettings.CertificatePolicy.TRUST_ALL
         : ProxySettings.CertificatePolicy.STRICT;
   }

   @Override
   public void decorate(UiCanvas var1) {
      NColumnPanel var2 = this.emptyGrid();
      var2.addRow(this.hostField.getLabel(), this.hostField.getInputComponent(), null);
      var2.addRow(this.portField.getLabel(), this.portField.getInputComponent(), null);
      var2.addRow(this.userField.getLabel(), this.userField.getInputComponent(), null);
      JComponent[] var3 = this.passwordField.getLayoutComponents();
      var2.addRow(var3[0], var3[1], var3[2]);
      var2.addRow(this.ntlmField.getLabel(), this.ntlmField.getInputComponent(), null);
      var2.addRow(null, this.laxCertPolicySelector.getInputComponent(), null);
      JComponent var4 = this.layoutButtons();
      var2.addRow(null, var4, null);
      var2.decorate(var1);
   }

   private NColumnPanel emptyGrid() {
      return new NColumnPanel(3)
         .setInsets(new Insets(0, 0, 10, 5))
         .setXWeights(0.0, 1.0, 0.0)
         .setYWeights(0.0, 0.0, 0.0)
         .setFills(GridBagLayoutPanel.Fill.NONE, GridBagLayoutPanel.Fill.HORIZONTAL, GridBagLayoutPanel.Fill.NONE);
   }

   private JComponent layoutButtons() {
      return this.saveAction.isPresent()
         ? BoxBuilder.horizontal().centerAligned().add(new KongaButton(this.saveAction.get()).withSmallActionIcon()).glue().add(this.clearAction).container()
         : InvisiblePanel.of(this.clearAction);
   }

   @Override
   public void requestFocus() {
      this.hostField.requestFocus();
   }

   public void setValidStateReceiver(Receiver<Boolean> var1) {
      boolean var2 = this.validate();
      var1.handle(var2);
      this.validStateReceiver = var1;
   }

   private boolean validate() {
      return this.getHost().length() > 0
         ? this.portField.isFieldValid()
         : this.getUser().isEmpty()
            && this.passwordField.getPassword().isEmpty()
            && this.getNtlmDomain().isEmpty()
            && this.portField.getValue().trim().isEmpty();
   }

   public void setEnabled(boolean var1) {
      this.hostField.setEnabled(var1);
      this.portField.setEnabled(var1);
      this.userField.setEnabled(var1);
      this.passwordField.setEnabled(var1);
      this.ntlmField.setEnabled(var1);
      this.clearAction.setEnabled(var1);
   }

   public void addInputChangeListener(InputChangeListener var1) {
      this.inputPanel.addInputChangeListener(var1);
   }

   public void removeInputChangeListener(InputChangeListener var1) {
      this.inputPanel.removeInputChangeListener(var1);
   }

   public void addUndoableEditListener(UndoableEditListener var1) {
      this.inputPanel.addUndoableEditListener(var1);
   }

   public void removeUndoableEditListener(UndoableEditListener var1) {
      this.inputPanel.removeUndoableEditListener(var1);
   }

   private class ClearAction extends AbstractAction {
      public ClearAction() {
         super("&Clear");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         ProxySettingsForm.this.set(ProxySettings.NONE);
      }
   }

   private class HostField extends TextInputField2 {
      public HostField(Size.Columns var2) {
         super("Host", new KongaTextField(var2), "&Host:");
         this.setAutoTrim(true);
      }

      @Override
      public boolean isInputValid(String var1) {
         return !StringUtils.isBlank(var1)
            ? true
            : ProxySettingsForm.this.portField.isEmpty()
               && ProxySettingsForm.this.userField.isEmpty()
               && ProxySettingsForm.this.passwordField.isEmpty()
               && ProxySettingsForm.this.ntlmField.isEmpty();
      }

      @Override
      public String getInvalidMessage(String var1) {
         return "Must provide a " + this.getName();
      }
   }

   private class ValidationListener extends DocumentContentChangeListener {
      private ValidationListener() {
      }

      @Override
      public void contentChanged(DocumentEvent var1) {
         boolean var2 = ProxySettingsForm.this.validate();
         ProxySettingsForm.this.hostField.decorateField();
         ProxySettingsForm.this.validStateReceiver.handle(var2);
      }
   }
}
