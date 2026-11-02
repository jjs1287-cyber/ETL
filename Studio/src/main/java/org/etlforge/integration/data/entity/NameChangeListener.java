package org.etlforge.integration.data.entity;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.etlforge.system.Disposable;
import org.etlforge.util.name.Name;

public abstract class NameChangeListener implements Disposable {
   private final IntegrationEntity item;
   private final PropertyChangeListener listener;

   public NameChangeListener(IntegrationEntity var1) {
      this.item = var1;
      this.listener = new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            NameChangeListener.this.nameChanged(((Name)var1.getOldValue()).getName(), ((Name)var1.getNewValue()).getName());
         }
      };
      var1.addPropertyChangeListener("Name", this.listener);
   }

   protected abstract void nameChanged(String var1, String var2);

   @Override
   public final void dispose() {
      this.item.removePropertyChangeListener("Name", this.listener);
   }
}
