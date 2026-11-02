package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.event.ActionEvent;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.integration.client.ui.entity.EntityPageDisplayer;
import org.etlforge.integration.client.ui.interchange.content.ProjectContentViewer;
import org.etlforge.integration.client.ui.interchange.content.ProjectContentViewerManager;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class OpenPageAction extends AbstractApplicationAction {
   public static final String ID = "action.ic.OpenPage";
   private final ProjectContentViewerManager contentViewers;
   private final EntityPageDisplayer pageDisplayer;

   public OpenPageAction(ProjectContentViewerManager var1, EntityPageDisplayer var2) {
      super("action.ic.OpenPage", InterchangeActionDictionary.DICTIONARY);
      this.contentViewers = var1;
      this.pageDisplayer = var2;
   }

   @Override
   public void execute(ActionEvent var1) {
      ProjectContentViewer var2 = this.contentViewers.getViewer();

      for (IntegrationEntity var4 : var2.getSelectedEntities()) {
         this.pageDisplayer.openPage(var4);
      }
   }
}
