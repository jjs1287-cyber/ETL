package org.etlforge.integration.data.entity;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.etlforge.system.Disposable;

public abstract class DeleteHandler implements Disposable {
   private final IntegrationEntity item;
   private final PropertyChangeListener listener;

   public DeleteHandler(IntegrationEntity var1) {
      this.item = var1;
      this.listener = new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            DeleteHandler.this.onDelete(DeleteHandler.this.item);
         }
      };
      var1.addPropertyChangeListener("Deleted", this.listener);
   }

   protected abstract void onDelete(IntegrationEntity var1);

   @Override
   public final void dispose() {
      this.item.removePropertyChangeListener("Deleted", this.listener);
   }
}
