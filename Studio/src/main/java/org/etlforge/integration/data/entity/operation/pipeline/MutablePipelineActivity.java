package org.etlforge.integration.data.entity.operation.pipeline;

import com.google.common.base.Preconditions;
import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.etlforge.util.id.KongaGuid;

public class MutablePipelineActivity<T> implements PipelineActivity<T> {
   private final PipelineActivityType type;
   private final KongaGuid containerId;
   private T object;
   private final String role;
   private List<PipelineActivityListener> listeners;
   private boolean readOnly;

   public MutablePipelineActivity(PipelineActivityType var1, String var2) {
      this(var1, KongaGuid.createGuid(), null, var2);
   }

   public MutablePipelineActivity(PipelineActivityType var1, KongaGuid var2, String var3) {
      this(var1, var2, null, var3);
   }

   public MutablePipelineActivity(PipelineActivityType var1, T var2, String var3) {
      this(var1, KongaGuid.createGuid(), (T)var2, var3);
   }

   private MutablePipelineActivity(PipelineActivityType var1, KongaGuid var2, T var3, String var4) {
      this.type = Preconditions.checkNotNull(var1, "type");
      this.containerId = Preconditions.checkNotNull(var2, "containerId");
      this.role = var4;
      this.object = (T)var3;
      this.listeners = new CopyOnWriteArrayList<>();
   }

   @Override
   public void dispose() {
   }

   @Override
   public final PipelineActivityType getType() {
      return this.type;
   }

   @Override
   public final KongaGuid getId() {
      return this.containerId;
   }

   @Override
   public final String getRole() {
      return this.role;
   }

   @Override
   public final boolean isEmpty() {
      return this.object == null;
   }

   @Override
   public final void removeContent() {
      this.setContent(null);
   }

   public MutablePipelineActivity<T> clone() {
      try {
         MutablePipelineActivity var1 = (MutablePipelineActivity)super.clone();
         var1.listeners = new CopyOnWriteArrayList<>();
         return var1;
      } catch (CloneNotSupportedException var2) {
         throw new RuntimeException(var2);
      }
   }

   @Override
   public final void addListener(PipelineActivityListener var1) {
      this.listeners.add(var1);
   }

   @Override
   public final void removeListener(PipelineActivityListener var1) {
      this.listeners.remove(var1);
   }

   @Override
   public final void removeAllListeners() {
      this.listeners.clear();
   }

   @Override
   public final T getContent() {
      return this.object;
   }

   public void setContent(T var1) {
      if (this.object != var1) {
         this.object = (T)var1;
         this.fireContentChanged();
      }
   }

   @Override
   public boolean canHaveScriptedLinks() {
      return false;
   }

   @Override
   public final boolean isReadOnly() {
      return this.readOnly;
   }

   public final void setReadOnly(boolean var1) {
      this.readOnly = var1;
   }

   @Override
   public final boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (this.getClass().equals(var1.getClass())) {
         MutablePipelineActivity var2 = (MutablePipelineActivity)var1;
         return this.containerId.equals(var2.getId());
      } else {
         return false;
      }
   }

   @Override
   public final int hashCode() {
      return this.containerId.hashCode();
   }

   protected final void fireContentChanged() {
      PipelineActivityEvent var1 = new PipelineActivityEvent(this);

      for (PipelineActivityListener var3 : this.listeners) {
         var3.contentChanged(var1);
      }
   }

   protected final void fireObjectChanged(PropertyChangeEvent var1) {
      PipelineActivityEvent var2 = new PipelineActivityEvent(this, var1);

      for (PipelineActivityListener var4 : this.listeners) {
         var4.activityObjectChanged(var2);
      }
   }
}
