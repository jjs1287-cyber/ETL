package org.etlforge.application.ui.window.section;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.window.AbstractWindowElementResource;
import org.etlforge.application.ui.window.WindowElement;
import org.etlforge.application.ui.window.WindowElementState;
import org.etlforge.application.ui.window.WindowSectionContent;
import org.etlforge.ui.Empty;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.util.KeyUtils;

final class DetachedContentDialog {
   private final WindowSectionContent content;
   private final Action attachAction;
   private KongaDialog dialog;
   private DetachedContentDialog.DialogListener dialogListener;
   private DetachedContentDialog.FocusActivator focusActivator;

   public DetachedContentDialog(WindowSectionContent var1, ActionListener var2) {
      assert var1.getWindow() != null : "Cannot detach content that has not been added to the ApplicationWindow";
      this.content = var1;
      this.attachAction = new DetachedContentDialog.AttachAction(var2);
   }

   public WindowSectionContent getContent() {
      return this.content;
   }

   public void open() {
      if (this.dialog != null) {
         this.dialog.toFront();
      } else {
         this.content.addResource(new DetachedContentDialog.DialogCloser());
         KongaDialog var1 = this.createDialog();
         this.registerDialogListener(var1);
         this.installFocusListener(var1);
         this.dialog = var1;
         var1.setVisible(true);
      }
   }

   private KongaDialog createDialog() {
      JFrame var1 = this.content.getWindow().getFrame();
      KongaDialog var2 = new KongaDialog(var1, this.content.getTitle(), ModalityType.MODELESS);
      BorderLayoutBuilder var3 = this.createDialogLayout();
      var3.asContentPaneFor(var2);
      ActionKeyBinding.install(this.attachAction, var2.getRootPane(), 1);
      var2.pack();
      var2.setLocationRelativeTo(null);
      return var2;
   }

   private BorderLayoutBuilder createDialogLayout() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 10);
      BoxBuilder var2 = BoxBuilder.horizontal();
      var2.glue().add(this.attachAction);
      JComponent var3 = this.createContentView();
      var1.center(var3).south(var2).withBorder(Empty.border(10));
      return var1;
   }

   private JComponent createContentView() {
      JPanel var1 = new JPanel(new BorderLayout());
      this.content.decorate(var1);
      return var1;
   }

   private void registerDialogListener(KongaDialog var1) {
      this.dialogListener = new DetachedContentDialog.DialogListener();
      var1.addWindowListener(this.dialogListener);
   }

   private void installFocusListener(KongaDialog var1) {
      this.focusActivator = new DetachedContentDialog.FocusActivator();
      ApplicationUi.getFocusManager().addFocusRoot(var1.getRootPane(), this.focusActivator);
   }

   private void closeDialog() {
      if (this.dialog != null) {
         this.dialog.removeWindowListener(this.dialogListener);
         this.dialogListener = null;
         this.dialog.setVisible(false);
         this.dialog.dispose();
         this.dialog = null;
      }
   }

   public boolean close() {
      if (this.content.removeFromWindow(false)) {
         this.closeDialog();
         return true;
      } else {
         return false;
      }
   }

   private class AttachAction extends AbstractAction {
      private final ActionListener worker;

      public AttachAction(ActionListener var2) {
         super("Attach");
         this.putValue("AcceleratorKey", KeyUtils.F12);
         this.worker = var2;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         DetachedContentDialog.this.closeDialog();
         this.worker.actionPerformed(var1);
      }
   }

   private class DialogCloser extends AbstractWindowElementResource {
      private DialogCloser() {
      }

      @Override
      public void dispose(WindowElement var1) {
         DetachedContentDialog.this.closeDialog();
      }
   }

   private class DialogListener extends WindowAdapter {
      private DialogListener() {
      }

      @Override
      public void windowClosed(WindowEvent var1) {
         this.uninstallFocusListener();
         if (DetachedContentDialog.this.content.getWindow() != null) {
            DetachedContentDialog.this.attachAction.actionPerformed(null);
         }
      }

      private void uninstallFocusListener() {
         ApplicationUi.getFocusManager().removeFocusRoot(DetachedContentDialog.this.dialog.getRootPane());
         DetachedContentDialog.this.focusActivator = null;
      }
   }

   private class FocusActivator implements FocusListener {
      private FocusActivator() {
      }

      @Override
      public void focusGained(FocusEvent var1) {
         DetachedContentDialog.this.content.appendState(WindowElementState.ACTIVE);
      }

      @Override
      public void focusLost(FocusEvent var1) {
         DetachedContentDialog.this.content.revokeState(WindowElementState.ACTIVE);
      }
   }
}
