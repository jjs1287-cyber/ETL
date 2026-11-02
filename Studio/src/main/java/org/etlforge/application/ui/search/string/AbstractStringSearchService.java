package org.etlforge.application.ui.search.string;

import java.awt.EventQueue;
import org.etlforge.ui.DelayedJob;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.util.UiUtils;

public abstract class AbstractStringSearchService implements UiProvider {
   private static final int LARGE_MODEL_DELAY = 200;
   private final StringSearchModelDefiner ui;
   private boolean largeModel;
   private DelayedJob delayedUpdater;

   public AbstractStringSearchService() {
      this(StringSearchRule.CONTAINS_IGNORE_CASE, StringSearchRule.STARTS_WITH_IGNORE_CASE, StringSearchRule.ENDS_WITH_IGNORE_CASE);
   }

   public AbstractStringSearchService(StringSearchRule... var1) {
      this.ui = this.createUi(var1);
      this.ui.getSearchModel().addModelListener(new StringSearchModelListener() {
         @Override
         public void searchRuleChanged(StringSearchModel var1, StringSearchRule var2) {
            AbstractStringSearchService.this.updateFilter();
         }

         @Override
         public void searchStringChanged(StringSearchModel var1, String var2) {
            AbstractStringSearchService.this.updateFilter();
         }
      });
   }

   protected abstract void applyNewFilter();

   public final void setLargeModel(boolean var1) {
      this.largeModel = var1;
   }

   public final void setSearchString(String var1) {
      this.ui.setSearchString(var1);
   }

   public final void setSelectedRule(StringSearchRule var1) {
      this.ui.setSelectedRule(var1);
   }

   public final void setCaption(String var1) {
      this.ui.setCaption(var1);
   }

   public final void setToolTip(String var1) {
      this.ui.setToolTip(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.ui.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.ui.requestFocus();
   }

   protected final StringSearchModel getSearchModel() {
      return this.ui.getSearchModel();
   }

   private void updateFilter() {
      if (this.largeModel) {
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               AbstractStringSearchService.this.doDelayedUpdate();
            }
         });
      } else {
         this.applyNewFilter();
      }
   }

   private void doDelayedUpdate() {
      assert EventQueue.isDispatchThread() : "This method should be called from the EDT";
      if (this.delayedUpdater != null) {
         this.delayedUpdater.cancel();
      }

      this.delayedUpdater = DelayedJob.createAndSchedule(200, new Runnable() {
         @Override
         public void run() {
            AbstractStringSearchService.this.applyNewFilter();
            AbstractStringSearchService.this.delayedUpdater = null;
         }
      });
   }

   private StringSearchModelDefiner createUi(StringSearchRule... var1) {
      StringSearchModelDefiner var2 = new StringSearchModelDefiner(var1);
      var2.setSelectedRule(StringSearchRule.CONTAINS_IGNORE_CASE);
      var2.setEmptyStringMatchesEverything(true);
      return var2;
   }
}
