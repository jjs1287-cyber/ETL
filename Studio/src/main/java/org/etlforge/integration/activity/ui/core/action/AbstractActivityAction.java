package org.etlforge.integration.activity.ui.core.action;

import com.google.common.base.Preconditions;
import org.etlforge.application.ui.BusyWorker;
import org.etlforge.application.ui.action.ActionDictionary;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.action.AbstractSingleActiveObjectAction;
import org.etlforge.application.ui.window.editor.EditorDisplayer;
import org.etlforge.integration.activity.ActivitySubject;
import org.etlforge.integration.activity.ui.ActivityViewUtils;
import org.etlforge.integration.activity.ui.EntireSystemSubject;
import org.etlforge.integration.activity.ui.IntegrationEntityActivitySubject;
import org.etlforge.integration.activity.ui.component.page.ActivityPageManager;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.ui.wait.WaitService;

public abstract class AbstractActivityAction extends AbstractSingleActiveObjectAction<IntegrationEntity> implements ActivityAction {
   private EditorDisplayer editorDisplayer;
   private final WaitService waitService;
   private ActivityPageManager activityPageManager;

   public AbstractActivityAction(String var1, ActionDictionary var2, ApplicationWindow var3) {
      super(var3.getActiveObjectService(), var1, var2, IntegrationEntity.class);
      this.waitService = var3;
      this.editorDisplayer = var3.getEditorService();
      this.putValue("ShortDescription", var2.getToolTip(var1));
   }

   protected abstract ActivityPageManager createActivityPageManager(EditorDisplayer var1);

   @Override
   public final synchronized void setPageDisplayer(EditorDisplayer var1) {
      Preconditions.checkNotNull(var1, "displayer");
      this.editorDisplayer = var1;
   }

   protected final boolean isEnabledFor(IntegrationEntity var1) {
      return ActivityViewUtils.isValidType(var1);
   }

   protected final void runOnObject(IntegrationEntity var1) {
      try {
         ActivitySubject var2 = this.getEntrySubject(var1);
         if (var2 != null && var2.isReady()) {
            this.submitJob(var2);
         }
      } catch (IllegalArgumentException var3) {
         var3.printStackTrace();
      }
   }

   private void submitJob(final ActivitySubject var1) {
      (new BusyWorker(this.waitService) {
         @Override
         protected void doWork() {
            ActivityPageManager var1x = AbstractActivityAction.this.getActivityPageManager();
            var1x.display(var1);
         }
      }).run();
   }

   private synchronized ActivityPageManager getActivityPageManager() {
      if (this.activityPageManager == null) {
         this.activityPageManager = this.createActivityPageManager(this.editorDisplayer);
      }

      return this.activityPageManager;
   }

   private ActivitySubject getEntrySubject(IntegrationEntity var1) throws IllegalArgumentException {
      if (var1 instanceof RootFolder) {
         EntityType var2 = ((RootFolder)var1).getItemType();
         if (var2 == EntityType.Project) {
            return new EntireSystemSubject();
         }
      }

      return IntegrationEntityActivitySubject.create(var1);
   }
}
