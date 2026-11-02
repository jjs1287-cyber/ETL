package org.etlforge.integration.data.entity.operation.pipeline;

import com.google.common.base.Functions;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import org.etlforge.base.ListenerReport;
import org.etlforge.collections.Collections2;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.operation.OperationType;
import org.etlforge.integration.data.entity.operation.pipeline.event.OperationPipelineEvent;
import org.etlforge.integration.data.entity.operation.pipeline.event.OperationPipelineListener;
import org.etlforge.integration.data.entity.operation.pipeline.event.PipelineActivityInsertedEvent;
import org.etlforge.integration.data.entity.operation.pipeline.event.PipelineActivityRemovedEvent;
import org.etlforge.integration.data.entity.script.ScriptedLink;
import org.etlforge.integration.data.entity.script.ScriptedLinkPredicates;
import org.etlforge.integration.data.entity.script.ScriptedLinkSource;
import org.etlforge.system.Disposable;
import org.etlforge.system.Disposables;

public final class OperationPipeline implements Disposable {
   private OperationType type;
   private final List<PipelineActivity> activities;
   private final List<OperationPipelineListener> listeners;
   private ActivityInsertionPolicy insertionPolicy;
   private ActivityRemovingPolicy removingPolicy;
   private final Map<PipelineActivityPosition, PipelineActivity> activityPositions;

   public OperationPipeline(OperationPipeline var1) {
      this.type = var1.type;
      this.activities = Lists.newArrayList();

      for (PipelineActivity var3 : var1.getActivities()) {
         this.activities.add(var3.clone());
      }

      this.activityPositions = Maps.newHashMap();
      OperationPipeline.PositionBuilder.build(this);
      this.listeners = new CopyOnWriteArrayList<>();
      this.insertionPolicy = this.type.getInsertionPolicy(this);
      this.removingPolicy = this.type.getRemovingPolicy(this);
   }

   public OperationPipeline(OperationType var1, List<? extends PipelineActivity> var2) {
      this.type = var1;
      this.activities = Lists.newArrayList(var2);
      this.activityPositions = Maps.newHashMap();
      OperationPipeline.PositionBuilder.build(this);
      this.listeners = new CopyOnWriteArrayList<>();
      this.insertionPolicy = var1.getInsertionPolicy(this);
      this.removingPolicy = var1.getRemovingPolicy(this);
   }

   public OperationType getType() {
      return this.type;
   }

   @Override
   public void dispose() {
      Disposables.disposeAll(this.activities);
      this.activities.clear();
   }

   public PipelineActivityPosition getPosition(PipelineActivity var1) {
      for (Entry var3 : this.activityPositions.entrySet()) {
         if (var3.getValue() == var1) {
            return (PipelineActivityPosition)var3.getKey();
         }
      }

      throw new IllegalArgumentException("Asked for the position of a PipelineActivity that does not exist in this pipeline: " + var1);
   }

   public PipelineActivity getActivityAtPosition(PipelineActivityPosition var1) {
      return this.activityPositions.get(var1);
   }

   public List<PipelineActivity> getActivitiesFor(Object var1) {
      Preconditions.checkNotNull(var1, "content");
      ArrayList var2 = Lists.newArrayList();

      for (PipelineActivity var4 : this.activities) {
         if (var4.getContent() == var1) {
            var2.add(var4);
         }
      }

      return var2;
   }

   public ActivityInsertionPolicy getInsertionPolicy() {
      return this.insertionPolicy;
   }

   public PipelineActivity getFirstActivity() {
      return this.activities.get(0);
   }

   public PipelineActivity getLastActivity() {
      return this.activities.get(this.activities.size() - 1);
   }

   public <T extends PipelineActivity> T getFirstActivityOfType(Class<T> var1) {
      for (PipelineActivity var3 : this.activities) {
         if (var1.isInstance(var3)) {
            return (T)var1.cast(var3);
         }
      }

      return null;
   }

   public <T extends PipelineActivity> T getLastActivityOfType(Class<T> var1) {
      for (PipelineActivity var3 : Lists.reverse(this.getActivities())) {
         if (var1.isInstance(var3)) {
            return (T)var1.cast(var3);
         }
      }

      return null;
   }

   public int getActivityCount() {
      return this.activities.size();
   }

   public List<PipelineActivity> getActivities() {
      return Lists.newArrayList(this.activities);
   }

   public PipelineActivity getNextActivity(PipelineActivity var1) {
      int var2 = this.indexOf(var1);
      return var2 < this.activities.size() - 1 ? this.activities.get(var2 + 1) : null;
   }

   public PipelineActivity getPreviousActivity(PipelineActivity var1) {
      int var2 = this.indexOf(var1);
      return var2 > 0 ? this.activities.get(var2 - 1) : null;
   }

   public boolean contains(PipelineActivity var1) {
      return this.activities.contains(var1);
   }

