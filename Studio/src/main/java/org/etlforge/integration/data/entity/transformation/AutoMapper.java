package org.etlforge.integration.data.entity.transformation;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class AutoMapper<S, T> {
   private final Function<S, String> sourceFieldNameFunction;
   private final Function<T, String> targetFieldNameFunction;
   private Map<String, T> lowerCaseTargetNameToField;
   private IdentityHashMap<S, String> sourceFieldToName;
   private IdentityHashMap<T, String> targetFieldToName;
   private List<AutoMapper.Match<S, T>> newMappings;

   public AutoMapper(Function<S, String> var1, Function<T, String> var2) {
      this.sourceFieldNameFunction = Preconditions.checkNotNull(var1);
      this.targetFieldNameFunction = Preconditions.checkNotNull(var2);
   }

   public List<AutoMapper.Match<S, T>> findMatches(Collection<S> var1, Collection<T> var2) {
      if (!var1.isEmpty() && !var2.isEmpty()) {
         this.newMappings = Lists.newArrayList();
         this.init(var2);
         this.lookForExactMatches(var1);
         this.lookForPartialMatches();
         return this.newMappings;
      } else {
         return Collections.emptyList();
      }
   }

   private void init(Collection<T> var1) {
      this.sourceFieldToName = Maps.newIdentityHashMap();
      this.targetFieldToName = Maps.newIdentityHashMap();
      this.lowerCaseTargetNameToField = Maps.newHashMap();

      for (Object var3 : var1) {
         String var4 = this.targetFieldNameFunction.apply((T)var3);
         this.targetFieldToName.put((T)var3, var4);
         this.lowerCaseTargetNameToField.put(var4.toLowerCase(), (T)var3);
      }
   }

   private void lookForExactMatches(Collection<S> var1) {
      for (Object var3 : var1) {
         String var4 = this.sourceFieldNameFunction.apply((S)var3);
         Object var5 = this.lowerCaseTargetNameToField.get(var4.toLowerCase());
         if (var5 == null) {
            this.sourceFieldToName.put((S)var3, var4);
         } else {
            this.targetFieldToName.remove(var5);
            this.newMappings.add(AutoMapper.Match.of((S)var3, (T)var5));
         }
      }
   }

   private void lookForPartialMatches() {
      AutoMapMatcher var1 = new AutoMapMatcher();
      Iterator var2 = this.sourceFieldToName.entrySet().iterator();

      while (var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         Object var4 = var3.getKey();
         String var5 = (String)var3.getValue();

         for (Entry var7 : this.targetFieldToName.entrySet()) {
            Object var8 = var7.getKey();
            String var9 = (String)var7.getValue();
            if (var1.isMatch(var5, var9)) {
               this.newMappings.add(AutoMapper.Match.of((S)var4, (T)var8));
               this.targetFieldToName.remove(var8);
               var2.remove();
               break;
            }
         }
      }
   }

   public static final class Match<S, T> {
      public final S source;
      public final T target;

      public static <S, T> AutoMapper.Match<S, T> of(S var0, T var1) {
         return new AutoMapper.Match<>((S)var0, (T)var1);
      }

      private Match(S var1, T var2) {
         this.source = (S)var1;
         this.target = (T)var2;
      }

      @Override
      public int hashCode() {
         return Objects.hashCode(this.source, this.target);
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         }

         if (!(var1 instanceof AutoMapper.Match)) {
            return false;
         }

         AutoMapper.Match var2 = (AutoMapper.Match)var1;
         return this.source.equals(var2.source) && this.target.equals(var2.target);
      }

      @Override
      public String toString() {
         return this.source + " --> " + this.target;
      }
   }
}
