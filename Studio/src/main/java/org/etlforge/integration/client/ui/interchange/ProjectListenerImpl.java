package org.etlforge.integration.client.ui.interchange;

import com.google.common.collect.Lists;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectFile;
import org.etlforge.integration.client.project.RecentProjects;
import org.etlforge.integration.client.ui.entity.EntityPageLocator;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.NameChangeListener;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.event.IntegrationProjectEvent;
import org.etlforge.integration.data.project.event.ProjectListener;
import org.etlforge.system.Disposable;
import org.etlforge.ui.util.UiUtils;

final class ProjectListenerImpl extends ProjectListener.Adapter implements Disposable {
   private final RecentProjects recentProjects;
   private final EditorService editorService;
   private final EntityPageLocator pageLocator;
   private final IntegrationProject project;
   private final NameChangeListener nameListener;

   public ProjectListenerImpl(RecentProjects var1, EditorService var2, EntityPageLocator var3, IntegrationProject var4) {
      this.recentProjects = var1;
      this.editorService = var2;
      this.pageLocator = var3;
      this.project = var4;
      this.nameListener = new ProjectListenerImpl.NameChangeHandler(var4);
      var4.addProjectListener(this);
   }

   @Override
   public void dispose() {
      this.project.removeProjectListener(this);
      this.nameListener.dispose();
   }

   @Override
   public void entitiesAdded(IntegrationProjectEvent var1) {
      for (IntegrationEntity var3 : var1.getEntities()) {
         this.selectNodeForCreatedEntityInTree(var3);
      }
   }

   private void selectNodeForCreatedEntityInTree(final IntegrationEntity var1) {
      if (!TreeAndPageSelectionSynchronization.isEnabled()) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               UiCompanion.of(var1).displayInTree();
            }
         });
      }
   }

   @Override
   public void entitiesDeleted(IntegrationProjectEvent var1) {
      final List var2 = var1.getEntities();
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            ProjectListenerImpl.this.closeEditorsForDeletedEntities(var2);
         }
      });
   }

   private void closeEditorsForDeletedEntities(List<IntegrationEntity> var1) {
      ArrayList var2 = Lists.newArrayList();

      for (IntegrationEntity var4 : var1) {
         Editor var5 = this.pageLocator.findOpenPage(var4);
         if (var5 != null) {
            var2.add(var5);
         }

         String[] var6 = new String[]{var4.getID().toString()};
         this.editorService.getEditorMemento().remove(var6);
      }

      for (Object __etlItem86 : var2) {
         Editor var8 = (Editor) __etlItem86;
         var8.close();
      }
   }

   private class NameChangeHandler extends NameChangeListener {
      public NameChangeHandler(IntegrationProject var2) {
         super(var2);
      }

      @Override
      protected void nameChanged(String var1, String var2) {
         ManagedProject var3 = ProjectListenerImpl.this.project.getExtensionObject(ManagedProject.class);
         ProjectFile var4 = var3.getProjectFile();
         this.updateRecentProject(var1, var2, var4);
      }

      private void updateRecentProject(String var1, String var2, ProjectFile var3) {
         ProjectListenerImpl.this.recentProjects.projectWasRenamed(var1, var3.getParentFolder(), var2);
      }
   }
}
