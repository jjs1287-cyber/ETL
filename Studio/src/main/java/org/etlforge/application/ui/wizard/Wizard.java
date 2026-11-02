package org.etlforge.application.ui.wizard;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.Application;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.DoNotShowAgainQuestion;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.application.ui.wizard.prefs.ConfirmWizardCancelledPreference;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.system.Disposable;
import org.etlforge.system.DisposableCollection;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.wait.MessageWaitService;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.util.preference.KongaPreferences;

public abstract class Wizard<T> {
   private static final int NAVIGATION_WAIT_DELAY = 100;
   private WizardDisplayer displayer;
   private volatile WizardPage currentPage;
   private final LinkedList<WizardPage> applicablePages = new LinkedList<>();
   private final Map<WizardPage, Boolean> allVisitedPages = new IdentityHashMap<>();
   private final WizardListeners<T> listeners = new WizardListeners<>();
   private WizardPageChain pageChain;
   private boolean isInCancelledState;
   private boolean isNavigating;
   private boolean isInDone;
   private String title;
   private String helpId;
   private final List<Wizard> nestedWizards = Lists.newArrayList();
   private Wizard currentWizard;
   private WizardPageChain currentPageChain;
   private boolean started;
   private final KongaPreferences preferences;
   private volatile WaitService waitService = WaitService.NONE;
   private boolean finishWizardOnEventThread = true;
   private final DisposableCollection disposables = new DisposableCollection();

   public Wizard() {
      this.preferences = KongaPreferences.getPreferences(this.getClass());
   }

   public final void setWaitService(WaitService var1) {
      this.waitService = Preconditions.checkNotNull(var1);
   }

   protected final WaitService getWaitService() {
      return this.waitService;
   }

   public final KongaPreferences getPreferences() {
      return this.preferences;
   }

   public void addNestedWizard(Wizard var1) {
      Preconditions.checkNotNull(var1, "wizard");
      Preconditions.checkArgument(var1 != this, "A wizard cannot add itself as a sub-wizard");
      this.nestedWizards.add(var1);
      if (this.displayer != null) {
         var1.setDisplayer(this.displayer);
      }
   }

   public void setDisplayer(WizardDisplayer var1) {
      Preconditions.checkNotNull(var1, "displayer");
      this.displayer = var1;

      for (Wizard var3 : this.nestedWizards) {
         var3.setDisplayer(var1);
      }
   }

   public WizardDisplayer getDisplayer() {
      return this.displayer;
   }

   public void setPageChain(WizardPageChain var1) {
      Preconditions.checkNotNull(var1, "chain");
      this.pageChain = var1;
   }

   protected WizardPageChain getPageChain() {
      return this.pageChain;
   }

   public void updateNavigationControls() {
      if (this.displayer != null) {
         this.displayer.updateNavigationControls();
      }
   }

   public void start() {
      Preconditions.checkState(this.displayer != null, "A Wizard cannot be started without a displayer.");
      Preconditions.checkState(this.pageChain != null, "A Wizard cannot be started without pages.");
      this.currentWizard = this;
      this.currentPageChain = this.pageChain;
      WizardPage var1 = this.getFirstPage();
      this.newPageVisited(var1);
      this.switchToPage(var1);
      this.started = true;
   }

   private void newPageVisited(WizardPage var1) {
      if (this.applicablePages.isEmpty() || this.applicablePages.getLast() != var1) {
         this.applicablePages.addLast(var1);
      }

      if (!this.allVisitedPages.containsKey(var1)) {
         var1.addToWizard();
         this.allVisitedPages.put(var1, null);
      }
   }

   private void switchToPage(WizardPage var1) {
      this.getDisplayer().showPage(var1);
      this.currentPage = var1;
   }

   public String getTitle() {
      return this.title;
   }

   public void setTitle(String var1) {
      this.title = var1;
      if (this.displayer != null) {
         this.displayer.updateTitle();
      }
   }

   public String getHelpId() {
      return this.helpId;
   }

   public void setHelpId(String var1) {
      this.helpId = var1;
   }

   public final WizardPage getCurrentPage() {
      return this.currentPage;
   }

   public List<WizardPage> getAllApplicablePages() {
      return new ArrayList<>(this.applicablePages);
   }

   protected final synchronized boolean isInCancelledState() {
      return this.isInCancelledState;
   }

   protected final synchronized void setCancelledState(boolean var1) {
      this.isInCancelledState = var1;
   }

