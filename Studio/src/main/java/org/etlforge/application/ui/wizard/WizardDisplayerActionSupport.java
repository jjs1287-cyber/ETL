package org.etlforge.application.ui.wizard;

import com.google.common.base.Preconditions;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.widget.DefaultComponentAction;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

final class WizardDisplayerActionSupport implements Disposable {
   private final Wizard wizard;
   private final Action goBack;
   private final Action goNext;
   private final JButton goNextButton;
   private final Action save;
   private final JButton saveButton;
   private final Action cancel;
   private NavigationButtons toolbar;
   private KongaDialog dialog;

   public WizardDisplayerActionSupport(Wizard var1) {
      this.wizard = var1;
      this.goBack = WizardActions.getPreviousPageAction(var1);
      this.goNext = WizardActions.getNextPageAction(var1);
      this.goNextButton = new KongaButton(this.goNext);
      this.save = WizardActions.getSaveAction(var1);
      this.saveButton = new KongaButton(this.save);
      this.cancel = WizardActions.getCancelAction(var1);
      this.cancel.setEnabled(true);
   }

   public void setDialog(KongaDialog var1, UiCanvas var2) {
      Preconditions.checkState(this.dialog == null, "A dialog has already been set");
      this.dialog = var1;
      this.layoutInDialog(var1, var2);
   }

   private void layoutInDialog(KongaDialog var1, UiCanvas var2) {
      List var3 = this.createButtonsForDialog();
      Grid var4 = Horizontal.grid().of(var3).nonOpaque().withHGap(10);
      Horizontal.box().glue().add(var4).glue().nonOpaque().decorate(var2);
   }

   private List<JButton> createButtonsForDialog() {
      ArrayList var1 = new ArrayList();
      var1.add(new KongaButton(this.goBack));
      var1.add(this.goNextButton);
      var1.add(this.saveButton);
      var1.add(new KongaButton(this.cancel));
      return var1;
   }

   @Override
   public void dispose() {
   }

   public void update(final WizardPage var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            WizardDisplayerActionSupport.this.updateImpl(var1);
         }
      });
   }

   private void updateImpl(final WizardPage var1) {
      if (var1 != null) {
         this.goBack.setEnabled(this.wizard.hasPreviousPage(var1));
         this.goNext.setEnabled(this.canGoNext(var1));
         this.save.setEnabled(this.wizard.canFinish());
      } else {
         this.goBack.setEnabled(false);
         this.goNext.setEnabled(false);
         this.save.setEnabled(false);
      }

      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            WizardDisplayerActionSupport.this.updateDefaultButton(var1);
         }
      });
   }

   private boolean canGoNext(WizardPage var1) {
      return var1.canContinue() && this.wizard.hasNextPage(var1);
   }

   public void handleContextMenuRequest(Component var1, int var2, int var3) {
      KongaPopupMenu var4 = new KongaPopupMenu();
      Action[] var5 = new Action[]{this.goNext, this.goBack, this.save, null, this.cancel};
      var4.addActions(var5);
      var4.show(var1, var2, var3);
   }

   public NavigationButtons getToolBar() {
      if (this.toolbar == null) {
         this.toolbar = new NavigationButtons(new KongaButton(this.goBack), this.goNextButton, this.saveButton, new KongaButton(this.cancel));
      }

      return this.toolbar;
   }

   private void updateDefaultButton(WizardPage var1) {
      JButton var2;
      if (this.save.isEnabled() && !this.goNext.isEnabled()) {
         var2 = this.saveButton;
      } else {
         var2 = this.goNextButton;
      }

      if (this.dialog != null) {
         this.dialog.getRootPane().setDefaultButton(var2);
      }

      if (this.toolbar != null) {
         this.toolbar.setDefaultButton(var2);
         this.toolbar.applyDefaultButtonToRootContainer();
      }
   }

   public void installDefaultAction(JComponent var1) {
      DefaultComponentAction.whenInFocusedWindow(var1, new WizardDisplayerActionSupport.DefaultAction());
   }

   public void setSaveText(String var1) {
      this.save.putValue("Name", var1);
   }

   private class DefaultAction extends AbstractAction {
      private DefaultAction() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (WizardDisplayerActionSupport.this.save.isEnabled()) {
            WizardDisplayerActionSupport.this.save.actionPerformed(var1);
         } else if (WizardDisplayerActionSupport.this.goNext.isEnabled()) {
            WizardDisplayerActionSupport.this.goNext.actionPerformed(var1);
         }
      }
   }
}
