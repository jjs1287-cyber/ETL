package org.etlforge.integration.client.ui.interchange.content;

import java.awt.EventQueue;
import java.util.List;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class SelectionMemento {
   private final ProjectContentViewer viewer;
   private final List<IntegrationEntity> selection;

   public SelectionMemento(ProjectContentViewer var1) {
      this.viewer = var1;
      this.selection = var1.getSelectedEntities();
   }

   public void restore() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            if (!SelectionMemento.this.selection.isEmpty()) {
               SelectionMemento.this.viewer.selectEntity(SelectionMemento.this.selection.get(0));
            }
         }
      });
   }
}
