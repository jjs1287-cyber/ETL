package org.etlforge.ui.selection;

import com.google.common.base.Preconditions;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import org.etlforge.ui.dialog.KongaDialog;

public class DialogSelectionSupport {
   private final SelectionSource source;
   private final JButton button;
   private final SelectionBasedEnabler impl;
   private final SelectionAction defaultAction;

   public DialogSelectionSupport(SelectionSource var1, KongaDialog var2) {
      this.source = var1;
      this.button = getButtonToBind(var2);
      this.impl = new SelectionBasedEnabler(var1, this.button) {
         @Override
         protected boolean isEnabledFor(Selection var1) {
            return DialogSelectionSupport.this.isEnabledFor(var1);
         }
      };
      if (var1 instanceof SelectionSourceWithDefaultAction) {
         this.defaultAction = new DialogSelectionSupport.DefaultAction();
         ((SelectionSourceWithDefaultAction)var1).setDefaultAction(this.defaultAction);
      } else {
         this.defaultAction = null;
      }
   }

   private static JButton getButtonToBind(KongaDialog var0) {
      JButton var1 = var0.getOKButton();
      if (var1 == null) {
         var1 = var0.getContinueButton();
         if (var1 == null) {
            var1 = var0.getApplyButton();
         }
      }

      return Preconditions.checkNotNull(var1, "This class only works for dialogs with OK, Continue, or Apply buttons");
   }

   protected boolean isEnabledFor(Selection var1) {
      return !var1.isEmpty();
   }

   public final void uninstall() {
      this.impl.uninstall();
      if (this.defaultAction != null) {
         ((SelectionSourceWithDefaultAction)this.source).setDefaultAction(null);
      }
   }

   private final class DefaultAction extends AbstractSelectionAction {
      private DefaultAction() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (DialogSelectionSupport.this.button.isEnabled()) {
            DialogSelectionSupport.this.button.doClick();
         }
      }
   }
}
