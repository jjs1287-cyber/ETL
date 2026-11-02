package org.etlforge.integration.activity.ui.core.action;

import org.etlforge.application.ui.action.ActionDictionary;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.EditorDisplayer;
import org.etlforge.integration.activity.ActivitySubject;
import org.etlforge.integration.activity.serverapi.queue.OperationQueueProvider;
import org.etlforge.integration.activity.ui.component.page.ActivityPage;
import org.etlforge.integration.activity.ui.component.page.ActivityPageManager;
import org.etlforge.integration.activity.ui.queue.OperationQueuePage;

public final class ShowQueueAction extends AbstractActivityAction {
   private final OperationQueueProvider queueApi;

   public ShowQueueAction(ApplicationWindow var1, OperationQueueProvider var2) {
      this(ActivityActionDictionary.DICTIONARY, var1, var2);
   }

   public ShowQueueAction(ActionDictionary var1, ApplicationWindow var2, OperationQueueProvider var3) {
      super("activity.action.Queue", var1, var2);
      this.queueApi = var3;
   }

   @Override
   protected ActivityPageManager createActivityPageManager(EditorDisplayer var1) {
      return new ActivityPageManager(var1, true) {
         @Override
         protected ActivityPage createNewPage(ActivitySubject var1) {
            return new OperationQueuePage(var1, ShowQueueAction.this.queueApi);
         }
      };
   }
}
