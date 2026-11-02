package org.etlforge.integration.client.connect.wizard;

import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.integration.client.ui.entity.wizard.WizardNameInputField;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.widget.DelayedDocumentContentChangeReaction;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public final class NameChangeUi<T extends IntegrationEntity> implements UiProvider {
   private final WizardNameInputField field;
   private final Receiver<String> callback;

   public NameChangeUi(T var1, Folder var2, Receiver<String> var3) {
      MnemonicString var4 = ApplicationResources.MNEMONIC_FACTORY.fromString("This is the &name given to this wizard by ETLForge. You can rename it here.");
      this.field = new WizardNameInputField(var4, var1, var2);
      this.field.setSendInputChangeEvents(true);
      NameChangeUi.ChangeNotifier var5 = new NameChangeUi.ChangeNotifier();
      var5.install(this.field.getInputComponent());
      this.callback = var3;
   }

   public void setParent(Folder var1) {
      this.field.setParent(var1);
   }

   public String getName() {
      return this.field.getValue();
   }

   public void setName(String var1) {
      this.field.setValue(var1);
   }

   public boolean isValid() {
      return this.field.isFieldValid();
   }

   public void addValidationListener(InputChangeListener var1) {
      this.field.addInputChangeListener(var1);
   }

   public void removeValidationListener(InputChangeListener var1) {
      this.field.removeInputChangeListener(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      CaptionLayout.of(this.field.getLabel(), InvisiblePanel.of(this.field.getInputComponent())).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.field.requestFocus();
   }

   private class ChangeNotifier extends DelayedDocumentContentChangeReaction {
      private ChangeNotifier() {
      }

      @Override
      public void run() {
         if (NameChangeUi.this.field.isFieldValid()) {
            String var1 = NameChangeUi.this.field.getValue().trim();
            NameChangeUi.this.callback.handle(var1);
         }
      }
   }
}
