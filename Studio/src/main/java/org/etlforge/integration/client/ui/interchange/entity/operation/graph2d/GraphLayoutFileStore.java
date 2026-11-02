package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import org.etlforge.application.Application;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.widget.XmlFileViewer;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.entity.settings.EntityResources;
import org.etlforge.integration.client.entity.settings.FileResource;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.layout.OperationGraphLayout;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.ui.widget.scroll.ScrollPosition;
import org.etlforge.util.persist.XmlPersistor;
import org.xml.sax.SAXException;

public final class GraphLayoutFileStore implements GraphLayoutStore {
   private static final String LAYOUT_FILE_NAME = "GraphLayout.xml";
   private static final String SCROLL_POSITION_FILE_NAME = "ScrollPosition";
   private final FileResource layoutFile;
   private final FileResource scrollPositionFile;

   public GraphLayoutFileStore(Operation var1) {
      ManagedProject var2 = ProjectUtils.getManagedProject(var1);
      if (var2 == null) {
         throw new IllegalArgumentException("op is not part of a project");
      }

      EntityResources var3 = var2.getEntityResources();
      this.layoutFile = var3.getFileResource(var1, "GraphLayout.xml");
      this.scrollPositionFile = var3.getFileResource(var1, "ScrollPosition");
   }

   @Override
   public void saveLayout(OperationGraphLayout var1) throws IOException {
      XmlPersistor var2 = XmlPersistor.newRoot("GraphLayout");
      var1.writeTo(var2);
      this.saveLayout(var2);
   }

   public void saveLayout(XmlPersistor var1) throws IOException {
      this.layoutFile.write(var1.toXml());
   }

   @Override
   public boolean restoreLayout(OperationGraphLayout var1) throws IOException {
      String var2 = this.layoutFile.read();
      if (var2 == null) {
         return false;
      }

      try {
         XmlPersistor var3 = XmlPersistor.load(var2);
         var1.readFrom(var3);
         return true;
      } catch (SAXException var4) {
         throw new IOException("Failed to restore the layout: " + var4.getMessage(), var4);
      }
   }

   @Override
   public void saveScrollPosition(ScrollPosition var1) {
      try {
         this.scrollPositionFile.serialize(var1);
      } catch (Exception var3) {
         ErrorLog.internal(this.getClass(), "Failed to save the graph's current scroll position.", var3);
      }
   }

   @Override
   public ScrollPosition restoreScrollPosition() {
      try {
         ScrollPosition var1 = this.scrollPositionFile.deserialize(ScrollPosition.class);
         if (var1 != null) {
            return var1;
         }
      } catch (Exception var2) {
         ErrorLog.internal(this.getClass(), "Failed to restore the graph's initial scroll position.", var2);
      }

      return ScrollPosition.TOP_LEFT;
   }

   @Override
   public void delete() {
      this.layoutFile.delete();
      this.scrollPositionFile.delete();
   }

   @Override
   public void view(ApplicationWindow var1) {
      if (this.layoutFile.exists()) {
         this.openFile(var1);
      } else {
         Alert.error("The layout file is missing.", "Error");
      }
   }

   private void openFile(final ApplicationWindow var1) {
      ApplicationWorker.SafeRunnable var2 = new ApplicationWorker.SafeRunnable() {
         @Override
         public void run() {
            File var1x = GraphLayoutFileStore.this.createTempFile();
            if (var1x != null) {
               XmlFileViewer var2x = XmlFileViewer.getViewer(var1);
               var2x.viewFile(var1x);
            }
         }
      };
      ApplicationWorker var3 = Application.getWorker();
      var3.submitForParallel(var2);
   }

   private File createTempFile() {
      try {
         File var1 = File.createTempFile("oplayout", ".xml");
         var1.deleteOnExit();
         String var2 = this.layoutFile.read();
         Files.write(var2, var1, Charsets.UTF_8);
         return var1;
      } catch (IOException var3) {
         ErrorLog.attention(GraphLayoutFileStore.class, "Cannot open the graph layout file.", var3);
         return null;
      }
   }
}
