package org.etlforge.application.ui.update;

import com.google.common.base.Optional;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.application.ui.window.AbstractWindowSectionContent;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.ui.window.action.ViewPreferencesAction;
import org.etlforge.application.update.BranchId;
import org.etlforge.application.update.CheckForUpdatesCallback;
import org.etlforge.application.update.CheckForUpdatesRequest;
import org.etlforge.application.update.DownloadNewVersionCallback;
import org.etlforge.application.update.LatestVersion;
import org.etlforge.application.update.UpdateService;
import org.etlforge.application.update.UpdateServiceException;
import org.etlforge.system.thread.Cancelable;
import org.etlforge.system.thread.Timeout;
import org.etlforge.ui.Empty;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.wait.DefaultMessageWaitService;
import org.etlforge.ui.wait.MessageWaitService;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.widget.KongaScrollPane;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.overlay.KongaLayeredPane;

public final class CheckForUpdatesView extends AbstractWindowSectionContent {
   public static final String ID = CheckForUpdatesView.class.getName();
   private final UpdateService updateService;
   private final BranchId branchId;
   private final KongaLayeredPane layeredPane = new KongaLayeredPane();
   private final BorderLayoutBuilder layout = new BorderLayoutBuilder().withBackground(Colors.NEUTRAL_BACKGROUND);
   private final MessageWaitService waitService = new DefaultMessageWaitService(this.layeredPane, WizardStyleSheet.MEDIUM_CAPTION_STYLE);
   private CheckForUpdatesView.State state = CheckForUpdatesView.State.IDLE;
   private CheckForUpdatesRequest checkForUpdatesRequest;
   private Cancelable currentDownloadJob;
   private ViewPreferencesAction preferencesAction;

   public CheckForUpdatesView(WindowSection var1, UpdateService var2, BranchId var3) {
      super(ID, var1);
      this.updateService = var2;
      this.branchId = var3;
      KongaScrollPane var4 = new KongaScrollPane(this.layout.container());
      var4.makeInconspicuous();
      this.layeredPane.setBaseComponent(var4);
   }

   @Override
   public void addToWindow(ApplicationWindow var1) {
      super.addToWindow(var1);
      this.preferencesAction = new ViewPreferencesAction(var1);
      this.preferencesAction.setInitialPath(new String[]{"Check for Updates"});
      if (this.state == CheckForUpdatesView.State.IDLE) {
         this.checkForUpdates();
      }
   }

   @Override
   public boolean removeFromWindow(boolean var1) {
      if (!var1) {
         this.cancelCheckForUpdatesRequest();
         boolean var2 = this.askUserAboutCurrentDownloadWhenClosing();
         if (!var2) {
            return false;
         }
      }

      return super.removeFromWindow(var1);
   }

   private void cancelCheckForUpdatesRequest() {
      this.cancelCurrentCheckForUpdate();
   }

   private void cancelCurrentCheckForUpdate() {
      if (this.checkForUpdatesRequest != null) {
         this.checkForUpdatesRequest.cancel();
         this.checkForUpdatesRequest = null;
      }
   }

   private boolean askUserAboutCurrentDownloadWhenClosing() {
      if (this.currentDownloadJob == null) {
         return true;
      }

      AbortDownloadPrompt var1 = new AbortDownloadPrompt();
      Optional var2 = Optional.of(this.layout.container());
      int var3 = var1.ask(var2);
      switch (var3) {
         case 0:
            if (this.currentDownloadJob != null) {
               this.currentDownloadJob.cancel();
            }

            return true;
         case 1:
            return true;
         case 2:
            return false;
         default:
            throw new RuntimeException("Unexpected choice: " + var3);
      }
   }

   public void checkForUpdates() {
      if (this.state == CheckForUpdatesView.State.IDLE) {
         this.state = CheckForUpdatesView.State.CHECKING;
         this.changeView(new JLabel(""));
         WaitLock var1 = this.waitService.startWait("Checking for updates");
         CheckForUpdatesView.CheckForUpdatesCallbackImpl var2 = new CheckForUpdatesView.CheckForUpdatesCallbackImpl(var1);
         Timeout var3 = Timeout.seconds(30L);
         this.checkForUpdatesRequest = this.updateService.checkForUpdates(this.branchId, var2, var3);
      }
   }

   public void showLatestVersion(LatestVersion var1) {
      if (this.state != CheckForUpdatesView.State.DOWNLOADING) {
         this.cancelCheckForUpdatesRequest();
         CheckForUpdatesView.StartDownloadAction var2 = new CheckForUpdatesView.StartDownloadAction(var1);
         NewVersionUi var3 = new NewVersionUi(var1, this.layeredPane, var2);
         this.changeView(var3.layout());
      }
   }

   public void startDownload(LatestVersion var1) {
      if (this.state != CheckForUpdatesView.State.DOWNLOADING) {
         this.cancelCurrentCheckForUpdate();
         if (!var1.hasDownloadUrl()) {
            this.displayErrorMessage(new Exception("The download URL is missing. Please contact Support."));
         } else {
            this.state = CheckForUpdatesView.State.DOWNLOADING;
            File var2 = DownloadLocationFactory.getDestination(var1);
            DownloadProgessUi var3 = new DownloadProgessUi(var1.getVersionNumber(), var2);
            CheckForUpdatesView.DownloadCallback var4 = new CheckForUpdatesView.DownloadCallback(var2, var3);
            this.currentDownloadJob = this.updateService.downloadNewVersion(var1, var2, var4);
            CancelDownloadAction var5 = new CancelDownloadAction(this.currentDownloadJob);
            var3.setCancelAction(var5);
            this.changeView(var3.layout());
         }
      }
   }

