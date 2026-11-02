package org.etlforge.integration.client.ui.jitterpack;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.awt.EventQueue;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.etlforge.application.ui.action.ActionInvocationListener;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.help.WindowHelpButton;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.jitterpack.Exporter;
import org.etlforge.system.callback.CallbackResult;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionUtils;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.util.ButtonOrder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.wait.WaitLock;

public final class ExportJitterPackDialog {
   public static void show(ApplicationWindow var0, ExportJitterPackUi var1, Exporter var2) {
      Preconditions.checkNotNull(var0, "window");
      Preconditions.checkNotNull(var1, "ui");
      ExportJitterPackDialog.DialogImpl var3 = new ExportJitterPackDialog.DialogImpl(var0.getFrame(), var0, var1, var2);
      var3.setVisible(true);
   }

   private ExportJitterPackDialog() {
   }

   private static class DialogImpl extends WizardStyleDialog {
      private final ApplicationWindow window;
      private final ExportJitterPackUi ui;
      private final Action exportAction;
      private final JButton helpButton;
      private WaitLock waitLock;

      public DialogImpl(JFrame var1, ApplicationWindow var2, ExportJitterPackUi var3, Exporter var4) {
         super(var1, Strings.getJitterPackString("Export.Title"), (JComponent)null, null, true);
         this.window = var2;
         this.ui = var3;
         this.helpButton = new WindowHelpButton(this, ExportJitterPackUi.HELP_ID, true).getButton();
         this.exportAction = this.createExportAction(var4);
         this.createComponents();
         this.pack();
         this.manageLocation();
      }

      private Action createExportAction(Exporter var1) {
         ActionInvocationListener var2 = new ActionInvocationListener() {
            @Override
            public void actionInvoked(ApplicationAction var1) {
               DialogImpl.this.waitLock = DialogImpl.this.startWait();
               UiUtils.setEnabledRecursively(DialogImpl.this.getContentPane(), false);
            }
         };
         return new DoExportAction(this.window, this.ui, var1, var2, new ExportJitterPackDialog.DialogImpl.ActionCallback());
      }

      private void createComponents() {
         this.createContentPane();
         this.setupUi();
      }

      private void createContentPane() {
         JComponent var1 = this.createExportUiPanel();
         UiProvider var2 = this.createButtonPanel();
         this.getDefaultLayout().center(var1).south(var2);
      }

      private JComponent createExportUiPanel() {
         JPanel var1 = BorderLayoutBuilder.fillWith(this.ui);
         var1.setOpaque(true);
         var1.setBorder(Empty.border(10));
         UiUtils.setPanelBackgrounds(var1, Colors.NEUTRAL_BACKGROUND);
         return var1;
      }

      private UiProvider createButtonPanel() {
         this.setFixedButtonSize(false);
         JButton var1 = this.createOKButton();
         JButton var2 = this.createCancelButton();
         ImmutableList var3 = ButtonOrder.okAndCancelOrder(var1, var2);
         return Horizontal.box().withEmptyBorder(10).nonOpaque().add(this.helpButton).glue().addAllWithSpace(10, var3);
      }

      private void setupUi() {
         this.ui.setDoExportAction(this.exportAction);
         this.ui.setSendUndoEvents(false);
         this.ui.setSendInputChangeEvents(true);
         this.ui.addInputChangeListener(new InputChangeListener() {
            @Override
            public void inputChanged(InputChangeEvent var1) {
               DialogImpl.this.updateExportActionEnabledState();
            }
         });
         this.updateExportActionEnabledState();
      }

      private void updateExportActionEnabledState() {
         boolean var1 = this.ui.isInputValid();
         this.exportAction.setEnabled(var1);
         this.getOKButton().setEnabled(var1);
      }

      @Override
      public JButton createOKButton() {
         JButton var1 = super.createOKButton();
         var1.setText("Export");
         return var1;
      }

      @Override
      protected void onOk() {
         if (this.exportAction.isEnabled()) {
            ActionUtils.run(this.exportAction, this, "export");
         }
      }

      @Override
      protected void onDialogOpened() {
         super.onDialogOpened();
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               DialogImpl.this.setOkButtonAsDefault();
            }
         });
      }

      private class ActionCallback implements CallbackResult {
         private ActionCallback() {
         }

         @Override
         public void cancelled(Object var1) {
            this.reactivateUi();
         }

         @Override
         public void failed(Object var1) {
            this.reactivateUi();
         }

         @Override
         public void succeeded(Object var1) {
            ExportJitterPackDialog.DialogImpl.super.onOk();
         }

         private void reactivateUi() {
            this.stopWait();
            UiUtils.setEnabledRecursively(DialogImpl.this.getContentPane(), true);
         }

         private void stopWait() {
            if (DialogImpl.this.waitLock != null) {
               DialogImpl.this.waitLock.release();
            }
         }
      }
   }
}

