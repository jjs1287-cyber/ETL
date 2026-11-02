package org.etlforge.integration.client.ui.entity.wizard;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.wizard.ApplicationWindowWizard;
import org.etlforge.application.ui.wizard.WizardPageChain;
import org.etlforge.application.ui.wizard.WizardPageException;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.wait.WaitLock;

public abstract class AbstractEntityWizard<T extends IntegrationEntity> extends ApplicationWindowWizard<T> {
   private final T entity;
   private final Folder parent;
   private boolean hasBeenSaved;

   protected AbstractEntityWizard(ApplicationWindow var1, T var2, Folder var3) {
      super(var1);
      this.entity = Preconditions.checkNotNull((T)var2, "entity");
      this.parent = Preconditions.checkNotNull(var3, "parent");
   }

   public final T getEntityBeingCreated() {
      return this.entity;
   }

   public final Folder getParent() {
      return this.parent;
   }

   @Override
   public final void start() {
      final WaitLock var1 = this.getWaitService().startWait();
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            AbstractEntityWizard.this.startImpl(var1);
         }
      });
   }

   private void startImpl(WaitLock var1) {
      try {
         if (this.getPageChain() == null) {
            WizardPageChain var2 = this.createPageChain(this.entity, this.parent);
            this.setPageChain(var2);
         }

         String var6 = this.getStartSection();
         this.getDisplayer().setSection(var6);
         super.start();
      } finally {
         var1.release();
      }
   }

   protected WizardPageChain createPageChain(T var1, Folder var2) {
      throw new UnsupportedOperationException();
   }

   protected abstract String getStartSection();

   protected final T finishWizard() throws WizardPageException {
      WaitLock var1 = this.startWaitForFinishWork();

      try {
         this.applyPageInformation(this.entity);
         this.hasBeenSaved = true;
         this.doAdditionalWorkOnFinish(this.entity);
         return this.entity;
      } finally {
         var1.release();
      }
   }

   protected WaitLock startWaitForFinishWork() {
      return this.getWaitService().startWait();
   }

   protected abstract void applyPageInformation(T var1) throws WizardPageException;

   protected void doAdditionalWorkOnFinish(T var1) throws WizardPageException {
   }

   protected final void flagAsSaved() {
      this.hasBeenSaved = true;
   }

   @Override
   protected final boolean onWizardCancelled() {
      if (this.isDoneOrCancelled()) {
         return true;
      }

      this.setCancelledState(true);
      boolean var1 = this.isStarted() ? this.promptUserOnCancel() : true;
      if (var1) {
         if (!this.hasBeenSaved) {
            this.entity.delete();
         }
      } else {
         this.setCancelledState(false);
      }

      return var1;
   }

   private boolean isDoneOrCancelled() {
      return this.hasBeenSaved || this.isInCancelledState();
   }

   @Override
   public final String getTitle() {
      String var1 = super.getTitle();
      if (var1 == null) {
         var1 = this.entity.getName();
      }

      if (!var1.endsWith("*")) {
         var1 = var1 + "*";
      }

      return var1;
   }
}
