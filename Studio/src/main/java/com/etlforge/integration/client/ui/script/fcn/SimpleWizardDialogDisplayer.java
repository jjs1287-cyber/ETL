package com.etlforge.integration.client.ui.script.fcn;

import com.google.common.base.Supplier;
import com.etlforge.integration.client.ui.script.fcn.arg.ArgumentBasedFunctionWizard;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.undo.UndoRedoControllers;
import org.etlforge.application.ui.wizard.WizardSectionsUi;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.data.script.Function;
import org.etlforge.ui.Empty;
import org.etlforge.ui.dialog.LayeredKongaDialog;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.wait.WaitIndicator;
import org.etlforge.ui.wait.WaitService;

public final class SimpleWizardDialogDisplayer {
   private final Function function;
   private LayeredKongaDialog dialog;
   private final SimpleFunctionWizardPage page;
   private final InputChangeListener validationListener;
   private boolean allowFinishOnInvalidInput;

   public SimpleWizardDialogDisplayer(Function var1, SimpleFunctionWizardPage var2) {
      this.function = var1;
      this.page = var2;
      this.dialog = this.createDialog(var1);
      this.validationListener = new InputChangeListener() {
         @Override
         public void inputChanged(InputChangeEvent var1) {
            SimpleWizardDialogDisplayer.this.updateOkEnabled();
         }
      };
      var2.addChangeListener(this.validationListener);
      this.updateOkEnabled();
   }

   private LayeredKongaDialog createDialog(Function var1) {
      LayeredKongaDialog var2 = new LayeredKongaDialog(UiUtils.getActiveWindow(), var1.getName()) {
         @Override
         protected void onDialogOpened() {
            SimpleWizardDialogDisplayer.this.page.postDisplay();
         }
      };
      var2.getDefaultLayout().withBackground(Colors.ETLFORGE_BLUE);
      this.doLayout(var2);
      var2.setMinimumSize(new Dimension(600, 600));
      if (ClientIcons.FORMULA_16 instanceof ImageIcon) {
         var2.setIconImage(((ImageIcon)ClientIcons.FORMULA_16).getImage());
      }

      var2.manageLocation(ArgumentBasedFunctionWizard.class);
      var2.setOkButtonAsDefault();
      var2.enableUndoRedoForTextComponents(UndoRedoControllers.textFactory());
      return var2;
   }

   private void doLayout(LayeredKongaDialog var1) {
      var1.getDefaultLayout().center(this.page);
      this.addTitle(var1);
      addButtons(var1);
   }

   private void addTitle(LayeredKongaDialog var1) {
      String var2 = this.function.getDisplayName();
      String var3 = StringUtils.substringBefore(var2, "(") + "(...)";
      WizardSectionsUi var4 = new WizardSectionsUi(new String[]{var3});
      var4.setSection(var3);
      var1.getDefaultLayout().north(var4);
   }

   private static void addButtons(LayeredKongaDialog var0) {
      JButton var1 = var0.createOKButton();
      var1.setText("Finish");
      JButton var2 = var0.createCancelButton();
      JComponent var3 = WizardStyleSheet.layoutButtons(var1, var2);
      var3.setBorder(Empty.border(10, 0, 10, 0));
      var0.getDefaultLayout().south(var3);
   }

   private void updateOkEnabled() {
      boolean var1 = this.allowFinishOnInvalidInput || this.page.isInputValid();
      this.dialog.getOKButton().setEnabled(var1);
   }

   public void setAllowFinishOnInvalidInput(boolean var1) {
      this.allowFinishOnInvalidInput = var1;
      this.updateOkEnabled();
   }

   public boolean show() {
      this.dialog.setVisible(true);
      boolean var1 = this.dialog.wasOkPressed();
      this.dialog = null;
      this.page.removeChangeListener(this.validationListener);
      return var1;
   }

   public WaitService getWaitService() {
      return this.dialog;
   }

   public void setWaitIndicator(final WaitIndicator var1) {
      this.dialog.setWaitIndicatorFactory(new Supplier<WaitIndicator>() {
         public WaitIndicator get() {
            return var1;
         }
      });
   }
}
