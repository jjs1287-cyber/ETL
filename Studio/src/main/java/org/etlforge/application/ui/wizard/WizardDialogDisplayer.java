package org.etlforge.application.ui.wizard;

import com.google.common.base.Preconditions;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import javax.swing.JComponent;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.dialog.LayeredKongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.wait.DefaultMessageWaitService;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.wait.WaitService;

public class WizardDialogDisplayer implements WizardDisplayer {
   private WaitLock waitLock;
   private Wizard wizard;
   private WizardPage currentPage;
   private WizardDialogDisplayer.DialogImpl dialog;
   private WizardDisplayerActionSupport actions;
   private boolean modal;
   private Dimension minimumSize;
   private Class<?> sizeManagementKey;
   private WizardSectionsUi sectionsUi = new WizardSectionsUi(new String[0]);
   private Image dialogImage;
   private WaitService waitService = WaitService.NONE;
   private String saveText;

   public WizardDialogDisplayer() {
   }

   public WizardDialogDisplayer(WaitLock var1) {
      this.waitLock = var1;
   }

   public void setModal(boolean var1) {
      this.modal = var1;
   }

   public void setWizard(Wizard var1) {
      Preconditions.checkState(this.wizard == null, "Cannot set the wizard more than once.");
      this.wizard = var1;
      this.actions = new WizardDisplayerActionSupport(var1);
      if (this.saveText != null) {
         this.actions.setSaveText(this.saveText);
      }

      this.createDialog();
      var1.addWizardListener(new WizardDialogDisplayer.WizardListenerImpl());
   }

   @Override
   public WizardPage getCurrentPage() {
      return this.currentPage;
   }

   @Override
   public void handleContextMenuRequest(Component var1, int var2, int var3) {
      this.actions.handleContextMenuRequest(var1, var2, var3);
   }

   @Override
   public void setSection(String var1) {
      this.sectionsUi.setSection(var1);
   }

   @Override
   public String getSection() {
      return this.sectionsUi.getSection();
   }

   @Override
   public void setSections(String[] var1) {
      if (this.sectionsUi == null) {
         this.sectionsUi = new WizardSectionsUi(var1);
      } else {
         this.sectionsUi.setSections(var1);
      }
   }

   public void setSaveText(String var1) {
      if (this.actions == null) {
         this.saveText = var1;
      } else {
         this.actions.setSaveText(var1);
      }
   }

   @Override
   public void showPage(WizardPage var1) {
      Preconditions.checkNotNull(var1, "page");
      if (var1 != this.currentPage) {
         this.currentPage = var1;
         this.addPageToDisplayArea(var1);
         this.updateNavigationControls();
         this.updateTitle();
         this.ensureDialogVisible();
      }
   }

   @Override
   public void updateNavigationControls() {
      this.actions.update(this.currentPage);
   }

   @Override
   public WaitService getWaitService() {
      return this.waitService;
   }

   public void setDialogImage(Image var1) {
      this.dialogImage = var1;
      if (this.dialog != null) {
         this.dialog.setIconImage(var1);
      }
   }

   public void setManageLocation(Class<?> var1) {
      if (this.dialog != null) {
         this.dialog.manageLocation(var1);
      } else {
         this.sizeManagementKey = var1;
      }
   }

   public void setMinimumSize(Dimension var1) {
      this.minimumSize = var1;
   }

   private void stopInitialWait() {
      if (this.waitLock != null) {
         this.waitLock.release();
         this.waitLock = null;
      }
   }

