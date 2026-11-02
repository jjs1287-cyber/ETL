package org.etlforge.integration.data.entity.operation.flow;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.data.entity.EmailMessage;
import org.etlforge.integration.data.entity.IdToObjectUtils;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityEvent;
import org.etlforge.integration.data.entity.operation.pipeline.ScriptedOperations;
import org.etlforge.integration.data.entity.operation.pipeline.event.OperationPipelineAdapter;
import org.etlforge.integration.data.entity.operation.pipeline.event.OperationPipelineEvent;
import org.etlforge.integration.data.entity.operation.pipeline.event.OperationPipelineListener;
import org.etlforge.integration.data.entity.operation.pipeline.event.PipelineActivityInsertedEvent;
import org.etlforge.integration.data.entity.operation.pipeline.event.PipelineActivityRemovedEvent;
import org.etlforge.integration.data.entity.script.ScriptedLink;
import org.etlforge.integration.data.entity.script.ScriptedLinkPredicates;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectUtils;
import org.etlforge.system.Disposable;
import org.etlforge.ui.util.UiUtils;

public final class OperationFlow implements Disposable {
   private OperationWrapper startPoint;
   private final IntegrationEntityLookup entityLookup;
   private final Map<Operation, OperationWrapper> operations;
   private final Map<EmailMessage, EmailWrapper> emails;
   private final List<OperationFlowListener> listeners;
   private final PropertyChangeListener opWrapperListener;
   private final OperationPipelineListener pipelineEventDispatcher;
   private final OperationFlow.OutsideModificationListener outsideModificationListener;
   private final List<OperationPipelineListener> pipelineListeners;
   private final OperationFlow.DeletionListener deletionListener;

   public OperationFlow(Operation var1) {
      this(var1, createEntityLookup(var1));
   }

   public OperationFlow(Operation var1, IntegrationEntityLookup var2) {
      this.entityLookup = var2;
      this.opWrapperListener = new OperationFlow.OperationWrapperListener();
      this.pipelineEventDispatcher = new OperationFlow.PipelineEventDispatcher();
      this.outsideModificationListener = new OperationFlow.OutsideModificationListener();
      this.pipelineListeners = new CopyOnWriteArrayList<>();
      this.deletionListener = new OperationFlow.DeletionListener();
      this.operations = Maps.newHashMap();
      this.emails = Maps.newHashMap();
      this.listeners = new CopyOnWriteArrayList<>();
      this.startPoint = this.getWrapperFor(var1);
      this.build(var1, new HashSet<>());
   }

   @Override
   public void dispose() {
      this.disposeOperations();
      this.disposeEmails();
      this.listeners.clear();
   }

   public OperationWrapper getStartPoint() {
      return this.startPoint;
   }

   public boolean isDirty() {
      for (OperationWrapper var2 : this.operations.values()) {
         if (var2.isDirty()) {
            return true;
         }
      }

      return false;
   }

   public List<OperationWrapper> getDirtyOperations() {
      ArrayList var1 = Lists.newArrayList();

      for (OperationWrapper var3 : this.operations.values()) {
         if (var3.isDirty()) {
            var1.add(var3);
         }
      }

      return var1;
   }

   public boolean contains(Operation var1) {
      return this.operations.containsKey(var1);
   }

   public void startSave() {
      this.outsideModificationListener.blocked = true;
   }

   public void saveComplete() {
      this.outsideModificationListener.blocked = false;
   }

   public void addFlowListener(OperationFlowListener var1) {
      this.listeners.add(var1);
   }

   public void removeFlowListener(OperationFlowListener var1) {
      this.listeners.remove(var1);
   }

   public void addPipelineListener(OperationPipelineListener var1) {
      this.pipelineListeners.add(var1);
   }

   public void removePipelineListener(OperationPipelineListener var1) {
      this.pipelineListeners.remove(var1);
   }