   @Override
   public String getTitle() {
      return "Check for Updates";
   }

   @Override
   public void decorate(JComponent var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder();
      var2.north(this.getTitlePanel()).center(this.layeredPane);
      UiUtils.setPanelBackgrounds(var2.container(), Colors.NEUTRAL_BACKGROUND);
      BorderLayoutBuilder.from(var1).center(var2.container());
   }

   @Override
   protected void populateToolBar(JToolBar var1) {
      JButton var2 = var1.add(this.preferencesAction);
      var2.setText("Preferences...");
      var1.addSeparator();
      super.populateToolBar(var1);
   }

   private void displayMessage(final String var1, final Icon var2) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            JLabel var1x = CheckForUpdatesView.this.createMessageLabel(var1, var2);
            CheckForUpdatesView.this.changeView(var1x);
         }
      });
   }

   private JLabel createMessageLabel(String var1, Icon var2) {
      KongaLabel var3 = TextStyles.AdminHint.makeLabel(var1);
      var3.setIcon(var2);
      var3.setBorder(Empty.border(8));
      return var3;
   }

   private void changeView(JComponent var1) {
      this.layeredPane.removeAllOverlays();
      this.layout.removeAll();
      this.layout.north(var1);
      this.layout.refreshLayout();
   }

   private void displayErrorMessage(final Exception var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            ErrorUi var1x = new ErrorUi(var1, CheckForUpdatesView.this.new CheckAgainAction());
            CheckForUpdatesView.this.changeView(var1x.layout());
         }
      });
   }

   private class CheckAgainAction extends AbstractAction {
      public CheckAgainAction() {
         super("Check Again");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               CheckForUpdatesView.this.checkForUpdates();
            }
         });
      }
   }

   private class CheckForUpdatesCallbackImpl implements CheckForUpdatesCallback {
      private final WaitLock waitLock;
      private volatile boolean canceled;

      public CheckForUpdatesCallbackImpl(WaitLock var2) {
         this.waitLock = var2;
      }

      @Override
      public void isUpToDate() {
         if (!this.canceled) {
            this.done();
            CheckForUpdatesView.this.displayMessage("You are running the latest version.", CommonIcons.INFO_24);
         }
      }

      private void done() {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               CheckForUpdatesCallbackImpl.this.waitLock.release();
               CheckForUpdatesView.this.checkForUpdatesRequest = null;
               CheckForUpdatesView.this.state = CheckForUpdatesView.State.IDLE;
            }
         });
      }

      @Override
      public void laterVersionFound(LatestVersion var1) {
         if (!this.canceled) {
            this.done();
            this.displayLatestVersionInfo(var1);
         }
      }

      private void displayLatestVersionInfo(final LatestVersion var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               CheckForUpdatesView.this.showLatestVersion(var1);
            }
         });
      }

      @Override
      public void requestCanceled() {
         this.canceled = true;
         this.done();
         CheckForUpdatesView.this.displayMessage("The check for updates did not complete.", CommonIcons.WARNING_24);
      }

      @Override
      public void resultNotAvailable(UpdateServiceException var1) {
         if (!this.canceled) {
            this.done();
            CheckForUpdatesView.this.displayErrorMessage(var1);
         }
      }
   }

   private class DownloadCallback implements DownloadNewVersionCallback {
      private final File file;
      private final DownloadProgessUi ui;

      public DownloadCallback(File var2, DownloadProgessUi var3) {
         this.file = var2;
         this.ui = var3;
      }

      @Override
      public void progressUpdate(int var1, String var2) {
         this.ui.updateProgress(var1, var2);
      }

      @Override
      public void downloadComplete() {
         this.done(CheckForUpdatesView.State.DOWNLOADED);
         this.showDownloadCompleteMessage(this.file);
      }

      private void done(final CheckForUpdatesView.State var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               CheckForUpdatesView.this.state = var1;
               CheckForUpdatesView.this.currentDownloadJob = null;
            }
         });
      }

      private void showDownloadCompleteMessage(final File var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               DownloadCompleteUi var1x = new DownloadCompleteUi(var1);
               CheckForUpdatesView.this.changeView(var1x.layout());
            }
         });
      }

      @Override
      public void downloadCanceled() {
         this.done(CheckForUpdatesView.State.IDLE);
         CheckForUpdatesView.this.displayMessage("The download was canceled.", CommonIcons.INFO_24);
      }

      @Override
      public void downloadFailed(Exception var1) {
         this.done(CheckForUpdatesView.State.IDLE);
         CheckForUpdatesView.this.displayErrorMessage(var1);
      }
   }

   private class StartDownloadAction extends AbstractAction {
      private final LatestVersion version;

      public StartDownloadAction(LatestVersion var2) {
         super("&Download");
         this.version = var2;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               CheckForUpdatesView.this.startDownload(StartDownloadAction.this.version);
            }
         });
      }
   }

   private enum State {
      IDLE,
      CHECKING,
      DOWNLOADING,
      DOWNLOADED;
   }
}
