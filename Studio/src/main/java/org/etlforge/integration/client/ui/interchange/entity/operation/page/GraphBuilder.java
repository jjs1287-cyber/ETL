package org.etlforge.integration.client.ui.interchange.entity.operation.page;

import java.awt.EventQueue;
import java.io.IOException;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.entity.settings.EntityResources;
import org.etlforge.integration.client.entity.settings.EntitySettings;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPageLog;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.GraphLayoutStore;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.OperationGraph2D;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.OperationGraphConfig;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.prefs.EdgeTypePreference;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.ui.widget.scroll.ScrollPosition;
import org.etlforge.util.persist.Persistor;

final class GraphBuilder {
   private final Operation operation;
   private final OperationGraphConfig graphConfig;
   private final GraphLayoutStore layoutStore;

   public GraphBuilder(Operation var1, OperationGraphConfig var2, GraphLayoutStore var3) {
      this.operation = var1;
      this.graphConfig = var2;
      this.layoutStore = var3;
   }

   public OperationGraph2D build() {
      log("Starting to build the graph.");
      OperationGraph2D var1 = new OperationGraph2D(this.operation, this.graphConfig);
      log("Graph created.");
      this.restoreLayout(var1);
      this.restoreZoomLevel(var1);
      var1.selectStartOperation();
      var1.setEdgeTypePreference(EdgeTypePreference.instance());
      this.restoreScrollLocation(var1);
      return var1;
   }

   public void restoreLayout(OperationGraph2D var1) {
      try {
         try {
            if (this.layoutStore.restoreLayout(var1.getLayout())) {
               return;
            }
         } catch (IOException var6) {
            ErrorLog.attention(GraphBuilder.class, "Failed to restore the graph layout.", var6);
         }

         var1.getLayout().setDefaultExpansionState();
      } finally {
         log("Operation graph layout restored.");
      }
   }

   private void restoreScrollLocation(final OperationGraph2D var1) {
      final ScrollPosition var2 = this.layoutStore.restoreScrollPosition();
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            var1.restoreScrollLocation(var2);
         }
      });
   }

   private static void log(String var0) {
      IntegrationEntityPageLog.LOG.info(var0);
   }

   private void restoreZoomLevel(OperationGraph2D var1) {
      Persistor var2 = this.getOperationSettings();
      Double var3 = var2.getDouble("graph.zoom");
      if (var3 != null) {
         try {
            var1.setZoomLevel(var3);
         } catch (RuntimeException var5) {
         }
      }

      log("Zoom level restored.");
   }

   private Persistor getOperationSettings() {
      ManagedProject var1 = ProjectUtils.getManagedProject(this.operation);
      EntityResources var2 = var1.getEntityResources();
      EntitySettings var3 = var2.getSettings();
      return var3.getEntitySettings(this.operation);
   }
}
