package org.etlforge.application.ui.wizard;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Stack;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.undo.UndoRedoInstallerFactory;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.undo.UndoRedoInstaller;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.wait.MessageWaitService;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.widget.KongaScrollPane;
import org.etlforge.ui.widget.overlay.KongaLayeredPane;
import org.etlforge.ui.widget.overlay.OverlayContainer;

public abstract class AbstractWizardPage implements WizardPage {
   private final Wizard wizard;
   private boolean displayed;
   private boolean canContinue = true;
   private final UndoRedoInstaller undoRedoInstaller;
   private final Stack<WaitLock> waitLocks;
   private String title;
   private String subTitle;
   private String helpId;
   private final KongaLayeredPane overlayContainer = new KongaLayeredPane();
   private JPanel mainPanel = new JPanel();

   public AbstractWizardPage(Wizard var1, String var2) {
      this.wizard = Preconditions.checkNotNull(var1, "wizard");
      this.title = var2;
      this.undoRedoInstaller = UndoRedoInstallerFactory.newInstaller();
      this.waitLocks = new Stack<>();
      this.setHelpId("welcome");
      this.setCanContinue(false);
   }

   @Override
   public Wizard getWizard() {
      return this.wizard;
   }

   @Override
   public final String getTitle() {
      return this.title;
   }

   public final void setTitle(String var1) {
      this.title = var1;
   }

   public final String getSubTitle() {
      return this.subTitle;
   }

   public final void setSubTitle(String var1) {
      this.subTitle = var1;
   }

   public final void setHelpId(String var1) {
      this.helpId = var1;
   }

   @Override
   public final String getHelpId() {
      return this.helpId;
   }

   @Override
   public final void addToWizard() {
      Preconditions.checkState(!this.displayed, "This page has already been added to the wizard");
      this.displayed = true;
      this.onFirstDisplay();
   }

   protected void onFirstDisplay() {
   }

   @Override
   public final void removeFromWizard() {
      this.undoRedoInstaller.dispose();
      this.dispose();
   }

   protected void dispose() {
   }

   @Override
   public boolean prepareForForwardNavigation() {
      return true;
   }

   @Override
   public boolean prepareForBackwardNavigation() {
      return true;
   }

   public void setCanContinue(boolean var1) {
      this.canContinue = var1;
      WizardDisplayer var2 = this.getWizard().getDisplayer();
      if (var2 != null) {
         var2.updateNavigationControls();
      }
   }

   @Override
   public boolean canContinue() {
      return this.canContinue;
   }

   public final UndoRedoInstaller getUndoRedoInstaller() {
      return this.undoRedoInstaller;
   }

   public final WaitService getWaitService() {
      return this.getWizard().getDisplayer().getWaitService();
   }

   public final void startWait() {
      WaitService var1 = this.getWaitService();
      if (var1 != null) {
         synchronized (this.waitLocks) {
            this.waitLocks.push(var1.startWait());
         }
      }
   }

   public final void startWait(String var1) {
      WaitService var2 = this.getWaitService();
      if (var2 != null) {
         synchronized (this.waitLocks) {
            WaitLock var4 = var2 instanceof MessageWaitService ? ((MessageWaitService)var2).startWait(var1) : var2.startWait();
            this.waitLocks.push(var4);
         }
      }
   }

   public final void stopWait() {
      WaitLock var1 = null;
      synchronized (this.waitLocks) {
         if (this.waitLocks.isEmpty()) {
            return;
         }

         var1 = this.waitLocks.pop();
      }

      var1.release();
   }

   protected String getForwardNavigationMessage() {
      return null;
   }

   @Override
   public void requestFocus() {
   }

   @Override
   public final void decorate(UiCanvas var1) {
      if (this.overlayContainer.getBaseComponent() == null) {
         this.installPageLayout();
         this.installNavigationShortcuts();
      }

      var1.display(this.overlayContainer);
   }

   private void installPageLayout() {
      this.mainPanel = this.layoutPage();
      this.prepMainPanel(this.mainPanel);
      JPanel var1 = BorderLayoutBuilder.fillWith(this.mainPanel);
      StyleSheet.GenericEditorCanvas.makeOver(var1);
      KongaScrollPane var2 = new KongaScrollPane(var1);
      var2.setBorder(Empty.border());
      this.overlayContainer.setBaseComponent(var2);
      this.overlayContainer.setBaseInsets(new Insets(20, 5, 5, 5));
   }

   protected abstract JPanel layoutPage();

   private void prepMainPanel(JPanel var1) {
      var1.setOpaque(true);
      var1.setBackground(StyleSheet.GenericEditorPanelClass.getBackground());
      var1.setBorder(StyleSheet.GenericEditorPanelClass.getBorder());
   }

   private void installNavigationShortcuts() {
      AbstractWizardPage.NextPageAction var1 = new AbstractWizardPage.NextPageAction();
      AbstractWizardPage.PreviousPageAction var2 = new AbstractWizardPage.PreviousPageAction();
      AbstractWizardPage.FinishAction var3 = new AbstractWizardPage.FinishAction();
      AbstractWizardPage.CancelAction var4 = new AbstractWizardPage.CancelAction();

      for (Action var6 : Arrays.asList(var1, var2, var3, var4)) {
         ActionKeyBinding.install(var6, this.overlayContainer, 1);
      }
   }

   public OverlayContainer getOverlayContainer() {
      return this.overlayContainer;
   }

   public final JPanel getMainPanel() {
      return this.mainPanel;
   }

   private class CancelAction extends AbstractAction {
      public CancelAction() {
         this.putValue("AcceleratorKey", KeyUtils.getCtrlDown(87));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         AbstractWizardPage.this.getWizard().cancelWizard();
      }
   }

   private class FinishAction extends AbstractAction {
      public FinishAction() {
         this.putValue("AcceleratorKey", KeyUtils.getCtrlShiftDown(39));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (AbstractWizardPage.this.getWizard().canFinish()) {
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  AbstractWizardPage.this.getWizard().saveRequested();
               }
            });
         }
      }
   }

   private class NextPageAction extends AbstractAction {
      public NextPageAction() {
         this.putValue("AcceleratorKey", KeyUtils.getCtrlDown(39));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (AbstractWizardPage.this.canContinue()) {
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  NextPageAction.this.moveNext();
               }
            });
         }
      }

      private void moveNext() {
         Wizard var1 = AbstractWizardPage.this.getWizard();
         if (var1.hasNextPage(AbstractWizardPage.this)) {
            var1.nextPageRequested();
         } else if (var1.canFinish()) {
            var1.saveRequested();
         }
      }
   }

   private class PreviousPageAction extends AbstractAction {
      public PreviousPageAction() {
         this.putValue("AcceleratorKey", KeyUtils.getCtrlDown(37));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (AbstractWizardPage.this.getWizard().hasPreviousPage(AbstractWizardPage.this)) {
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  AbstractWizardPage.this.getWizard().previousPageRequested();
               }
            });
         }
      }
   }
}
