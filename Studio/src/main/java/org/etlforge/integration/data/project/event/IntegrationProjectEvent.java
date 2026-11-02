package org.etlforge.integration.data.project.event;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.EventObject;
import java.util.List;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;

public class IntegrationProjectEvent extends EventObject {
   private final List<IntegrationEntity> entities;

   public IntegrationProjectEvent(IntegrationProject var1, IntegrationEntity var2) {
      this(var1, Collections.singletonList(var2));
   }

   public IntegrationProjectEvent(IntegrationProject var1, List<? extends IntegrationEntity> var2) {
      super(var1);
      this.entities = Lists.newArrayList(var2);
   }

   public IntegrationProject getSource() {
      return (IntegrationProject)super.getSource();
   }

   public List<IntegrationEntity> getEntities() {
      return Lists.newArrayList(this.entities);
   }

   public Iterable<IntegrationEntity> filter(Predicate<? super IntegrationEntity> var1) {
      return Iterables.filter(this.entities, var1);
   }

   public <T extends IntegrationEntity> Iterable<T> filter(Class<T> var1) {
      return Iterables.filter(this.entities, var1);
   }
}
