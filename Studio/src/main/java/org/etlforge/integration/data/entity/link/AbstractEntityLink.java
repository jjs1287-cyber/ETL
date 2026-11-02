package org.etlforge.integration.data.entity.link;

import com.google.common.base.Objects;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.etlforge.integration.data.entity.IntegrationEntity;

public abstract class AbstractEntityLink implements EntityLink {
   private final IntegrationEntity main;
   private final IntegrationEntity dependant;
   private boolean deleted;
   private final PropertyChangeListener itemListener;

   public AbstractEntityLink(IntegrationEntity var1, IntegrationEntity var2) {
      this.main = var1;
      this.dependant = var2;
      this.itemListener = new AbstractEntityLink.ItemListener();
      this.main.addPropertyChangeListener(this.itemListener);
      this.dependant.addPropertyChangeListener(this.itemListener);
   }

   @Override
   public IntegrationEntity getDependantObject() {
      return this.dependant;
   }

   @Override
   public IntegrationEntity getMainObject() {
      return this.main;
   }

   @Override
   public final IntegrationEntity getOtherObject(IntegrationEntity var1) {
      if (var1 == this.main) {
         return this.dependant;
      } else if (var1 == this.dependant) {
         return this.main;
      } else {
         throw new IllegalArgumentException(String.format("e [%s] is not part of this [%s] link", var1, this));
      }
   }

   @Override
   public void dispose() {
      if (!this.deleted) {
         this.main.removePropertyChangeListener(this.itemListener);
         this.dependant.removePropertyChangeListener(this.itemListener);
         this.deleted = true;
      }
   }

   protected void mainObjectChanged(PropertyChangeEvent var1) {
   }

   protected void mainObjectDeleted() {
      if (this.isStrong()) {
         this.dependant.delete();
      }

      this.dispose();
   }

   protected void dependantObjectChanged(PropertyChangeEvent var1) {
   }

   protected void dependantObjectDeleted() {
      this.dispose();
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof AbstractEntityLink)) {
         return false;
      }

      AbstractEntityLink var2 = (AbstractEntityLink)var1;
      return this.main.getID().equals(var2.main.getID()) && this.dependant.getID().equals(var2.dependant.getID());
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.main.getID(), this.dependant.getID());
   }

   @Override
   public String toString() {
      return this.main + " --> " + this.dependant;
   }

   private class ItemListener implements PropertyChangeListener {
      private ItemListener() {
      }

      @Override
      public final void propertyChange(PropertyChangeEvent var1) {
         Object var2 = var1.getSource();
         if ("Deleted".equals(var1.getPropertyName())) {
            this.handleDelete(var2);
         } else {
            this.handleNonDelete(var2, var1);
         }
      }

      private void handleDelete(Object var1) {
         if (var1 == AbstractEntityLink.this.main) {
            AbstractEntityLink.this.mainObjectDeleted();
         } else {
            AbstractEntityLink.this.dependantObjectDeleted();
         }
      }

      private void handleNonDelete(Object var1, PropertyChangeEvent var2) {
         if (var1 == AbstractEntityLink.this.main) {
            AbstractEntityLink.this.mainObjectChanged(var2);
         } else {
            AbstractEntityLink.this.dependantObjectChanged(var2);
         }
      }
   }
}