   private final void onWizardDone() {
      Runnable var1 = new Runnable() {
         @Override
         public void run() {
            try {
               T var1x = Wizard.this.finishWizard();
               Wizard.this.currentPage = null;
               Wizard.this.fireWizardCompleted(var1x);
            } catch (WizardPageException var2) {
               Wizard.this.fireWizardFailed(var2);
            }
         }
      };
      if (this.finishWizardOnEventThread) {
         var1.run();
      } else {
         KongaSwingWorker.runWhileWaiting(var1, this.getWaitService());
      }
   }

   protected abstract T finishWizard() throws WizardPageException;

   protected abstract boolean onWizardCancelled();

   public WizardPage getFirstPage() {
      return this.pageChain.getFirstPage();
   }

   public boolean hasNextPage(WizardPage var1) {
      return this.currentPageChain.hasNextPage(var1) || this.getNextWizard() != null;
   }

   private Wizard getNextWizard() {
      if (this.nestedWizards.isEmpty()) {
         return null;
      }

      int var1 = this.nestedWizards.indexOf(this.currentWizard) + 1;
      return var1 < this.nestedWizards.size() ? this.nestedWizards.get(var1) : null;
   }

   public boolean hasPreviousPage(WizardPage var1) {
      return this.currentPageChain.hasPreviousPage(var1) || this.getPreviousWizard() != null;
   }

   private Wizard getPreviousWizard() {
      if (this.currentWizard != this && !this.nestedWizards.isEmpty()) {
         int var1 = this.nestedWizards.indexOf(this.currentWizard) - 1;
         return var1 < 0 ? this : this.nestedWizards.get(var1);
      } else {
         return null;
      }
   }

   public boolean canFinish() {
      return this.currentPage != null && this.currentPage.canContinue() && !this.hasNextPage(this.currentPage);
   }

   public void handlePageException(String var1, Throwable var2) {
      if (var1 == null) {
         var1 = "An error occurred.";
      }

      ErrorLog.attention(this.getClass(), var1, var2);
   }

   public final void nextPageRequested() {
      if (!this.isNavigating() && this.currentPage.canContinue()) {
         this.setNavigating(true);
         WaitLock var1 = this.startWaitBeforeMovingToTheNextPage();
         Wizard.NextPageRequestedJob var2 = new Wizard.NextPageRequestedJob(this.currentPage, var1);
         var2.execute();
      }
   }

   private WaitLock startWaitBeforeMovingToTheNextPage() {
      if (this.currentPage instanceof AbstractWizardPage) {
         String var1 = ((AbstractWizardPage)this.currentPage).getForwardNavigationMessage();
         if (StringUtils.isNotEmpty(var1)) {
            WaitService var2 = this.getDisplayer().getWaitService();
            if (var2 instanceof MessageWaitService) {
               return ((MessageWaitService)var2).startWait(var1);
            }
         }
      }

      return this.startWait(100);
   }

   protected final void setFinishWizardOnEventThread(boolean var1) {
      this.finishWizardOnEventThread = var1;
   }

   private void done() {
      if (!this.isInDone) {
         this.isInDone = true;
         this.onWizardDone();
         this.finishNestedWizards();
         this.isInDone = false;
      }
   }

   private void finishNestedWizards() {
      for (Wizard var2 : this.nestedWizards) {
         if (var2.isStarted()) {
            var2.onWizardDone();
         }
      }
   }

   private void removeLastPage() {
      this.applicablePages.removeLast();
   }

   protected final boolean isStarted() {
      return this.started;
   }

   public final void previousPageRequested() {
      if (!this.isNavigating()) {
         this.setNavigating(true);
         WaitLock var1 = this.startWait(100);
         Wizard.PreviousPageRequestedJob var2 = new Wizard.PreviousPageRequestedJob(var1);
         EventQueue.invokeLater(var2);
      }
   }

   public final void saveRequested() {
      Preconditions.checkState(this.currentPage != null && !this.applicablePages.isEmpty(), "Cannot invoke save on an empty wizard.");
      final Runnable var1 = new Runnable() {
         @Override
         public void run() {
            Wizard.this.done();
         }
      };
      ApplicationWorker.SafeRunnable var2 = new ApplicationWorker.SafeRunnable() {
         @Override
         public void run() {
            if (Wizard.this.currentPage.prepareForForwardNavigation()) {
               EventQueue.invokeLater(var1);
            }
         }
      };
      ApplicationWorker var3 = Application.getWorker();
      var3.submitForParallel(var2);
   }

   public final boolean cancelWizard() {
      boolean var1 = this.onWizardCancelled() && this.cancelSubWizards();
      if (var1) {
         this.fireWizardCancelled();
      }

      return var1;
   }

   private boolean cancelSubWizards() {
      for (Wizard var2 : this.nestedWizards) {
         if (!var2.cancelWizard()) {
            return false;
         }
      }

      return true;
   }

