package org.etlforge.integration.client.ui.interchange.entity.transformation.page;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.logging.Logger;
import org.etlforge.application.ui.window.AbstractWindowElementResource;
import org.etlforge.application.ui.window.WindowElement;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperException;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings.MappingErrorHandler;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings.MappingManager;
import org.etlforge.integration.data.entity.Transformation;

final class MappingsChangeListeningResource extends AbstractWindowElementResource {
   private MappingsChangeListeningResource.ListenerImpl listener;
   private Logger logger = Logger.getAnonymousLogger();

   public void setLogger(Logger var1) {
      this.logger = var1;
   }

   @Override
   public void create(WindowElement var1) {
      if (this.listener == null) {
         assert var1 instanceof TransformationPage;
         this.installListener((TransformationPage)var1);
      }
   }

   private void installListener(TransformationPage var1) {
      this.listener = new MappingsChangeListeningResource.ListenerImpl(var1, this.logger);
      var1.getObject().addPropertyChangeListener("Transformation#Mappings", this.listener);
   }

   @Override
   public void dispose(WindowElement var1) {
      if (this.listener != null) {
         assert var1 instanceof TransformationPage;
         this.removeListener((TransformationPage)var1);
      }
   }

   private void removeListener(TransformationPage var1) {
      var1.getObject().removePropertyChangeListener("Transformation#Mappings", this.listener);
      this.listener = null;
   }

   private static class ListenerImpl implements PropertyChangeListener {
      private final TreeMapper treeMapper;
      public final Logger logger;

      public ListenerImpl(TransformationPage var1, Logger var2) {
         this.treeMapper = var1.getTreeMapper();
         this.logger = var2;
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         try {
            Transformation var2 = this.treeMapper.getTransformation();
            List var3 = this.treeMapper.getTransformation().getMapList();
            this.logger.info(String.format("Updating the mapping tree for %s with new map list. Number of mappings: %d", var2.getName(), var3.size()));
            this.treeMapper.setMapList(var3, false);
            MappingManager var4 = this.treeMapper.getMappingManager();
            boolean var5 = var4.noSourceDataUsed();
            this.logger.info("The mapping manager says No Source Data Used: " + var5);
            this.treeMapper.getTransformation().setNoSourceDataUsedInMapping(var5);
         } catch (TreeMapperException var6) {
            this.logger.throwing(this.getClass().getName(), "propertyChange", var6);
            MappingErrorHandler.LOGGER.caught(var6);
         }
      }
   }
}