   public void deleteEmail(EmailWrapper var1) {
      if (this.emails.containsKey(var1.getActivity())) {
         for (Route var4 : var1.getIncomingRoutes()) {
            OperationWrapper var5 = var4.getFrom();
            if (var4.getType() == RouteType.SUCCESS) {
               var5.setSuccessEmailId(null);
            } else {
               var5.setFailureEmailId(null);
            }
         }

         List var6 = this.removeNodes(this.getUnreachableNodes());
         this.fireChange(var6);
      }
   }

   public void deleteOperation(OperationWrapper var1) {
      if (this.operations.containsKey(var1.getActivity())) {
         if (var1 == this.startPoint) {
            throw new UnsupportedOperationException("Cannot delete the start point of an operation flow");
         }

         List var2 = var1.getIncomingRoutes();
         if (Iterables.find(var2, RouteType.SCRIPTED, null) != null) {
            throw new UnsupportedOperationException("Cannot delete an operation that is triggered through a script");
         }

         for (Object __etlItem159 : var2) {
            Route var4 = (Route) __etlItem159;
            OperationWrapper var5 = var4.getFrom();
            if (var4.getType() == RouteType.SUCCESS) {
               var5.setSuccessOperationId(null);
            } else if (var4.getType() == RouteType.FAILURE) {
               var5.setFailureOperationId(null);
            } else if (var4.getType() == RouteType.SIBLING) {
               List var6 = var5.getSiblingIds();
               var6.remove(var1.getActivity().getID());
               var5.setSiblingIds(var6);
               var5.removeOutgoingRoute(var4);
            }
         }

         List var7 = this.removeNodes(this.getUnreachableNodes());
         this.fireChange(var7);
      }
   }

   public void deleteRoute(Route var1) {
      OperationWrapper var2 = var1.getFrom();
      Preconditions.checkArgument(this.operations.values().contains(var2), "route is not part of this flow");
      new OperationRouteDeleter(var2).deleteRoute(var1);
      ArrayList var3 = Lists.newArrayList();
      Set var4 = this.getUnreachableNodes();
      if (var4.isEmpty()) {
         var3.add(new RouteRemovedChange(var1));
      } else {
         var3.addAll(this.removeNodes(var4));
      }

      this.fireChange(var3);
   }

   private void setNextOperation(OperationWrapper var1, Operation var2, RouteType var3) {
      assert var3 == RouteType.SUCCESS || var3 == RouteType.FAILURE;
      ArrayList var4 = Lists.newArrayList();
      Route var5 = var1.findOutgoingRoute(RoutePredicates.toOperation(var3));
      OperationWrapper var6 = this.operations.get(var2);
      if (var6 != null) {
         if (var1.hasRouteOfTypeTo(var6, var3)) {
            return;
         }
      } else {
         OperationFlow.OperationInserter var7 = new OperationFlow.OperationInserter(var4);
         var6 = var7.insert(var2, var1);
      }

      Route var9 = new Route(var1, var6, var3);
      var1.addOutgoingRoute(var9);
      var4.add(new RouteInsertedChange(var9));
      if (var5 != null) {
         var1.removeOutgoingRoute(var5);
         Set var8 = this.getUnreachableNodes();
         if (var8.isEmpty()) {
            var4.add(new RouteRemovedChange(var5));
         } else {
            var4.addAll(this.removeNodes(var8));
         }
      }

      this.fireChange(var4);
   }

   private void removeNextOperation(OperationWrapper var1, RouteType var2) {
      assert var2 == RouteType.SUCCESS || var2 == RouteType.FAILURE;
      Route var3 = var1.findOutgoingRoute(RoutePredicates.toOperation(var2));
      if (var3 != null) {
         this.deleteRoute(var3);
      }
   }

   private void removeEmail(OperationWrapper var1, RouteType var2) {
      assert var2 == RouteType.SUCCESS || var2 == RouteType.FAILURE;
      Route var3 = var1.findOutgoingRoute(RoutePredicates.toEmail(var2));
      if (var3 != null) {
         this.deleteRoute(var3);
      }
   }

