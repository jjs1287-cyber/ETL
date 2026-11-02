package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d;

import java.io.IOException;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.layout.OperationGraphLayout;
import org.etlforge.ui.widget.scroll.ScrollPosition;

public interface GraphLayoutStore {
   GraphLayoutStore EMPTY = new GraphLayoutStore() {
      @Override
      public void view(ApplicationWindow var1) {
      }

      @Override
      public void saveLayout(OperationGraphLayout var1) {
      }

      @Override
      public boolean restoreLayout(OperationGraphLayout var1) {
         return false;
      }

      @Override
      public void saveScrollPosition(ScrollPosition var1) {
      }

      @Override
      public ScrollPosition restoreScrollPosition() {
         return ScrollPosition.TOP_LEFT;
      }

      @Override
      public void delete() {
      }
   };

   void saveLayout(OperationGraphLayout var1) throws IOException;

   boolean restoreLayout(OperationGraphLayout var1) throws IOException;

   void saveScrollPosition(ScrollPosition var1);

   ScrollPosition restoreScrollPosition();

   void delete();

   void view(ApplicationWindow var1);
}
