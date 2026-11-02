package org.etlforge.integration.client.ui.interchange;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.integration.client.ui.interchange.content.ProjectContentViewer;

public final class CollapseProjectContentAction extends AbstractAction {
   private final ProjectContentViewer viewer;

   public CollapseProjectContentAction(ProjectContentViewer var1) {
      super("Collapse All", ApplicationResources.ICONS.getIcon("CollapseTree.16"));
      this.viewer = var1;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.viewer.collapseAll(null);
   }
}