   private List<FlowChange> removeNodes(Collection<? extends OperationActivityWrapper> var1) {
      ArrayList var2 = Lists.newArrayList();

      for (OperationActivityWrapper var4 : var1) {
         var2.add(new NodeRemovedChange(var4));
         if (var4 instanceof OperationWrapper) {
            this.disposeOperationWrapper((OperationWrapper)var4);
         } else {
            this.disposeEmailWrapper((EmailWrapper)var4);
         }
      }

      return var2;
   }

   private void addEmailRoute(OperationWrapper var1, EmailMessage var2, RouteType var3) {
      assert var3 == RouteType.SUCCESS || var3 == RouteType.FAILURE;
      ArrayList var4 = Lists.newArrayList();
      EmailWrapper var5 = this.emails.get(var2);
      if (var5 != null) {
         if (var1.hasRouteOfTypeTo(var5, var3)) {
            return;
         }
      } else {
         var5 = this.insertEmail(var1, var2, var4);
      }

      Route var6 = var1.findOutgoingRoute(RoutePredicates.toEmail(var3));
      if (var6 != null) {
         var1.removeOutgoingRoute(var6);
         EmailWrapper var7 = (EmailWrapper)var6.getTo();
         if (this.isReachable(var7)) {
            var4.add(new RouteRemovedChange(var6));
         } else {
            var4.add(new NodeRemovedChange(var7));
            this.disposeEmailWrapper(var7);
         }
      }

      Route var8 = new Route(var1, var5, var3);
      var1.addOutgoingRoute(var8);
      var4.add(new RouteInsertedChange(var8));
      this.fireChange(var4);
   }

   private EmailWrapper insertEmail(OperationWrapper var1, EmailMessage var2, List<FlowChange> var3) {
      assert !this.emails.containsKey(var2);
      EmailWrapper var4 = new EmailWrapper(var2);
      this.emails.put(var2, var4);
      var3.add(new NodeInsertedChange(var4, var1));
      return var4;
   }

   private void disposeEmailWrapper(EmailWrapper var1) {
      this.emails.remove(var1.getActivity());
      var1.getActivity().removePropertyChangeListener("Deleted", this.deletionListener);
      var1.dispose();
   }

   private void disposeOperationWrapper(OperationWrapper var1) {
      this.operations.remove(var1.getActivity());
      var1.getPipeline().removePipelineListener(this.pipelineEventDispatcher);
      var1.getPipeline().removePipelineListener(this.outsideModificationListener);
      var1.removePropertyChangeListener(this.opWrapperListener);
      var1.getActivity().removePropertyChangeListener("Deleted", this.deletionListener);
      var1.dispose();
   }

   private Set<OperationActivityWrapper> getUnreachableNodes() {
      return new OperationFlow.Traverser().getUnreachableNodes();
   }

   private boolean isReachable(OperationActivityWrapper var1) {
      return new OperationFlow.Traverser().isReachable(var1);
   }

