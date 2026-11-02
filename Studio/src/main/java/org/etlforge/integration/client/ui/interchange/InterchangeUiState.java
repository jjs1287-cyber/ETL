package org.etlforge.integration.client.ui.interchange;

import com.google.common.collect.Lists;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.Action;
import org.etlforge.application.ui.job.JobStatusAdapter;
import org.etlforge.application.ui.job.JobStatusChangedEvent;
import org.etlforge.application.ui.window.ActiveObjectService;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUiState;
import org.etlforge.integration.client.ui.entity.EntityPageDisplayer;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.content.ProjectContentViewer;
import org.etlforge.integration.data.entity.EntityDescriptor;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.ui.event.ApplicationMessageEvent;

public final class InterchangeUiState {
   public static void resetState(ManagedProject var0) {
      ProjectUiState var1 = var0.getUiState();
      var1.reset();
      saveStateToDisk(var0);
   }

   public static void saveState(ManagedProject var0, ProjectContentViewer var1, EditorService var2) {
      ProjectUiState var3 = var0.getUiState();
      var3.reset();
      processDisplayedEntities(var3, var2);
      processExpandedNodes(var3, var1);
      saveStateToDisk(var0);
   }

   private static void processDisplayedEntities(ProjectUiState var0, EditorService var1) {
      Editor var2 = var1.getActiveEditor();
      ArrayList var3 = Lists.newArrayList();

      for (Editor var7 : var1.getOpenEditors()) {
         if (var7 instanceof IntegrationEntityPage) {
            IntegrationEntity var8 = ((IntegrationEntityPage)var7).getObject();
            var3.add(var8);
            if (var7 == var2) {
               var0.setActiveEntityOnStartUp(var8);
            }
         }
      }

      var0.setEntitiesToDisplayOnStartUp(var3);
   }

   private static void processExpandedNodes(ProjectUiState var0, ProjectContentViewer var1) {
      ArrayList var2 = Lists.newArrayList();

      for (IntegrationEntity var4 : var1.getExpandedEntities()) {
         var2.add(new EntityDescriptor(var4));
      }

      var0.setExpandedOnStartUp(var2);
   }

   private static void saveStateToDisk(ManagedProject var0) {
      try {
         var0.saveUiState();
      } catch (InterchangeSavingException var2) {
         ApplicationMessageEvent.postErrorMessage(InterchangeUiState.class, PackageResources.UiState.getSaveFailed(var2));
      }
   }

   public static void restoreState(
      ManagedProject var0, ProjectContentViewer var1, EditorService var2, ActiveObjectService var3, EntityPageDisplayer var4, Runnable var6
   ) {
      ProjectUiState var7 = var0.getUiState();
      setInitialTreeExpansionState(var7, var1);
      setInitiallyOpenedPages(var7, var2, var3, var4, var6);
   }

   private static void setInitialTreeExpansionState(ProjectUiState var0, ProjectContentViewer var1) {
      List var2 = var0.getExpandedOnStartUp();
      if (var2 != null && !var2.isEmpty()) {
         var1.setExpandedEntities(var2);
      }
   }

   private static void setInitiallyOpenedPages(
      ProjectUiState var0, final EditorService var1, final ActiveObjectService var2, EntityPageDisplayer var3, final Runnable var5
   ) {
      if (!var0.hasEntitiesToDisplayOnStartup()) {
         var5.run();
      } else {
         RestoreInitialPagesJob var6 = new RestoreInitialPagesJob(var3, var1, var0);
         var6.setUseProgressDialog(500);
         var6.addJobStatusListener(new JobStatusAdapter() {
            @Override
            public void jobEnded(JobStatusChangedEvent var1x) {
               InterchangeUiState.setActiveObject(var2, var1);
               var5.run();
            }
         });
         var6.submit();
      }
   }

   private static void setActiveObject(final ActiveObjectService var0, final EditorService var1) {
      Runnable var2 = new Runnable() {
         @Override
         public void run() {
            Editor var1x = var1.getActiveEditor();
            if (var1x != null) {
               EditorService var2x = var1;
               var0.setActiveObjects(var2x, Collections.singletonList(var1x.getObject()));
            }
         }
      };
      EventQueue.invokeLater(var2);
   }

   private InterchangeUiState() {
   }
}
