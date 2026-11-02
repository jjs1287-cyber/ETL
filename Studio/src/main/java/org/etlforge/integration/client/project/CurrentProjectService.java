package org.etlforge.integration.client.project;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.project.IntegrationProject;

public abstract class CurrentProjectService implements IntegrationEntityLookup {
   private final List<CurrentProjectServiceListener> listeners = Lists.newCopyOnWriteArrayList();

   @Nullable
   public abstract IntegrationProject getProject();

   @Nullable
   public abstract ManagedProject getManagedProject();

   public abstract void closeProject() throws CloseProjectVetoException;

   public final void addListener(CurrentProjectServiceListener var1) {
      this.listeners.add(Preconditions.checkNotNull(var1, "lst"));
   }

   public final void removeListener(CurrentProjectServiceListener var1) {
      this.listeners.remove(Preconditions.checkNotNull(var1, "lst"));
   }

   protected final void fireProjectOpened(IntegrationProject var1, ManagedProject var2) {
      CurrentProjectEvent var3 = new CurrentProjectEvent(this, var1, var2);

      for (CurrentProjectServiceListener var5 : this.listeners) {
         var5.projectOpened(var3);
      }
   }

   protected final void fireProjectClosed(IntegrationProject var1, ManagedProject var2) {
      CurrentProjectEvent var3 = new CurrentProjectEvent(this, var1, var2);

      for (CurrentProjectServiceListener var5 : this.listeners) {
         var5.projectClosed(var3);
      }
   }

   protected final void fireCloseProjectRequested(IntegrationProject var1) throws CloseProjectVetoException {
      for (CurrentProjectServiceListener var3 : this.listeners) {
         var3.closeProjectRequested(var1);
      }
   }

   @Override
   public final IntegrationEntity getEntity(IntegrationEntityId var1) {
      return this.getLookup().getEntity(var1);
   }

   private IntegrationEntityLookup getLookup() {
      IntegrationProject var1 = this.getProject();
      return var1 == null ? IntegrationEntityLookup.NULL : var1.getItemLookup();
   }

   @Override
   public final <T extends IntegrationEntity> T getEntity(IntegrationEntityId var1, Class<T> var2) {
      return this.getLookup().getEntity(var1, var2);
   }
}
