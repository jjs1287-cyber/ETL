package org.etlforge.integration.client.ui.interchange.entity.operation.page;

import java.io.IOException;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.entity.settings.EntityResources;
import org.etlforge.integration.client.entity.settings.EntitySettings;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.GraphLayoutStore;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.OperationGraph2D;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.util.persist.Persistor;

final class GraphSettingsSaver {
   private final GraphLayoutStore layoutStore;

   public GraphSettingsSaver(GraphLayoutStore var1) {
      this.layoutStore = var1;
   }

   public void save(OperationGraph2D var1) {
      this.saveLayout(var1);
      this.saveZoomLevel(var1);
   }

   private void saveLayout(OperationGraph2D var1) {
      try {
         this.layoutStore.saveLayout(var1.getLayout());
      } catch (IOException var3) {
         ErrorLog.attention(OperationGraphPanel.class, "Failed to store the graph layout.", var3);
      }
   }

   private void saveZoomLevel(OperationGraph2D var1) {
      Persistor var2 = this.getOperationSettings(var1);
      var2.putDouble("graph.zoom", var1.getZoomLevel());
   }

   private Persistor getOperationSettings(OperationGraph2D var1) {
      Operation var2 = var1.getModel().getModeledFlow().getStartPoint().getActivity();
      ManagedProject var3 = ProjectUtils.getManagedProject(var2);
      EntityResources var4 = var3.getEntityResources();
      EntitySettings var5 = var4.getSettings();
      return var5.getEntitySettings(var2);
   }
}
