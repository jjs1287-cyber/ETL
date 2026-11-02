package org.etlforge.application.ui.widget;

import java.awt.Component;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.annotation.Nullable;
import javax.swing.SwingUtilities;
import org.etlforge.application.ui.undo.UndoRedoControllers;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.text.KongaTextField;
import org.etlforge.ui.widget.window.WindowUtils;

public class TextFieldInput {
   private final String title;
   private final TextInputField2 field;
   private Receiver<String> callback;
   private TextFieldInputValidator validator;
   private Component locationRelativeTo;

   public TextFieldInput(String var1, String var2) {
      this.title = var1;
      this.validator = TextFieldInputValidator.ALWAYS_VALID;
      this.field = new TextInputField2("", new KongaTextField(), KongaLabel.withMnemonic(var2)) {
         @Override
         public boolean isInputValid(String var1) {
            return TextFieldInput.this.validator.validate(var1);
         }

         @Override
         public String getInvalidMessage(String var1) {
            String var2x = TextFieldInput.this.validator.getErrorMessage(var1);
            return var2x != null ? var2x : super.getInvalidMessage(var1);
         }
      };
      this.field.setAutoTrim(true);
      this.field.setSendInputChangeEvents(true);
   }

   public void setCallback(Receiver<String> var1) {
      this.callback = var1;
   }

   public void setInitialValue(String var1) {
      this.field.setValue(var1);
   }

   public void setValidator(TextFieldInputValidator var1) {
      this.validator = var1;
   }

   public void setFieldWidth(int var1) {
      ((KongaTextField)this.field.getInputComponent()).setColumns(var1);
   }

   public void setSelectAllWhenFocused(boolean var1) {
      this.field.setSelectAllWhenFocused(var1);
   }

   public void setTextWhenEmpty(String var1) {
      this.field.setTextWhenEmpty(var1);
   }

   public void setAutoTrimEnabled(boolean var1) {
      this.field.setAutoTrim(var1);
   }

   public void setLocationRelativeTo(Component var1) {
      this.locationRelativeTo = var1;
   }

   public void show() {
      TextFieldInput.Editor var1 = this.createEditor();
      this.prepareEditor(var1);
      var1.show();
   }

   private void prepareEditor(final TextFieldInput.Editor var1) {
      var1.setOkEnabled(this.field.isFieldValid());
      this.field.addInputChangeListener(new InputChangeListener() {
         @Override
         public void inputChanged(InputChangeEvent var1x) {
            var1.setOkEnabled(TextFieldInput.this.field.isFieldValid());
         }
      });
      ((KongaTextField)this.field.getInputComponent()).addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1x) {
            var1.ok();
         }
      });
   }

   @Nullable
   public String showInDialog(@Nullable Window var1) {
      TextFieldInput.DialogEditor var2 = new TextFieldInput.DialogEditor(var1);
      this.prepareEditor(var2);
      return var2.openDialog();
   }

   private String getValue() {
      return this.field.getValue();
   }

   private void notifyCallback(boolean var1) {
      if (this.callback != null) {
         if (var1) {
            this.callback.handle(this.getValue());
         } else {
            this.callback.handle(null);
         }
      }
   }

   private TextFieldInput.Editor createEditor() {
      return new TextFieldInput.DialogEditor(null);
   }

   public static Integer askForIntegerInDialog(@Nullable Window var0, String var1, String var2, Integer var3, Integer var4, Integer var5) {
      TextFieldInput var6 = new TextFieldInput(var1, var2);
      if (var5 != null) {
         var6.setInitialValue(var5.toString());
      }

      var6.setSelectAllWhenFocused(true);
      if (var3 == null && var4 == null) {
         var6.setValidator(TextFieldInputValidator.INTEGER);
      } else {
         var6.setValidator(TextFieldInputValidator.integerInRange(var3 == null ? Integer.MIN_VALUE : var3, var4 == null ? Integer.MAX_VALUE : var4));
      }

      String var7 = var6.showInDialog(var0);
      return var7 != null ? Integer.valueOf(var7) : null;
   }

   private final class DialogEditor implements TextFieldInput.Editor {
      private final KongaDialog dialog;

      public DialogEditor(@Nullable Window var2) {
         this.dialog = new KongaDialog(var2 == null ? UiUtils.getActiveWindow() : var2, TextFieldInput.this.title);
         BorderLayoutBuilder var3 = this.doLayout();
         this.dialog.standardLayout(var3, KongaDialog.OK_CANCEL);
         this.dialog.setResizable(false);
         this.dialog.setOkButtonAsDefault();
         this.dialog.enableUndoRedoForTextComponents(UndoRedoControllers.textFactory());
      }

      @Override
      public void ok() {
         if (this.dialog.getOKButton().isEnabled()) {
            this.dialog.getOKButton().doClick();
         }
      }

      @Override
      public void setOkEnabled(boolean var1) {
         this.dialog.getOKButton().setEnabled(var1);
      }

      public String openDialog() {
         this.dialog.pack();
         this.setDialogLocation();
         this.dialog.setVisible(true);
         return this.dialog.wasOkPressed() ? TextFieldInput.this.getValue() : null;
      }

      private void setDialogLocation() {
         if (TextFieldInput.this.locationRelativeTo != null) {
            Point var1 = new Point(8, 16);
            SwingUtilities.convertPointToScreen(var1, TextFieldInput.this.locationRelativeTo);
            this.dialog.setLocation(var1);
            WindowUtils.ensureWindowCompletelyVisibleOnScreen(this.dialog);
         } else {
            this.dialog.setLocationRelativeTo(null);
         }
      }

      @Override
      public void show() {
         this.openDialog();
         TextFieldInput.this.notifyCallback(this.dialog.wasOkPressed());
      }

      private BorderLayoutBuilder doLayout() {
         BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 10);
         var1.north(InputFieldFactory.getCapsule(TextFieldInput.this.field, 2));
         return var1;
      }
   }

   private interface Editor {
      void show();

      void ok();

      void setOkEnabled(boolean var1);
   }
}