   private WaitLock startWait(int var1) {
      return this.getDisplayer().getWaitService().startWait(var1);
   }

   private void setNavigating(boolean var1) {
      this.isNavigating = var1;
   }

   private boolean isNavigating() {
      return this.isNavigating;
   }

   protected final void closeAllPages() {
      UiThreadSupplier.call(new Supplier<Void>() {
         public Void get() {
            for (WizardPage var2 : Wizard.this.allVisitedPages.keySet()) {
               var2.removeFromWizard();
            }

            Wizard.this.applicablePages.clear();
            Wizard.this.allVisitedPages.clear();
            return null;
         }
      });
   }

   public final void addWizardListener(WizardListener<T> var1) {
      this.listeners.add(var1);
   }

   public final void removeWizardListener(WizardListener<T> var1) {
      this.listeners.remove(var1);
   }

   protected final void fireWizardCompleted(final T var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            Wizard.this.listeners.call().wizardCompleted((T)var1);
            Wizard.this.dispose();
         }
      });
   }

   protected final void fireWizardFailed(final Throwable var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            Wizard.this.listeners.call().wizardFailed(var1);
         }
      });
   }

   protected final void fireWizardCancelled() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            Wizard.this.listeners.call().wizardCancelled();
            Wizard.this.dispose();
         }
      });
   }

   private void dispose() {
      this.closeAllPages();
      this.disposables.dispose();
      this.listeners.removeAll();
      this.currentPage = null;
   }

   protected final void addDisposableResource(Disposable var1) {
      this.disposables.add(var1);
   }

   protected final boolean promptUserOnCancel() {
      return new DoNotShowAgainQuestion(
            "Are you sure you want to abort the wizard?",
            "Abort the wizard?",
            ConfirmWizardCancelledPreference.instance(),
            Alert.Option.YES_NO,
            Alert.Option.YES,
            Alert.Type.WARNING
         )
         .ask(null);
   }

   private class NextPageRequestedJob extends KongaSwingWorker<Boolean, Void> {
      private final WizardPage currentPage;
      private final WaitLock waitLock;

      public NextPageRequestedJob(WizardPage var2, WaitLock var3) {
         this.currentPage = var2;
         this.waitLock = var3;
      }

      protected Boolean doInBackground() throws Exception {
         return this.currentPage.prepareForForwardNavigation();
      }

      @Override
      protected void finished() {
         this.waitLock.release();
         Wizard.this.setNavigating(false);
      }

      protected void onSuccess(Boolean var1) {
         if (var1) {
            WizardPage var2 = this.getNextPage();
            if (var2 == null) {
               Wizard.this.done();
            } else {
               Wizard.this.currentWizard.newPageVisited(var2);
               Wizard.this.switchToPage(var2);
            }
         }
      }

      private WizardPage getNextPage() {
         Preconditions.checkState(this.currentPage != null, "No page is currently displayed by this wizard.");
         WizardPage var1 = Wizard.this.currentPageChain.getNextPage(this.currentPage);
         if (var1 == null) {
            Wizard var2 = Wizard.this.getNextWizard();
            if (var2 != null) {
               Wizard.this.currentWizard = var2;
               Wizard.this.currentPageChain = Wizard.this.currentWizard.getPageChain();
               if (!var2.isStarted()) {
                  var2.start();
               }

               var1 = Wizard.this.currentPageChain.getFirstPage();
            }
         }

         return var1;
      }
   }

   private final class PreviousPageRequestedJob implements Runnable {
      private final WaitLock waitLock;

      private PreviousPageRequestedJob(WaitLock var2) {
         this.waitLock = var2;
      }

      @Override
      public void run() {
         try {
            if (Wizard.this.currentPage.prepareForBackwardNavigation()) {
               Wizard.this.currentWizard.removeLastPage();
               WizardPage var1 = this.getPreviousPage();
               if (var1 != null) {
                  Wizard.this.switchToPage(var1);
               }

               return;
            }
         } finally {
            this.waitLock.release();
            Wizard.this.setNavigating(false);
         }
      }

      private WizardPage getPreviousPage() {
         WizardPage var1 = Wizard.this.currentPageChain.getPreviousPage(Wizard.this.currentPage);
         if (var1 == null) {
            Wizard var2 = Wizard.this.getPreviousWizard();
            if (var2 != null) {
               Wizard.this.currentWizard = var2;
               Wizard.this.currentPageChain = Wizard.this.currentWizard.getPageChain();
               var1 = Wizard.this.currentPageChain.getLastPage();
            }
         }

         return var1;
      }
   }
}
