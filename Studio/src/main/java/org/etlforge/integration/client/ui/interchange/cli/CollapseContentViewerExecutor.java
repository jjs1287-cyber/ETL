package org.etlforge.integration.client.ui.interchange.cli;

import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.ui.interchange.content.ProjectContentViewer;
import org.etlforge.integration.client.ui.interchange.content.ProjectContentViewerManager;
import org.etlforge.integration.data.entity.RootFolder;

final class CollapseContentViewerExecutor extends AbstractContentViewerExpansionExecutor {
   public CollapseContentViewerExecutor(CurrentProjectService var1, ProjectContentViewerManager var2) {
      super(var1, var2);
   }

   @Override
   protected void execute(ProjectContentViewer var1, RootFolder var2) {
      var1.collapseAll(var2);
   }
}
