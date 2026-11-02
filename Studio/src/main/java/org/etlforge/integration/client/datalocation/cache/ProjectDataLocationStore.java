package org.etlforge.integration.client.datalocation.cache;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.DataLocationStore;
import org.etlforge.integration.data.project.IntegrationProject;

public final class ProjectDataLocationStore<T extends DataLocation> implements DataLocationStore<T> {
   private final Class<T> locClass;
   private final IntegrationProject project;
   private final EnumSet<SourceTarget> types;
   private final ProjectDataLocationStore<T>.Filter filter;
   private final ProjectDataLocationStore<T>.Converter converter;
   private T[] locations;

   private ProjectDataLocationStore(IntegrationProject var1, Class<T> var2, EnumSet<SourceTarget> var3) {
      this.project = Preconditions.checkNotNull(var1, "project");
      this.locClass = Preconditions.checkNotNull(var2, "clazz");
      this.types = Preconditions.checkNotNull(var3, "types");
      this.filter = new ProjectDataLocationStore.Filter();
      this.converter = new ProjectDataLocationStore.Converter();
   }

   public static <E extends DataLocation> ProjectDataLocationStore<E> allSourceLocations(IntegrationProject var0, Class<E> var1) {
      return new ProjectDataLocationStore<>(var0, var1, EnumSet.of(SourceTarget.Source));
   }

   public static <E extends DataLocation> ProjectDataLocationStore<E> allTargetLocations(IntegrationProject var0, Class<E> var1) {
      return new ProjectDataLocationStore<>(var0, var1, EnumSet.of(SourceTarget.Target));
   }

   public static <E extends DataLocation> ProjectDataLocationStore<E> allLocations(IntegrationProject var0, Class<E> var1) {
      return new ProjectDataLocationStore<>(var0, var1, EnumSet.allOf(SourceTarget.class));
   }

   @Override
   public T[] locations() {
      if (this.locations == null) {
         this.refresh();
      }

      return this.locations;
   }

   @Override
   public void refresh() {
      ArrayList var1 = new ArrayList();
      if (this.types.contains(SourceTarget.Source)) {
         this.collectLocations(Source.class, var1);
      }

      if (this.types.contains(SourceTarget.Target)) {
         this.collectLocations(Target.class, var1);
      }

      DataLocation[] var2 = (DataLocation[])Array.newInstance(this.locClass, var1.size());
      this.locations = (T[])var1.toArray(var2);
   }

   private void collectLocations(Class<? extends IntegrationEntity> var1, List<T> var2) {
      FluentIterable var3 = this.project.getAllEntitiesOfClass(var1).filter(this.filter).transform(this.converter);
      Iterables.addAll(var2, var3);
   }

   private final class Converter implements Function<IntegrationEntity, T> {
      private Converter() {
      }

      public T apply(IntegrationEntity var1) {
         return ProjectDataLocationStore.this.locClass.cast(((Locatable)var1).getLocation());
      }
   }

   private final class Filter implements EntityFilter {
      private Filter() {
      }

      @Override
      public boolean apply(IntegrationEntity var1) {
         DataLocation var2 = ((Locatable)var1).getLocation();
         return this.isValidLocation(var2);
      }

      private boolean isValidLocation(DataLocation var1) {
         return var1 != null && var1.getClass().equals(ProjectDataLocationStore.this.locClass);
      }
   }
}