   public List<PipelineActivity> getActivityRange(PipelineActivity var1, PipelineActivity var2) {
      int var3 = this.indexOf(var1);
      int var4 = this.indexOf(var2);
      if (var3 == var4) {
         return Collections.singletonList(var1);
      }

      if (var3 < var4) {
         return Lists.newArrayList(this.activities.subList(var3, var4 + 1));
      }

      ArrayList var5 = Lists.newArrayList(this.activities.subList(var4, var3 + 1));
      Collections.reverse(var5);
      return var5;
   }

   public ScriptedOperations getScriptedOperations(IntegrationEntityLookup var1) {
      OperationPipeline.ScriptedOperationCollector var2 = new OperationPipeline.ScriptedOperationCollector(var1);

      for (PipelineActivity var4 : this.getActivities()) {
         var2.visit(var4);
      }

      return var2.getCollectedOperations();
   }

   public List<ScriptedLink> getScriptedLinks(IntegrationEntityLookup var1, Predicate<ScriptedLink> var2) {
      OperationPipeline.ScriptedLinksCollector var3 = new OperationPipeline.ScriptedLinksCollector(var1, var2);
      return Lists.newArrayList(var3.visit(this));
   }

   private int indexOf(PipelineActivity var1) {
      int var2 = this.activities.indexOf(var1);
      Preconditions.checkArgument(var2 >= 0, "Not part of this pipeline: " + var1);
      return var2;
   }

   public void insertBetween(PipelineActivity var1, PipelineActivity var2, PipelineActivity var3) {
      Preconditions.checkArgument(this.getPreviousActivity(var3) == var2, "prev and next are not connected");
      this.insertActivity(var1, var2, var3, this.indexOf(var2) + 1);
   }

   public void insertBefore(PipelineActivity var1, PipelineActivity var2) {
      PipelineActivity var3 = this.getPreviousActivity(var2);
      if (var3 == null) {
         this.insertFirst(var1);
      } else {
         this.insertBetween(var1, var3, var2);
      }
   }

   public void insertAfter(PipelineActivity var1, PipelineActivity var2) {
      PipelineActivity var3 = this.getNextActivity(var2);
      if (var3 == null) {
         this.insertLast(var1);
      } else {
         this.insertBetween(var1, var2, var3);
      }
   }

   public void insertFirst(PipelineActivity var1) {
      this.insertActivity(var1, null, this.getFirstActivity(), 0);
   }

   public void insertLast(PipelineActivity var1) {
      this.insertActivity(var1, this.getLastActivity(), null, this.activities.size());
   }

   private void insertActivity(PipelineActivity var1, PipelineActivity var2, PipelineActivity var3, int var4) {
      Preconditions.checkNotNull(var1, "toInsert");
      Preconditions.checkArgument(!this.activities.contains(var1), "toInsert is already a part of this pipeline");
      this.activities.add(var4, var1);
      this.rebuildPipelineActivityPositions();

      for (OperationPipelineListener var6 : this.listeners) {
         var1.addListener(var6);
      }

      this.fireActivityInserted(var2, var3, var1);
   }

   public void removeActivity(PipelineActivity var1) throws RemoveActivityVetoException {
      Preconditions.checkNotNull(var1, "activity");
      Preconditions.checkArgument(this.activities.contains(var1), "activity is not part of this pipeline");
      this.removingPolicy.checkRemovalAllowed(var1);
      this.activities.remove(var1);
      this.rebuildPipelineActivityPositions();

      for (OperationPipelineListener var3 : this.listeners) {
         var1.removeListener(var3);
      }

      this.fireActivityRemoved(var1);
   }

   public void replaceWith(OperationPipeline var1) {
      if (!this.equals(var1)) {
         this.type = var1.type;
         this.insertionPolicy = var1.insertionPolicy;
         this.removingPolicy = var1.removingPolicy;
         this.dispose();

         for (PipelineActivity var3 : var1.getActivities()) {
            this.activities.add(var3.clone());
         }

         for (PipelineActivity var7 : this.activities) {
            for (OperationPipelineListener var5 : this.listeners) {
               var7.addListener(var5);
            }
         }

         this.rebuildPipelineActivityPositions();
         this.firePipelineChanged();
      }
   }

   public boolean hasPostConnectionPoint() {
      return this.type.hasPostConnectionPoint(this);
   }

   public void addPipelineListener(OperationPipelineListener var1) {
      this.listeners.add(var1);

      for (PipelineActivity var3 : this.getActivities()) {
         var3.addListener(var1);
      }
   }

   public void removePipelineListener(OperationPipelineListener var1) {
      this.listeners.remove(var1);

      for (PipelineActivity var3 : this.getActivities()) {
         var3.removeListener(var1);
      }
   }

   public List<OperationPipelineListener> getAllPipelineListeners() {
      return Lists.newArrayList(this.listeners);
   }

