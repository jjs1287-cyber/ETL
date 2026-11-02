package org.etlforge.integration.client.ui.interchange.content;

import java.awt.EventQueue;
import java.util.List;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class ExpansionMemento {
   private final ProjectContentViewer viewer;
   private final List<IntegrationEntity> expanded;

   public ExpansionMemento(ProjectContentViewer var1) {
      this.viewer = var1;
      this.expanded = var1.getExpandedEntities();
   }

   public void restore() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ExpansionMemento.this.viewer.setExpandedEntities(ExpansionMemento.this.expanded);
         }
      });
   }
}
