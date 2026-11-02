package org.etlforge.integration.client.ui.interchange.location.panel;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Set;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.integration.data.location.FileLocation;
import org.etlforge.integration.data.location.file.RenamedFileName;
import org.etlforge.integration.data.validation.ValidationIssue;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.OneColumnPanel;

final class AfterProcessingPanel extends InputPanel {
   private final CheckBoxInputField deleteSourceFileOption = this.createDeleteSourceFileOption();
   private final TargetFileNameInputField renameFileToField;

   public AfterProcessingPanel(InputPanel var1, int var2) {
      this.renameFileToField = this.createRenameFileField(var2);
      var1.addInputPanel(this);
      this.doLayout();
   }

   public void reset(FileLocation var1) {
      if (var1.willBeDeletedOnSuccess()) {
         this.deleteSourceFileOption.setSelected(true);
      } else {
         this.deleteSourceFileOption.setSelected(false);
         this.renameFileToField.setValue(var1.getRenameFileTo());
      }
   }

   public void apply(FileLocation var1) {
      var1.setRenameFileTo(this.getRenameFileTo());
   }

   private void doLayout() {
      OneColumnPanel var1 = new OneColumnPanel();
      var1.withInsets(0, 0, 10, 0);
      var1.noFill(InputFieldFactory.getCapsule(this.deleteSourceFileOption, 0));
      var1.fillOut(InputFieldFactory.getCapsule(this.renameFileToField, 0));
      var1.withBorder(StyleSheet.getDefaultTitleBorder(Strings.get("File.AfterProcessing")));
      this.wrap(var1);
   }

   private RenamedFileName getRenameFileTo() {
      String var1 = this.deleteSourceFileOption.isSelected() ? "/\\delete/\\" : this.renameFileToField.getValue().trim();
      return new RenamedFileName(var1);
   }

   private CheckBoxInputField createDeleteSourceFileOption() {
      String var1 = Strings.get("File.DeleteSource.Name");
      String var2 = Strings.get("File.DeleteSource.Label");
      CheckBoxInputField var3 = InputFieldFactory.getCheckBoxInputField(var1, var2);
      var3.getInputComponent().addItemListener(new AfterProcessingPanel.DeleteSourceOptionListener());
      this.addInputField(var3);
      return var3;
   }

   private TargetFileNameInputField createRenameFileField(int var1) {
      String var2 = Strings.get("File.RenameFile.Name");
      String var3 = Strings.get("File.RenameFile.Label");
      AfterProcessingPanel.RenameFileToField var4 = new AfterProcessingPanel.RenameFileToField(var2, var3, var1);
      this.addInputField(var4, var4, var4);
      return var4;
   }

   private class DeleteSourceOptionListener implements ItemListener {
      private DeleteSourceOptionListener() {
      }

      @Override
      public void itemStateChanged(ItemEvent var1) {
         boolean var2 = !AfterProcessingPanel.this.deleteSourceFileOption.isSelected();
         AfterProcessingPanel.this.renameFileToField.setEnabled(var2);
         AfterProcessingPanel.this.renameFileToField.setEditable(var2);
         AfterProcessingPanel.this.renameFileToField.setActive(var2);
      }
   }

   private static class RenameFileToField extends TargetFileNameInputField {
      public RenameFileToField(String var1, String var2, int var3) {
         super(var1, var2, var3);
         this.setRequired(false);
         this.setTextWhenEmpty(Strings.get("File.RenameFile.Example"));
      }

      @Override
      protected Set<ValidationIssue> getValidationIssues(String var1) {
         return new RenamedFileName(var1).validate();
      }
   }
}
