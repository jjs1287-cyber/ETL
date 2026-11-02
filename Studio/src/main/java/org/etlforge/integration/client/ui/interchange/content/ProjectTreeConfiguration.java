package org.etlforge.integration.client.ui.interchange.content;

import java.util.Collections;
import org.etlforge.application.ui.action.popup.PopupMenuManager;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.integration.client.ui.entity.move.EntityMover;
import org.etlforge.integration.client.ui.entity.tree.EntityTreePathPublisher;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTree;
import org.etlforge.integration.client.ui.entity.tree.ProjectNodeRenamer;
import org.etlforge.integration.client.ui.interchange.component.tree.ProjectTreeController;
import org.etlforge.integration.client.ui.interchange.component.tree.RenameEntityNodeStrategy;
import org.etlforge.integration.client.ui.interchange.component.tree.TreeDropStrategy;
import org.etlforge.integration.client.ui.interchange.component.tree.TreeTransferHandler;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfigLookup;
import org.etlforge.integration.client.ui.project.ProjectRenamer;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.widget.tree.KongaTree;

public final class ProjectTreeConfiguration {
   private final EntityConfigLookup entityConfigs;
   private final ProjectRenamer projectRenamer;
   private final EntityMover entityMover;
   private final ProjectTreeController controller;

   public ProjectTreeConfiguration(EntityConfigLookup var1, ProjectRenamer var2, EntityMover var3, ProjectTreeController var4) {
      this.entityConfigs = var1;
      this.projectRenamer = var2;
      this.entityMover = var3;
      this.controller = var4;
   }

   public void setPopupMenuManager(PopupMenuManager var1) {
      this.controller.setPopupMenuManager(var1);
   }

   public void configure(IntegrationEntityTree var1) {
      this.configure(var1, Collections.emptyList());
   }

   public void configure(IntegrationEntityTree var1, ContentViewerCategory var2) {
      this.configure(var1, Collections.singletonList(var2));
   }

   public void configure(IntegrationEntityTree var1, Iterable<ContentViewerCategory> var2) {
      TreeTransferHandler var3 = this.createTransferHandler(var2);
      var1.setTransferHandler(var3);
      this.controller.install(var1);
      var1.setMouseRenameEnabled(true);
      var1.setRenameNodeStrategy(new RenameEntityNodeStrategy(this.entityConfigs));
      var1.setProjectNodeRenamer(new ProjectNodeRenamer() {
         @Override
         public void rename(IntegrationProject var1) {
            ProjectTreeConfiguration.this.projectRenamer.renameProject(var1);
         }
      });
      var1.setBackground(Colors.TREE_BACKGROUND);
      var1.addKongaTreeListener(new EntityTreePathPublisher(var1));
      var1.setSelectionMode(KongaTree.SelectionMode.DISCONTIGUOUS_TREE_SELECTION);
      var1.setDragEnabled(true);
      var1.setRowHeight(var1.getRowHeight() + 2);
   }

   private TreeTransferHandler createTransferHandler(Iterable<ContentViewerCategory> var1) {
      TreeTransferHandler var2 = new TreeTransferHandler(this.entityMover);

      for (ContentViewerCategory var4 : var1) {
         for (TreeDropStrategy var6 : var4.getTreeDropStrategies(this.entityConfigs)) {
            var2.addDropStrategy(var6);
         }
      }

      return var2;
   }
}
