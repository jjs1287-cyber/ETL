package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings.MappingErrorHandler;

public interface RefreshStructuresCallback {
   RefreshStructuresCallback IGNORE = new RefreshStructuresCallback() {
      @Override
      public void done() {
      }

      @Override
      public void cancelled() {
      }

      @Override
      public MappingErrorHandler getMappingErrorHandler() {
         return MappingErrorHandler.IGNORE;
      }
   };

   void done();

   void cancelled();

   MappingErrorHandler getMappingErrorHandler();
}