   public void reportListeners(ListenerReport var1) {
      for (OperationPipelineListener var3 : this.listeners) {
         var1.addListener("OperationPipelineListener", var3);
      }
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof OperationPipeline ? this.equals((OperationPipeline)var1) : false;
      }
   }

   private boolean equals(OperationPipeline var1) {
      if (this.type != var1.type) {
         return false;
      }

      int var2 = this.activities.size();
      if (var2 != var1.activities.size()) {
         return false;
      }

      for (int var3 = 0; var3 < var2; var3++) {
         PipelineActivity var4 = this.activities.get(var3);
         PipelineActivity var5 = var1.activities.get(var3);
         if (!var4.equals(var5)) {
            return false;
         }

         if (var4.isReadOnly() != var5.isReadOnly()) {
            return false;
         }

         if (var4.getContent() != var5.getContent()) {
            return false;
         }
      }

      return true;
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + this.type.hashCode();

      for (PipelineActivity var3 : this.activities) {
         var1 = 31 * var1 + var3.hashCode();
         Object var4 = var3.getContent();
         var1 = 31 * var1 + (var4 != null ? var4.hashCode() : 0);
      }

      return var1;
   }

   public void printToConsole() {
      System.out.println(Collections2.join(this.activities, " -> "));
   }

   private void rebuildPipelineActivityPositions() {
      this.activityPositions.clear();
      OperationPipeline.PositionBuilder.build(this);
   }

   private void fireActivityInserted(PipelineActivity var1, PipelineActivity var2, PipelineActivity var3) {
      if (!this.listeners.isEmpty()) {
         PipelineActivityInsertedEvent var4 = new PipelineActivityInsertedEvent(this, var1, var2, var3);

         for (OperationPipelineListener var6 : this.listeners) {
            var6.activityInserted(var4);
         }
      }
   }

   private void fireActivityRemoved(PipelineActivity var1) {
      if (!this.listeners.isEmpty()) {
         PipelineActivityRemovedEvent var2 = new PipelineActivityRemovedEvent(this, var1);

         for (OperationPipelineListener var4 : this.listeners) {
            var4.activityRemoved(var2);
         }
      }
   }

   private void firePipelineChanged() {
      if (!this.listeners.isEmpty()) {
         OperationPipelineEvent var1 = new OperationPipelineEvent(this);

         for (OperationPipelineListener var3 : this.listeners) {
            var3.pipelineChanged(var1);
         }
      }
   }

   @Override
   public String toString() {
      return this.type.name() + ": " + Collections2.join(this.activities, Functions.toStringFunction(), " -> ");
   }

   private static final class PositionBuilder {
      private final Multiset<PipelineActivityType> typeCount = HashMultiset.create();
      private final Map<PipelineActivityPosition, PipelineActivity> map;

      private PositionBuilder(Map<PipelineActivityPosition, PipelineActivity> var1) {
         this.map = var1;
      }

      public static void build(OperationPipeline var0) {
         OperationPipeline.PositionBuilder var1 = new OperationPipeline.PositionBuilder(var0.activityPositions);

         for (PipelineActivity var3 : var0.getActivities()) {
            var1.visit(var3);
         }
      }

      public void visit(PipelineActivity var1) {
         PipelineActivityType var2 = var1.getType();
         this.typeCount.add(var2);
         int var3 = this.typeCount.count(var2);
         this.map.put(new PipelineActivityPosition(var2.name(), var3), var1);
      }
   }

   private static class ScriptedLinksCollector {
      private Set<ScriptedLink> links;
      private final IntegrationEntityLookup lookup;
      private final Predicate<ScriptedLink> filter;

      public ScriptedLinksCollector(IntegrationEntityLookup var1, Predicate<ScriptedLink> var2) {
         this.lookup = var1;
         this.filter = var2;
      }

      public Set<ScriptedLink> visit(OperationPipeline var1) {
         this.links = Sets.newHashSet();

         for (PipelineActivity var3 : var1.activities) {
            Object var4 = var3.getContent();
            if (var4 instanceof ScriptedLinkSource) {
               this.collectScriptedLinks(var3, (ScriptedLinkSource)var4);
            }
         }

         return this.links;
      }

      private void collectScriptedLinks(PipelineActivity var1, ScriptedLinkSource var2) {
         for (ScriptedLink var4 : Iterables.filter(var2.getScriptedLinks(this.lookup), this.filter)) {
            IntegrationEntity var5 = var4.getMainObject();
            if (!var5.isDeleted()) {
               this.links.add(var4);
            }
         }
      }
   }

   private static class ScriptedOperationCollector {
      private final ScriptedOperations ops;
      private final IntegrationEntityLookup lookup;

      public ScriptedOperationCollector(IntegrationEntityLookup var1) {
         this.lookup = var1;
         this.ops = new ScriptedOperations();
      }

      public ScriptedOperations getCollectedOperations() {
         return this.ops;
      }

      public void visit(PipelineActivity var1) {
         Object var2 = var1.getContent();
         if (var2 instanceof ScriptedLinkSource) {
            this.collectScriptedLinks(var1, (ScriptedLinkSource)var2);
         }
      }

      private void collectScriptedLinks(PipelineActivity var1, ScriptedLinkSource var2) {
         for (ScriptedLink var4 : Iterables.filter(var2.getScriptedLinks(this.lookup), ScriptedLinkPredicates.OPERATION)) {
            Operation var5 = (Operation)var4.getMainObject();
            if (!var5.isDeleted()) {
               this.ops.add(var1, var5);
            }
         }
      }
   }
}
