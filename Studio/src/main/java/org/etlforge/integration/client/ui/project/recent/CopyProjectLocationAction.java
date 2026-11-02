package org.etlforge.integration.client.ui.project.recent;

import java.awt.event.ActionEvent;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.util.clipboard.ClipboardUtils;

public class CopyProjectLocationAction extends AbstractSingleObjectSelectionAction<ProjectLocation> implements RecentProjectsViewerAction {
   public CopyProjectLocationAction() {
      super(ProjectLocation.class, Strings.get("Recent.CopyLocation.Name"));
   }

   @Override
   public void setViewer(RecentProjectsViewer var1) {
      this.setSelectionSource(var1);
   }

   protected void run(ProjectLocation var1, ActionEvent var2) {
      String var3 = var1.getLocationPath();
      ClipboardUtils.copyToSystemClipboard(var3);
   }
}
