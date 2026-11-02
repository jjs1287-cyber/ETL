package org.etlforge.integration.client.ui.entity.typeselector;

import org.etlforge.util.event.AbstractListenerManager;
import org.etlforge.util.event.ListenerManager;

public final class EntityTypeSelectionListeners
   extends AbstractListenerManager<EntityTypeSelectionListener>
   implements ListenerManager<EntityTypeSelectionListener> {
   protected EntityTypeSelectionListener createNotifier() {
      return new EntityTypeSelectionListener() {
         @Override
         public void selectionChanged(EntityTypeSelectionEvent var1) {
            for (EntityTypeSelectionListener var3 : EntityTypeSelectionListeners.this.getListeners()) {
               var3.selectionChanged(var1);
            }
         }
      };
   }
}
