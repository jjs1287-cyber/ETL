package org.etlforge.integration.client.ui.interchange.core.action;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.entity.typeselector.EntityTypeSelectionDialog;
import org.etlforge.integration.client.ui.interchange.content.ProjectContentViewer;
import org.etlforge.integration.client.ui.interchange.entity.DuplicateFolderStructureWorkflow;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.IntegrationProject;

public final class DuplicateFolderStructureAction extends AbstractAction {
   private final RootFolder sourceFolder;
   private final ImmutableSet<EntityType> selectableTypes;
   private final ProjectPersistor persistor;
   private volatile ProjectContentViewer projectViewer;

   public DuplicateFolderStructureAction(RootFolder var1, Iterable<EntityType> var2, ProjectPersistor var3) {
      super("Duplicate Folder Structure To...");
      Preconditions.checkArgument(var1.getParent() instanceof IntegrationProject, "sourceFolder must belong to a project");
      this.sourceFolder = var1;
      this.persistor = var3;
      this.selectableTypes = FluentIterable.<EntityType>from(var2).filter(new DuplicateFolderStructureAction.TypeFilter(var1)).toImmutableSet();
      this.putValue("ShortDescription", "Duplicate the folder structure for this item type to one or more other types in the project");
   }

   public void setProjectViewer(ProjectContentViewer var1) {
      this.projectViewer = var1;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ImmutableSet var1x = DuplicateFolderStructureAction.this.selectDestinationTypes();
            if (var1x != null && !var1x.isEmpty()) {
               DuplicateFolderStructureAction.this.startDuplication(var1x);
            }
         }
      });
   }

   private ImmutableSet<EntityType> selectDestinationTypes() {
      EntityTypeSelectionDialog var1 = new EntityTypeSelectionDialog()
         .ownedBy(ApplicationUi.getAppWinFrame())
         .withTitle("Select Destination Types")
         .including(this.selectableTypes);
      return var1.selectTypes();
   }

   private void startDuplication(ImmutableSet<EntityType> var1) {
      DuplicateFolderStructureWorkflow var2 = new DuplicateFolderStructureWorkflow(this.sourceFolder, this.persistor);
      var2.setProjectViewer(this.projectViewer);
      ApplicationWindow var3 = ApplicationUi.getAppWin();
      if (var3 != null) {
         var2.setWaitService(var3);
      }

      var2.duplicateTo(var1);
   }

   private static class TypeFilter implements Predicate<EntityType> {
      private final ImmutableSet<EntityType> exclude;

      public TypeFilter(RootFolder var1) {
         this.exclude = ImmutableSet.of(var1.getItemType(), EntityType.Folder, EntityType.RootFolder, EntityType.Project);
      }

      public boolean apply(EntityType var1) {
         return !this.exclude.contains(var1);
      }
   }
}