   private void fireChange(final List<? extends FlowChange> var1) {
      if (!var1.isEmpty()) {
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               OperationFlow.this.fireChange(new FlowChangeEvent(OperationFlow.this, var1));
            }
         });
      }
   }

   private void fireChange(FlowChangeEvent var1) {
      for (OperationFlowListener var3 : this.listeners) {
         var3.flowChanged(var1);
      }
   }

   private void disposeOperations() {
      for (OperationWrapper var2 : Lists.newArrayList(this.operations.values())) {
         this.disposeOperationWrapper(var2);
      }
   }

   private void disposeEmails() {
      for (EmailWrapper var2 : Lists.newArrayList(this.emails.values())) {
         this.disposeEmailWrapper(var2);
      }
   }

   private void build(Operation var1, Set<Operation> var2) {
      if (!var2.contains(var1)) {
         var2.add(var1);
         OperationWrapper var3 = this.getWrapperFor(var1);
         this.buildSiblings(var3, var2);
         this.buildScriptedRoutes(var3, var2);
         this.buildSuccessChain(var3, var2);
         this.buildFailureChain(var3, var2);
         this.checkSuccessEmail(var3);
         this.checkFailureEmail(var3);
      }
   }

   private void buildScriptedRoutes(OperationWrapper var1, Set<Operation> var2) {
      OperationPipeline var3 = var1.getPipeline();

      for (ScriptedLink var5 : var3.getScriptedLinks(this.entityLookup, ScriptedLinkPredicates.OPERATION)) {
         Operation var6 = (Operation)var5.getMainObject();
         this.link(var1, var6, RouteType.SCRIPTED);

         for (PipelineActivity var8 : var3.getActivitiesFor(var5.getDependantObject())) {
            Route var9 = Route.fromScriptSource(var8, var1, this.operations.get(var6));
            var1.addOutgoingRoute(var9);
         }

         this.build(var6, var2);
      }
   }

   private void buildSiblings(OperationWrapper var1, Set<Operation> var2) {
      for (Operation var4 : getSiblingOperations(var1.getActivity(), this.entityLookup)) {
         this.link(var1, var4, RouteType.SIBLING);
         this.build(var4, var2);
      }
   }

   private void buildSuccessChain(OperationWrapper var1, Set<Operation> var2) {
      Operation var3 = this.getSuccessOp(var1);
      if (var3 != null) {
         this.link(var1, var3, RouteType.SUCCESS);
         this.build(var3, var2);
      }
   }

   private void buildFailureChain(OperationWrapper var1, Set<Operation> var2) {
      Operation var3 = this.getFailureOp(var1);
      if (var3 != null) {
         this.link(var1, var3, RouteType.FAILURE);
         this.build(var3, var2);
      }
   }

   private void checkSuccessEmail(OperationWrapper var1) {
      EmailMessage var2 = this.getSuccessEmail(var1);
      if (var2 != null) {
         this.link(var1, var2, RouteType.SUCCESS);
      }
   }

   private void checkFailureEmail(OperationWrapper var1) {
      EmailMessage var2 = this.getFailureEmail(var1);
      if (var2 != null) {
         this.link(var1, var2, RouteType.FAILURE);
      }
   }

   private Operation getSuccessOp(OperationWrapper var1) {
      OperationId var2 = var1.getSuccessOperationId();
      return this.entityLookup.getEntity(var2, Operation.class);
   }

   private Operation getFailureOp(OperationWrapper var1) {
      OperationId var2 = var1.getFailureOperationId();
      return this.entityLookup.getEntity(var2, Operation.class);
   }

   private EmailMessage getSuccessEmail(OperationWrapper var1) {
      return this.entityLookup.getEntity(var1.getSuccessEmailId(), EmailMessage.class);
   }

   private EmailMessage getFailureEmail(OperationWrapper var1) {
      return this.entityLookup.getEntity(var1.getFailureEmailId(), EmailMessage.class);
   }

   private Route link(OperationWrapper var1, Operation var2, RouteType var3) {
      OperationWrapper var4 = this.getWrapperFor(var2);
      return this.link(var1, var4, var3);
   }

   private Route link(OperationWrapper var1, EmailMessage var2, RouteType var3) {
      EmailWrapper var4 = this.getWrapperFor(var2);
      return this.link(var1, var4, var3);
   }

   private Route link(OperationWrapper var1, OperationActivityWrapper var2, RouteType var3) {
      Route var4 = new Route(var1, var2, var3);
      var1.addOutgoingRoute(var4);
      return var4;
   }

   private OperationWrapper getWrapperFor(Operation var1) {
      OperationWrapper var2 = this.operations.get(var1);
      if (var2 == null) {
         var2 = new OperationWrapper(var1);
         var2.addPropertyChangeListener(this.opWrapperListener);
         var2.getPipeline().addPipelineListener(this.pipelineEventDispatcher);
         var2.getPipeline().addPipelineListener(this.outsideModificationListener);
         var1.addPropertyChangeListener("Deleted", this.deletionListener);
         this.operations.put(var1, var2);
      }

      return var2;
   }

   private OperationWrapper getWrapperFor(PipelineActivity var1) {
      for (OperationWrapper var3 : this.operations.values()) {
         if (var3.getPipeline().contains(var1)) {
            return var3;
         }
      }

      return null;
   }

   private EmailWrapper getWrapperFor(EmailMessage var1) {
      EmailWrapper var2 = this.emails.get(var1);
      if (var2 == null) {
         var2 = new EmailWrapper(var1);
         var1.addPropertyChangeListener("Deleted", this.deletionListener);
         this.emails.put(var1, var2);
      }

      return var2;
   }

   private static IntegrationEntityLookup createEntityLookup(Operation var0) {
      IntegrationProject var1 = ProjectUtils.getProject(var0);
      if (var1 == null) {
         throw new IllegalStateException("The operation does not belong to a project");
      } else {
         return var1.getItemLookup();
      }
   }

   private static Collection<Operation> getSiblingOperations(Operation var0, IntegrationEntityLookup var1) {
      return IdToObjectUtils.getEntitiesFromIds(var0.getSiblingOperations(), var1, Operation.class);
   }

   private List<FlowChange> rebuildLinksFrom(OperationWrapper var1, RouteType var2, Set<Operation> var3) {
      ArrayList var4 = Lists.newArrayList();
      List var5 = var1.findAllOutgoingRoutes(var2);
      ArrayList var6 = Lists.newArrayList();

      for (Object __etlItem497 : var5) {
         Route var8 = (Route) __etlItem497;
         if (!var3.contains(var8.getTo().getActivity())) {
            var6.add(var8);
            var1.removeOutgoingRoute(var8);
         }
      }

      for (Operation var13 : var3) {
         OperationWrapper var9 = this.operations.get(var13);
         if (var9 != null) {
            if (var1.hasRouteOfTypeTo(var9, var2)) {
               continue;
            }
         } else {
            var9 = new OperationFlow.OperationInserter(var4).insert(var13, var1);
         }

         Route var10 = new Route(var1, var9, var2);
         var1.addOutgoingRoute(var10);
         var4.add(new RouteInsertedChange(var10));
      }

      Set var12 = this.getUnreachableNodes();

      for (Object __etlItem521 : var6) {
         Route var15 = (Route) __etlItem521;
         if (!var12.contains(var15.getTo())) {
            var4.add(new RouteRemovedChange(var15));
         }
      }

      var4.addAll(this.removeNodes(var12));
      return var4;
   }

   private List<FlowChange> rebuildScriptedLinksFrom(OperationWrapper var1) {
      HashSet var2 = Sets.newHashSet(var1.findAllOutgoingRoutes(RoutePredicates.FROM_SCRIPT_SOURCE));

      for (Object __etlItem534 : var2) {
         Route var4 = (Route) __etlItem534;
         var1.removeOutgoingRoute(var4);
      }

      List var12 = this.rebuildLinksFrom(var1, RouteType.SCRIPTED, var1.getPipeline().getScriptedOperations(this.entityLookup).getOperations());
      HashSet var13 = Sets.newHashSet();
      OperationPipeline var5 = var1.getPipeline();

      for (ScriptedLink var7 : var5.getScriptedLinks(this.entityLookup, ScriptedLinkPredicates.OPERATION)) {
         Operation var8 = (Operation)var7.getMainObject();

         for (PipelineActivity var10 : var5.getActivitiesFor(var7.getDependantObject())) {
            Route var11 = Route.fromScriptSource(var10, var1, this.operations.get(var8));
            var13.add(var11);
            var1.addOutgoingRoute(var11);
         }
      }

      for (Object __etlItem552 : var2) {
         Route var16 = (Route) __etlItem552;
         if (!var13.contains(var16) && this.operations.containsKey(var16.getTo().getActivity())) {
            var12.add(new RouteRemovedChange(var16));
         }
      }

      for (Object __etlItem558 : var13) {
         Route var17 = (Route) __etlItem558;
         if (!var2.contains(var17)) {
            var12.add(new RouteInsertedChange(var17));
         }
      }

      return var12;
   }

   private List<FlowChange> rebuildSiblingsFrom(OperationWrapper var1) {
      HashSet var2 = Sets.newHashSet();

      for (OperationId var4 : var1.getSiblingIds()) {
         Operation var5 = this.entityLookup.getEntity(var4, Operation.class);
         if (var5 != null) {
            var2.add(var5);
         }
      }

      return this.rebuildLinksFrom(var1, RouteType.SIBLING, var2);
   }

   private class DeletionListener implements PropertyChangeListener {
      private DeletionListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         Object var2 = var1.getSource();
         if (var2 instanceof Operation) {
            OperationWrapper var3 = OperationFlow.this.operations.get(var2);

            for (Route var5 : var3.getIncomingRoutes()) {
               var5.getFrom().removeOutgoingRoute(var5);
               List var6 = OperationFlow.this.removeNodes(OperationFlow.this.getUnreachableNodes());
               OperationFlow.this.fireChange(var6);
            }
         } else if (var2 instanceof EmailMessage) {
            OperationFlow.this.deleteEmail(OperationFlow.this.emails.get(var2));
         }
      }
   }

   private class OperationInserter {
      private final List<FlowChange> changes;

      public OperationInserter(List<FlowChange> var2) {
         this.changes = var2;
      }

      public OperationWrapper insert(Operation var1, OperationWrapper var2) {
         assert !OperationFlow.this.operations.containsKey(var1);
         OperationWrapper var3 = OperationFlow.this.getWrapperFor(var1);
         this.changes.add(new NodeInsertedChange(var3, var2));
         this.linkScriptedOperations(var3);

         for (Operation var5 : OperationFlow.getSiblingOperations(var1, OperationFlow.this.entityLookup)) {
            this.link(var3, var5, RouteType.SIBLING);
         }

         Operation var8 = OperationFlow.this.getSuccessOp(var3);
         if (var8 != null) {
            this.link(var3, var8, RouteType.SUCCESS);
         }

         Operation var9 = OperationFlow.this.getFailureOp(var3);
         if (var9 != null) {
            this.link(var3, var9, RouteType.FAILURE);
         }

         EmailMessage var6 = OperationFlow.this.getSuccessEmail(var3);
         if (var6 != null) {
            this.link(var3, var6, RouteType.SUCCESS);
         }

         EmailMessage var7 = OperationFlow.this.getFailureEmail(var3);
         if (var7 != null) {
            this.link(var3, var7, RouteType.FAILURE);
         }

         return var3;
      }

      private void linkScriptedOperations(OperationWrapper var1) {
         ScriptedOperations var2 = var1.getPipeline().getScriptedOperations(OperationFlow.this.entityLookup);

         for (Operation var4 : var2.getOperations()) {
            this.link(var1, var4, RouteType.SCRIPTED);
            OperationWrapper var5 = OperationFlow.this.getWrapperFor(var4);

            for (PipelineActivity var7 : var2.getSources(var4)) {
               Route var8 = Route.fromScriptSource(var7, var1, var5);
               this.changes.add(new RouteInsertedChange(var8));
            }
         }
      }

      private void link(OperationWrapper var1, Operation var2, RouteType var3) {
         OperationWrapper var4 = OperationFlow.this.operations.get(var2);
         if (var4 == null) {
            var4 = this.insert(var2, var1);
         }

         Route var5 = OperationFlow.this.link(var1, var4, var3);
         this.changes.add(new RouteInsertedChange(var5));
      }

      private void link(OperationWrapper var1, EmailMessage var2, RouteType var3) {
         EmailWrapper var4 = OperationFlow.this.emails.get(var2);
         if (var4 == null) {
            var4 = OperationFlow.this.insertEmail(var1, var2, this.changes);
         }

         Route var5 = OperationFlow.this.link(var1, var4, var3);
         this.changes.add(new RouteInsertedChange(var5));
      }
   }

   private class OperationWrapperListener implements PropertyChangeListener {
      private OperationWrapperListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         String var2 = var1.getPropertyName();
         OperationWrapper var3 = (OperationWrapper)var1.getSource();
         if ("pipeline".equals(var2)) {
            List var4 = OperationFlow.this.rebuildScriptedLinksFrom(var3);
            var4.addAll(OperationFlow.this.rebuildSiblingsFrom(var3));
            var4.add(new NewOperationPipelineChange(var3));
            OperationFlow.this.fireChange(var4);
         } else if ("successOp".equals(var2)) {
            this.updateSuccessOperation(var3);
         } else if ("failureOp".equals(var2)) {
            this.updateFailureOperation(var3);
         } else if ("successEmail".equals(var2)) {
            this.updateSuccessMessage(var3);
         } else if ("failureEmail".equals(var2)) {
            this.updateFailureMessage(var3);
         } else if ("siblings".equals(var2)) {
            this.updateSiblingOperations(var3);
         }
      }

      private void updateSiblingOperations(OperationWrapper var1) {
         List var2 = var1.getSiblingIds();
         List var3 = KongaListUtils.filter(IdToObjectUtils.getEntitiesFromIds(var2, OperationFlow.this.entityLookup, Operation.class), Predicates.notNull());
         List var4 = OperationFlow.this.rebuildLinksFrom(var1, RouteType.SIBLING, Sets.newHashSet(var3));
         OperationFlow.this.fireChange(var4);
      }

      private void updateSuccessOperation(OperationWrapper var1) {
         Operation var2 = OperationFlow.this.getSuccessOp(var1);
         if (var2 != null) {
            this.setSuccessOperation(var1, var2);
         } else {
            this.removeSuccessOperation(var1);
         }
      }

      private void updateFailureOperation(OperationWrapper var1) {
         Operation var2 = OperationFlow.this.getFailureOp(var1);
         if (var2 != null) {
            this.setFailureOperation(var1, var2);
         } else {
            this.removeFailureOperation(var1);
         }
      }

      private void updateSuccessMessage(OperationWrapper var1) {
         EmailMessage var2 = OperationFlow.this.entityLookup.getEntity(var1.getSuccessEmailId(), EmailMessage.class);
         if (var2 != null) {
            this.setSuccessEmail(var1, var2);
         } else {
            this.removeSuccessEmail(var1);
         }
      }

      private void updateFailureMessage(OperationWrapper var1) {
         EmailMessage var2 = OperationFlow.this.entityLookup.getEntity(var1.getFailureEmailId(), EmailMessage.class);
         if (var2 != null) {
            this.setFailureEmail(var1, var2);
         } else {
            this.removeFailureEmail(var1);
         }
      }

      private void setSuccessOperation(OperationWrapper var1, Operation var2) {
         OperationFlow.this.setNextOperation(var1, var2, RouteType.SUCCESS);
         var1.setSuccessOperationId(var2.getID());
      }

      private void removeSuccessOperation(OperationWrapper var1) {
         OperationFlow.this.removeNextOperation(var1, RouteType.SUCCESS);
         var1.setSuccessOperationId(null);
      }

      private void setFailureOperation(OperationWrapper var1, Operation var2) {
         OperationFlow.this.setNextOperation(var1, var2, RouteType.FAILURE);
         var1.setFailureOperationId(var2.getID());
      }

      private void removeFailureOperation(OperationWrapper var1) {
         OperationFlow.this.removeNextOperation(var1, RouteType.FAILURE);
         var1.setFailureOperationId(null);
      }

      private void setSuccessEmail(OperationWrapper var1, EmailMessage var2) {
         OperationFlow.this.addEmailRoute(var1, var2, RouteType.SUCCESS);
         var1.setSuccessEmailId(var2.getID());
      }

      private void removeSuccessEmail(OperationWrapper var1) {
         OperationFlow.this.removeEmail(var1, RouteType.SUCCESS);
         var1.setSuccessEmailId(null);
      }

      private void setFailureEmail(OperationWrapper var1, EmailMessage var2) {
         OperationFlow.this.addEmailRoute(var1, var2, RouteType.FAILURE);
         var1.setFailureEmailId(var2.getID());
      }

      private void removeFailureEmail(OperationWrapper var1) {
         OperationFlow.this.removeEmail(var1, RouteType.FAILURE);
         var1.setFailureEmailId(null);
      }
   }

   private class OutsideModificationListener extends OperationPipelineAdapter {
      private boolean blocked;

      private OutsideModificationListener() {
      }

      @Override
      public void pipelineChanged(OperationPipelineEvent var1) {
         if (!this.blocked) {
            this.rebuild();
         }
      }

      private void rebuild() {
         Operation var1 = OperationFlow.this.startPoint.getActivity();
         OperationFlow.this.disposeOperations();
         OperationFlow.this.disposeEmails();
         OperationFlow.this.startPoint = OperationFlow.this.getWrapperFor(var1);
         OperationFlow.this.build(var1, new HashSet<>());
         OperationFlow.this.fireChange(Collections.singletonList(new FlowRebuiltChange()));
      }
   }

   private class PipelineEventDispatcher implements OperationPipelineListener {
      private PipelineEventDispatcher() {
      }

      @Override
      public void activityInserted(PipelineActivityInsertedEvent var1) {
         for (OperationPipelineListener var3 : OperationFlow.this.pipelineListeners) {
            var3.activityInserted(var1);
         }

         this.updateScriptedLinks(var1.getInsertedActivity());
      }

      @Override
      public void activityRemoved(PipelineActivityRemovedEvent var1) {
         for (OperationPipelineListener var3 : OperationFlow.this.pipelineListeners) {
            var3.activityRemoved(var1);
         }

         this.updateScriptedLinks(var1.getSource());
      }

      @Override
      public void pipelineChanged(OperationPipelineEvent var1) {
         for (OperationPipelineListener var3 : OperationFlow.this.pipelineListeners) {
            var3.pipelineChanged(var1);
         }
      }

      @Override
      public void activityObjectChanged(PipelineActivityEvent var1) {
         this.updateScriptedLinks(var1.getSource());

         for (OperationPipelineListener var3 : OperationFlow.this.pipelineListeners) {
            var3.activityObjectChanged(var1);
         }
      }

      @Override
      public void contentChanged(PipelineActivityEvent var1) {
         this.updateScriptedLinks(var1.getSource());

         for (OperationPipelineListener var3 : OperationFlow.this.pipelineListeners) {
            var3.contentChanged(var1);
         }
      }

      private void updateScriptedLinks(OperationPipeline var1) {
         OperationWrapper var2 = this.getOperationFor(var1);
         List var3 = OperationFlow.this.rebuildScriptedLinksFrom(var2);
         OperationFlow.this.fireChange(var3);
      }

      private OperationWrapper getOperationFor(OperationPipeline var1) {
         for (OperationWrapper var3 : OperationFlow.this.operations.values()) {
            if (var3.getPipeline() == var1) {
               return var3;
            }
         }

         return null;
      }

      private void updateScriptedLinks(PipelineActivity var1) {
         if (var1.canHaveScriptedLinks()) {
            OperationWrapper var2 = OperationFlow.this.getWrapperFor(var1);
            List var3 = OperationFlow.this.rebuildScriptedLinksFrom(var2);
            OperationFlow.this.fireChange(var3);
         }
      }
   }

   private class Traverser {
      private final Set<OperationActivityWrapper> reachable = Sets.newHashSet();

      public Traverser() {
         this.traverse(OperationFlow.this.startPoint);
      }

      public boolean isReachable(OperationActivityWrapper var1) {
         return this.reachable.contains(var1);
      }

      public Set<OperationActivityWrapper> getUnreachableNodes() {
         HashSet var1 = new HashSet<>(OperationFlow.this.operations.values());
         var1.addAll(OperationFlow.this.emails.values());
         var1.removeAll(this.reachable);
         return var1;
      }

      private void traverse(OperationActivityWrapper var1) {
         if (!this.reachable.contains(var1)) {
            this.reachable.add(var1);

            for (Route var3 : var1.getOutgoingRoutes()) {
               this.traverse(var3.getTo());
            }
         }
      }
   }
}
