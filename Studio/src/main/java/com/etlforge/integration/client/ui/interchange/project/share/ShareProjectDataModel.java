package com.etlforge.integration.client.ui.interchange.project.share;

import org.etlforge.application.Application;
import org.etlforge.integration.client.project.CloseProjectVetoException;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.project.backup.AvailableBackup;
import org.etlforge.integration.client.ui.entity.tree.DynamicProjectTreeModel;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTreeModel;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTreeModelFactory;
import org.etlforge.system.Disposable;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;

public final class ShareProjectDataModel extends AbstractPropertyBasedModel implements Disposable {
   public static final String SELECTED_BACKUP = "backup";
   public static final String SOURCE_PROJECT = "sourceProject";
   public static final String SOURCE_PROJECT_TITLE = "sourceProjectTitle";
   public static final String SOURCE_PROJECT_TREE_MODEL = "sourceProjectTreeModel";
   public static final String TARGET_PROJECT_TITLE = "targetProjectTitle";
   private final Property<AvailableBackup> selectedBackup;
   private final Property<ManagedProject> sourceProject;
   private final Property<String> sourceProjectTitle;
   private final Property<IntegrationEntityTreeModel> sourceTreeModel;
   private final ManagedProject targetProject;
   private final DynamicProjectTreeModel targetTreeModel;
   private final Property<String> targetProjectTitle;
   private final ProjectManager projectManager;

   public ShareProjectDataModel(ProjectManager var1, ManagedProject var2) {
      this.projectManager = var1;
      this.selectedBackup = this.newProperty("backup");
      this.sourceProject = this.newProperty("sourceProject");
      this.sourceProjectTitle = this.newProperty("sourceProjectTitle", "Import From");
      this.sourceTreeModel = this.newProperty("sourceProjectTreeModel", new IntegrationEntityTreeModel("[Empty]"));
      this.targetProject = var2;
      this.targetTreeModel = this.createTargetTreeModel(var2);
      this.targetProjectTitle = this.newProperty("targetProjectTitle", "Import To");
   }

   public ProjectManager getProjectManager() {
      return this.projectManager;
   }

   public void setEmptySourceTreeText(String var1) {
      this.sourceTreeModel.set(new IntegrationEntityTreeModel(var1));
   }

   @Override
   public void dispose() {
      this.targetTreeModel.dispose();
      this.closeCurrentSourceProject();
   }

   public AvailableBackup getSelectedBackup() {
      return this.selectedBackup.get();
   }

   public void setSelectedBackup(AvailableBackup var1) {
      this.selectedBackup.set(var1);
   }

   public ManagedProject getTargetProject() {
      return this.targetProject;
   }

   public IntegrationEntityTreeModel getTargetProjectTreeModel() {
      return this.targetTreeModel.getTreeModel();
   }

   public void setSourceProject(ManagedProject var1) {
      this.closeCurrentSourceProject();
      this.sourceProject.set(var1);
      this.sourceTreeModel.set(this.createTreeModelForProject(var1));
   }

   public ManagedProject getSourceProject() {
      return this.sourceProject.get();
   }

   public void setSourceProjectTitle(String var1) {
      this.sourceProjectTitle.set(var1);
   }

   public void setTargetProjectTitle(String var1) {
      this.targetProjectTitle.set(var1);
   }

   public IntegrationEntityTreeModel getSourceProjectTreeModel() {
      return this.sourceTreeModel.get();
   }

   private DynamicProjectTreeModel createTargetTreeModel(ManagedProject var1) {
      DynamicProjectTreeModel var2 = new DynamicProjectTreeModel();
      var2.setProject(var1.getProject());
      var2.setTreeModel(this.createTreeModelForProject(var1));
      return var2;
   }

   private IntegrationEntityTreeModel createTreeModelForProject(ManagedProject var1) {
      IntegrationEntityTreeModelFactory var2 = new IntegrationEntityTreeModelFactory(Application.getBean("ic.EntityTypeCategories", Iterable.class));
      return var2.newModel(var1.getProject());
   }

   private void closeCurrentSourceProject() {
      ManagedProject var1 = this.getSourceProject();
      if (var1 != null) {
         try {
            this.projectManager.closeProject(var1);
         } catch (CloseProjectVetoException var3) {
            throw new RuntimeException(var3);
         }
      }
   }
}