   private void addPageToDisplayArea(final WizardPage var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            var1.decorate(WizardDialogDisplayer.this.dialog);
            var1.requestFocus();
         }
      });
   }

   private void ensureDialogVisible() {
      if (!this.dialog.isVisible()) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               WizardDialogDisplayer.this.dialog.pack();
               WizardDialogDisplayer.this.enforceMinimumSize();
               if (WizardDialogDisplayer.this.sizeManagementKey != null) {
                  WizardDialogDisplayer.this.dialog.manageLocation(WizardDialogDisplayer.this.sizeManagementKey);
               } else {
                  WizardDialogDisplayer.this.dialog.setLocationRelativeTo(WizardDialogDisplayer.this.dialog.getOwner());
               }

               WizardDialogDisplayer.this.dialog.setVisible(true);
            }
         });
      }
   }

   private void enforceMinimumSize() {
      if (this.minimumSize != null) {
         Dimension var1 = this.dialog.getSize();
         boolean var2 = false;
         if (var1.width < this.minimumSize.width) {
            var1.width = this.minimumSize.width;
            var2 = true;
         }

         if (var1.height < this.minimumSize.height) {
            var1.height = this.minimumSize.height;
            var2 = true;
         }

         if (var2) {
            this.dialog.setSize(var1);
         }
      }
   }

   @Override
   public void updateTitle() {
      if (this.dialog != null) {
         this.dialog.setTitle(this.getDialogTitle());
      }
   }

   private void createDialog() {
      this.dialog = new WizardDialogDisplayer.DialogImpl(UiUtils.getActiveWindow(), this.getDialogTitle());
      this.addDialogButtons(this.dialog.getDefaultLayout());
      this.waitService = new DefaultMessageWaitService(this.dialog.getKongaLayeredPane(), WizardStyleSheet.MEDIUM_CAPTION_STYLE);
   }

   private void addDialogButtons(final BorderLayoutBuilder var1) {
      this.actions.setDialog(this.dialog, new UiCanvas() {
         @Override
         public void display(Component var1x) {
            if (var1x instanceof JComponent) {
               ((JComponent)var1x).setBorder(Empty.border(10, 0, 10, 0));
            }

            var1.south(var1x);
         }
      });
   }

   private String getDialogTitle() {
      String var1 = this.wizard != null ? this.wizard.getTitle() : null;
      String var2 = this.currentPage != null ? this.currentPage.getTitle() : null;
      return this.constructDialogTitle(var1, var2);
   }

   protected String constructDialogTitle(String var1, String var2) {
      StringBuilder var3 = new StringBuilder();
      if (var1 != null) {
         var3.append(var1);
      }

      if (var2 != null && var2.length() > 0) {
         if (var3.length() > 0) {
            var3.append(": ");
         }

         var3.append(var2);
      }

      return var3.toString();
   }

   private class DialogImpl extends LayeredKongaDialog implements UiCanvas {
      private JComponent page;
      private boolean done;

      public DialogImpl(Window var2, String var3) {
         super(var2, var3, WizardDialogDisplayer.this.modal ? ModalityType.APPLICATION_MODAL : ModalityType.MODELESS);
         this.setDefaultCloseOperation(0);
         this.getDefaultLayout().withBackground(Colors.ETLFORGE_BLUE);
         this.getDefaultLayout().north(WizardDialogDisplayer.this.sectionsUi);
         if (WizardDialogDisplayer.this.dialogImage != null) {
            this.setIconImage(WizardDialogDisplayer.this.dialogImage);
         }
      }

      @Override
      public void display(Component var1) {
         if (this.page != null) {
            this.getDefaultLayout().remove(this.page);
         }

         this.page = BorderLayoutBuilder.fillWith(var1);
         this.page.setOpaque(false);
         this.page.setBorder(Empty.border(0, 2, 0, 2));
         this.getDefaultLayout().center(this.page);
         this.getRootPane().revalidate();
         this.getRootPane().repaint();
      }

      public void setDone() {
         this.done = true;
      }

      @Override
      protected void onCancel() {
         WizardDialogDisplayer.this.wizard.cancelWizard();
      }

      @Override
      protected void onDialogOpened() {
         super.onDialogOpened();
         WizardDialogDisplayer.this.stopInitialWait();
      }

      @Override
      protected void onDialogClose() {
         if (this.done) {
            super.onDialogClose();
         } else {
            WizardDialogDisplayer.this.wizard.cancelWizard();
         }
      }
   }

   private class WizardListenerImpl implements WizardListener {
      private WizardListenerImpl() {
      }

      @Override
      public void wizardCancelled() {
         this.close();
      }

      @Override
      public void wizardCompleted(Object var1) {
         this.close();
      }

      @Override
      public void wizardFailed(Throwable var1) {
         this.close();
      }

      private void close() {
         WizardDialogDisplayer.this.stopInitialWait();
         WizardDialogDisplayer.this.dialog.setDone();
         WizardDialogDisplayer.this.dialog.removeFromLocationManager();
         WizardDialogDisplayer.this.dialog.dispose();
      }
   }
}
