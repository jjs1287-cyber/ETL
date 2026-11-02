package org.etlforge.integration.client.ui.entity.move;

import javax.swing.JComponent;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.integration.client.ui.entity.IntegrationEntityNameInputField;
import org.etlforge.integration.client.ui.entity.resources.EntityResources;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.DeleteRequestHandler;
import org.etlforge.system.Disposable;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.util.option.InputOption;
import org.etlforge.ui.util.option.InputOptionPanel;
import org.etlforge.ui.util.option.NoUiInputOption;
import org.etlforge.ui.util.option.Option;
import org.etlforge.ui.util.option.OptionSelector;
import org.etlforge.ui.util.option.SelectedOptionListener;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.util.name.IllegalNameException;
import org.etlforge.util.name.NameValidator;
import org.etlforge.util.name.NameValidatorStore;

public final class ConflictResolverSelector implements UiProvider, Disposable {
   private final ConflictResolverSelector.RenameResolverWrapper renameOption;
   private final InputOptionPanel<ConflictResolver> optionPanel;
   private boolean needsNameValidation;
   private Receiver<Boolean> validStateMonitor;

   public ConflictResolverSelector(IntegrationEntity var1, IntegrationEntity var2, DeleteRequestHandler var3) {
      this.renameOption = new ConflictResolverSelector.RenameResolverWrapper(var1, var2);
      NoUiInputOption var4 = new NoUiInputOption<>(new ReplaceExistingEntityResolver(var3), EntityResources.getString("Move.Choice.Replace"));
      NoUiInputOption var5 = new NoUiInputOption<>(ConflictResolver.DO_NOT_MOVE, EntityResources.getString("Move.Choice.DoNotMove"));
      this.optionPanel = new InputOptionPanel<>(this.renameOption, var4, var5);
      this.optionPanel.setSelectedOption(this.renameOption);
      this.needsNameValidation = true;
      this.validStateMonitor = NoReceiver.create();
      ConflictResolverSelector.ValidStateListener var6 = new ConflictResolverSelector.ValidStateListener();
      this.optionPanel.addSelectionListener(var6);
      this.renameOption.nameField.addInputChangeListener(var6);
   }

   @Override
   public void dispose() {
      this.optionPanel.dispose();
   }

   public ConflictResolver getResolver() {
      return this.isInputValid() ? this.optionPanel.getValue() : ConflictResolver.DO_NOT_MOVE;
   }

   public boolean isInputValid() {
      return this.needsNameValidation ? this.renameOption.isInputValid() : true;
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.optionPanel.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.optionPanel.requestFocus();
   }

   public void setValidStateMonitor(Receiver<Boolean> var1) {
      this.validStateMonitor = var1;
      this.notifyMonitor();
   }

   private void notifyMonitor() {
      this.validStateMonitor.handle(this.isInputValid());
   }

   private class RenameResolverWrapper implements InputOption<ConflictResolver> {
      private final IntegrationEntity itemToMove;
      private final IntegrationEntity newOwner;
      private final IntegrationEntityNameInputField nameField;
      private final ConflictResolver resolver;

      public RenameResolverWrapper(IntegrationEntity var2, IntegrationEntity var3) {
         this.itemToMove = var2;
         this.newOwner = var3;
         this.nameField = this.createNewNameField();
         this.resolver = new RenameMovedEntityResolver(this.nameField.asStringSupplier());
      }

      public boolean isInputValid() {
         return this.nameField.isFieldValid();
      }

      public ConflictResolver getValue() {
         return this.resolver;
      }

      @Override
      public boolean isActive() {
         return this.nameField.isActive();
      }

      @Override
      public void setActive(boolean var1) {
         this.nameField.setActive(var1);
         this.nameField.setEnabled(var1);
         ConflictResolverSelector.this.needsNameValidation = var1;
      }

      @Override
      public void decorate(UiCanvas var1) {
         JComponent var2 = InputFieldFactory.getCapsule(this.nameField);
         var2.setBorder(Empty.border(0, 25, 15, 0));
         var1.display(var2);
      }

      @Override
      public void requestFocus() {
         this.nameField.requestFocus();
      }

      @Override
      public void decorate(OptionSelector var1) {
         var1.setText(EntityResources.getString("Move.Choice.Rename"));
      }

      private IntegrationEntityNameInputField createNewNameField() {
         KongaLabel var1 = TextStyles.CaptionText.makeLabel(EntityResources.getString("Move.NewName") + ":");
         IntegrationEntityNameInputField var2 = new IntegrationEntityNameInputField(
            this.itemToMove, EntityResources.getString("Move.NewName"), InputFieldFactory.getTextField(30, true), var1
         );
         var2.setSelectAllWhenFocused(true);
         var2.setValue(this.getNewNameSuggestion());
         var2.setNameValidator(new ConflictResolverSelector.RenameResolverWrapper.NameValidatorImpl());
         var2.setSendInputChangeEvents(true);
         return var2;
      }

      private String getNewNameSuggestion() {
         return EntityUtils.getAvailableName(this.itemToMove, this.newOwner, this.itemToMove.getName());
      }

      private class NameValidatorImpl implements NameValidator {
         private NameValidatorImpl() {
         }

         @Override
         public void validateName(String var1) throws IllegalNameException {
            NameValidator var2 = NameValidatorStore.getInstance().get(RenameResolverWrapper.this.itemToMove);
            if (var2 != null) {
               var2.validateName(var1);
            }

            if (!EntityUtils.isChildNameUnique(RenameResolverWrapper.this.newOwner, var1)) {
               String var3 = EntityResources.format("DuplicateName.Message", var1);
               throw new IllegalNameException(var3);
            }
         }
      }
   }

   private class ValidStateListener implements InputChangeListener, SelectedOptionListener {
      private ValidStateListener() {
      }

      @Override
      public void inputChanged(InputChangeEvent var1) {
         ConflictResolverSelector.this.notifyMonitor();
      }

      @Override
      public void selectionChanged(Option[] var1) {
         ConflictResolverSelector.this.notifyMonitor();
      }
   }
}
