package org.etlforge.integration.activity.ui.core.action;

import com.google.common.base.Preconditions;
import org.etlforge.application.ui.action.ActionDictionary;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.EditorDisplayer;
import org.etlforge.integration.activity.ActivitySubject;
import org.etlforge.integration.activity.serverapi.log.OperationLogProvider;
import org.etlforge.integration.activity.ui.component.page.ActivityPage;
import org.etlforge.integration.activity.ui.component.page.ActivityPageManager;
import org.etlforge.integration.activity.ui.log.OperationLogPage;

public final class ShowLogAction extends AbstractActivityAction {
   private final OperationLogProvider logApi;

   public ShowLogAction(ApplicationWindow var1, OperationLogProvider var2) {
      this(ActivityActionDictionary.DICTIONARY, var1, var2);
   }

   public ShowLogAction(ActionDictionary var1, ApplicationWindow var2, OperationLogProvider var3) {
      super("activity.action.Log", var1, var2);
      Preconditions.checkNotNull(var3, "logApi");
      this.logApi = var3;
   }

   @Override
   protected ActivityPageManager createActivityPageManager(EditorDisplayer var1) {
      return new ActivityPageManager(var1, true) {
         @Override
         protected ActivityPage createNewPage(ActivitySubject var1) {
            return new OperationLogPage(var1, ShowLogAction.this.logApi);
         }
      };
   }
}
