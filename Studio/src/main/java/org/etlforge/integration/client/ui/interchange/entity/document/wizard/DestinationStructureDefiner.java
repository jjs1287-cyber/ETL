package org.etlforge.integration.client.ui.interchange.entity.document.wizard;

import com.google.common.base.Optional;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.integration.client.ui.interchange.entity.document.page.DelimiterAndQualifierPanel;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.structure.TextStructure;
import org.etlforge.integration.data.structure.text.FieldDelimiter;
import org.etlforge.integration.data.structure.text.QualifierMode;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.DelayedJob;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.panel.InvisiblePanel;

final class DestinationStructureDefiner implements UiProvider {
   private final ChangeListener externalChangeListener;
   private final IntegrationEntity documentOwner;
   private final JCheckBox activator;
   private final TextInputField2 nameField;
   private final DelimiterAndQualifierPanel delimiterPanel;
   private boolean enabled;
   private final boolean includeDelimiters;
   private JLabel notAvailableMessage;

   public DestinationStructureDefiner(String var1, boolean var2, IntegrationEntity var3, ChangeListener var4) {
      this.externalChangeListener = var4;
      this.documentOwner = var3;
      this.includeDelimiters = var2;
      this.activator = this.createActivator(var1);
      DestinationStructureDefiner.InputChangeListenerImpl var5 = new DestinationStructureDefiner.InputChangeListenerImpl();
      this.nameField = this.createNameField(var5);
      this.delimiterPanel = this.createDelimiterPanel(var5);
      this.enabled = true;
      this.setEnabled(false);
   }

   public void setNotAvailable(String var1) {
      this.activator.setSelected(false);
      this.activator.setEnabled(false);
      this.setEnabled(false);
      this.notAvailableMessage = TextStyles.DefaultErrorText.makeLabel(var1);
   }

   public void select() {
      assert this.activator.isEnabled() : "This selector is not enabled";
      this.activator.setSelected(true);
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.createDisplay());
   }

   private JComponent createDisplay() {
      OneColumnPanel var1 = new OneColumnPanel();
      this.addActivator(var1);
      this.addInputControls(var1);
      return var1.deeplyNonOpaque().invisible();
   }

   private void addActivator(OneColumnPanel var1) {
      var1.withInsets(2, 5, 5, 5);
      var1.add(this.activator);
   }

   private void addInputControls(OneColumnPanel var1) {
      var1.withInsets(2, 25, 3, 5);
      if (this.notAvailableMessage != null) {
         var1.add(this.notAvailableMessage);
      }

      var1.add(this.createNameCapsule());
      if (this.includeDelimiters) {
         var1.add(this.delimiterPanel.getUi());
      }
   }

   private JComponent createNameCapsule() {
      JComponent var1 = InputFieldFactory.getCapsule(this.nameField);
      return InvisiblePanel.of(var1);
   }

   @Override
   public void requestFocus() {
      this.activator.requestFocusInWindow();
   }

   public boolean isActivated() {
      return this.activator.isSelected();
   }

   private void setEnabled(boolean var1) {
      if (var1 != this.enabled) {
         this.nameField.setEnabled(var1);
         this.nameField.setActive(var1);
         this.delimiterPanel.setEnabled(var1);
         this.enabled = var1;
      }
   }

   public void contribute(TextStructure var1) {
      var1.setName(this.getName());
   }

   public void contribute(TextDocument var1) {
      var1.setName(this.getName());
   }

   public String getName() {
      return this.nameField.getValue().trim();
   }

   public boolean isValid() {
      return !this.enabled ? true : this.nameField.isFieldValid() && this.delimiterPanel.isInputValid();
   }

   private void notifyExternalListenerOfChange() {
      this.externalChangeListener.stateChanged(new ChangeEvent(this));
   }

   private JCheckBox createActivator(String var1) {
      KongaCheckBox var2 = new KongaCheckBox(var1);
      var2.addSelectionChangeListener(new Receiver<Boolean>() {
         public void handle(Boolean var1) {
            DestinationStructureDefiner.this.setEnabled(var1);
            if (var1) {
               DestinationStructureDefiner.this.moveFocusToNameField();
            }

            DestinationStructureDefiner.this.notifyExternalListenerOfChange();
         }
      });
      return var2;
   }

   private void moveFocusToNameField() {
      DelayedJob.createAndSchedule(250, new Runnable() {
         @Override
         public void run() {
            DestinationStructureDefiner.this.nameField.requestFocus();
         }
      });
   }

   private TextInputField2 createNameField(InputChangeListener var1) {
      TextInputField2 var2 = DocumentWizardUtils.getNameInputField("Document Name", this.documentOwner);
      var2.addInputChangeListener(var1);
      var2.setSendInputChangeEvents(true);
      return var2;
   }

   private DelimiterAndQualifierPanel createDelimiterPanel(InputChangeListener var1) {
      DelimiterAndQualifierPanel var2 = new DelimiterAndQualifierPanel(FieldDelimiter.COMMA, "\"", QualifierMode.WHEN_NEEDED, Optional.absent());
      var2.addInputChangeListener(var1);
      var2.setSendInputChangeEvents(true);
      return var2;
   }

   private class InputChangeListenerImpl implements InputChangeListener {
      private InputChangeListenerImpl() {
      }

      @Override
      public void inputChanged(InputChangeEvent var1) {
         DestinationStructureDefiner.this.notifyExternalListenerOfChange();
      }
   }
}
