package org.etlforge.integration.data.entity.operation.pipeline;

import com.google.common.base.Preconditions;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.util.id.KongaGuid;

public abstract class EntityActivity<T extends IntegrationEntity> extends MutablePipelineActivity<T> {
   private final Class<T> entityClass;
   private PropertyChangeListener entityListener;

   protected EntityActivity(PipelineActivityType var1, Class<T> var2, T var3, String var4) {
      super(var1, (T)var3, var4);
      this.entityClass = Preconditions.checkNotNull(var2, "entityClass");
      this.registerListener();
   }

   protected EntityActivity(PipelineActivityType var1, Class<T> var2, KongaGuid var3, String var4) {
      super(var1, var3, var4);
      this.entityClass = Preconditions.checkNotNull(var2, "entityClass");
   }

   protected EntityActivity(PipelineActivityType var1, Class<T> var2, String var3) {
      super(var1, var3);
      this.entityClass = Preconditions.checkNotNull(var2, "entityClass");
   }

   public abstract String getTypeName();

   public abstract String getTypeNamePlural();

   public final void setContent(T var1) {
      this.removeEntityListener();
      super.setContent((T)var1);
      this.registerListener();
   }

   public final void setContent(IntegrationEntityId var1, IntegrationEntityLookup var2) {
      this.setContent(var2.getEntity(var1, this.entityClass));
   }

   public EntityActivity<T> clone() {
      EntityActivity var1 = (EntityActivity)super.clone();
      var1.entityListener = null;
      var1.registerListener();
      return var1;
   }

   @Override
   public void dispose() {
      this.removeEntityListener();
      this.entityListener = null;
      this.removeAllListeners();
   }

   public final EntityLink getLinkToOperation(Operation var1) {
      return this.isEmpty() ? null : this.createOperationLink(var1);
   }

   protected abstract EntityLink createOperationLink(Operation var1);

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder("[").append(this.getRole()).append(": ");
      if (this.getContent() != null) {
         var1.append(this.getContent().getName());
      } else {
         var1.append("(empty)");
      }

      return var1.append("]").toString();
   }

   private void removeEntityListener() {
      if (this.entityListener != null) {
         IntegrationEntity var1 = this.getContent();
         if (var1 != null) {
            var1.removePropertyChangeListener(this.entityListener);
         }
      }
   }

   private void registerListener() {
      IntegrationEntity var1 = this.getContent();
      if (var1 != null) {
         if (this.entityListener == null) {
            this.entityListener = new EntityActivity.EntityChangeListener();
         }

         var1.addPropertyChangeListener(this.entityListener);
      }
   }

   private class EntityChangeListener implements PropertyChangeListener {
      private EntityChangeListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         if (this.isDeleted(var1)) {
            EntityActivity.this.setContent((T)null);
         } else {
            EntityActivity.this.fireObjectChanged(var1);
         }
      }

      private boolean isDeleted(PropertyChangeEvent var1) {
         return "Deleted".equals(var1.getPropertyName());
      }
   }
}
