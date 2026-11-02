package org.etlforge.integration.activity.ui.core.action;

import org.etlforge.application.ui.action.ActionDictionary;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.EditorDisplayer;
import org.etlforge.integration.activity.ActivitySubject;
import org.etlforge.integration.activity.serverapi.history.OperationHistoryProvider;
import org.etlforge.integration.activity.serverapi.history.SourceHistoryProvider;
import org.etlforge.integration.activity.ui.component.page.ActivityPage;
import org.etlforge.integration.activity.ui.component.page.ActivityPageManager;
import org.etlforge.integration.activity.ui.history.OperationHistoryPage;

public final class ShowHistoryAction extends AbstractActivityAction {
   private final OperationHistoryProvider opHistoryApi;
   private final SourceHistoryProvider sourceHistoryApi;
   private boolean deleteSourceHistorySupported = true;

   public ShowHistoryAction(ApplicationWindow var1, OperationHistoryProvider var2, SourceHistoryProvider var3) {
      this(ActivityActionDictionary.DICTIONARY, var1, var2, var3);
   }

   public ShowHistoryAction(ActionDictionary var1, ApplicationWindow var2, OperationHistoryProvider var3, SourceHistoryProvider var4) {
      super("activity.action.History", var1, var2);
      this.opHistoryApi = var3;
      this.sourceHistoryApi = var4;
   }

   @Override
   protected ActivityPageManager createActivityPageManager(EditorDisplayer var1) {
      return new ActivityPageManager(var1, true) {
         @Override
         protected ActivityPage createNewPage(ActivitySubject var1) {
            return new OperationHistoryPage(
               var1, ShowHistoryAction.this.opHistoryApi, ShowHistoryAction.this.sourceHistoryApi, ShowHistoryAction.this.deleteSourceHistorySupported
            );
         }
      };
   }

   public void setDeleteSourceHistorySupported(boolean var1) {
      this.deleteSourceHistorySupported = var1;
   }
}
