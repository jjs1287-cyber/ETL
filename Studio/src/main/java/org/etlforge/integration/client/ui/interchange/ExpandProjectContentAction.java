package org.etlforge.integration.client.ui.interchange;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.integration.client.ui.interchange.content.ProjectContentViewer;

public final class ExpandProjectContentAction extends AbstractAction {
   private final ProjectContentViewer viewer;

   public ExpandProjectContentAction(ProjectContentViewer var1) {
      super("Expand All", ApplicationResources.ICONS.getIcon("ExpandTree.16"));
      this.viewer = var1;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.viewer.expandAll(null);
   }
}
