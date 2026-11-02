package org.etlforge.integration.data.entity.operation.flow;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.system.Disposable;
import org.etlforge.util.event.PropertyChangeEventSender;

public abstract class OperationActivityWrapper implements PropertyChangeEventSender, Disposable {
   private final OperationFlowActivity activity;
   private final List<Route> incoming;
   private final List<Route> outgoing;
   private final PropertyChangeSupport changeSupport;

   OperationActivityWrapper(OperationFlowActivity var1) {
      this.activity = Preconditions.checkNotNull(var1);
      this.changeSupport = new PropertyChangeSupport(this);
      this.incoming = Lists.newArrayList();
      this.outgoing = Lists.newArrayList();
   }

   public OperationFlowActivity getActivity() {
      return this.activity;
   }

   public final boolean hasIncomingRoutes() {
      return !this.incoming.isEmpty();
   }

   public final List<Route> getIncomingRoutes() {
      return Lists.newArrayList(this.incoming);
   }

   public final List<Route> getOutgoingRoutes() {
      return Lists.newArrayList(this.outgoing);
   }

   final void addOutgoingRoute(Route var1) {
      Preconditions.checkArgument(var1.getFrom() == this, "out does not originate in this OperationActivityWrapper");
      if (!this.outgoing.contains(var1)) {
         this.outgoing.add(var1);
         var1.getTo().incoming.add(var1);
      }
   }

   public boolean removeOutgoingRoute(Route var1) {
      return this.outgoing.remove(var1);
   }

   public final boolean hasRouteOfTypeTo(OperationActivityWrapper var1, RouteType var2) {
      for (Route var4 : this.outgoing) {
         if (var4.getTo() == var1 && var4.getType() == var2) {
            return true;
         }
      }

      return false;
   }

   public final Route findOutgoingRoute(Predicate<Route> var1) {
      return Iterables.getFirst(Iterables.filter(this.outgoing, var1), null);
   }

   public final List<Route> findAllOutgoingRoutes(Predicate<Route> var1) {
      return KongaListUtils.filter(this.outgoing, var1);
   }

   public final Route findIncomingRoute(Predicate<Route> var1) {
      return Iterables.getFirst(Iterables.filter(this.incoming, var1), null);
   }

   public final List<Route> findAllIncomingRoutes(Predicate<Route> var1) {
      return KongaListUtils.filter(this.incoming, var1);
   }

   final List<Route> remove() {
      ArrayList var1 = Lists.newArrayList(this.incoming);

      for (Route var3 : this.incoming) {
         OperationWrapper var4 = var3.getFrom();
         if (var4 != this) {
            ((OperationActivityWrapper)var4).outgoing.remove(var3);
         }
      }

      this.incoming.clear();
      var1.addAll(this.outgoing);

      for (Route var6 : this.outgoing) {
         OperationActivityWrapper var7 = var6.getTo();
         if (var7 != this) {
            var7.incoming.remove(var6);
         }
      }

      this.outgoing.clear();
      return var1;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof OperationActivityWrapper ? this.activity.getID().equals(((OperationActivityWrapper)var1).activity.getID()) : false;
      }
   }

   @Override
   public int hashCode() {
      return this.activity.getID().hashCode();
   }

   @Override
   public String toString() {
      return this.activity.getName();
   }

   @Override
   public final void addPropertyChangeListener(PropertyChangeListener var1) {
      this.changeSupport.addPropertyChangeListener(var1);
   }

   @Override
   public final void addPropertyChangeListener(String var1, PropertyChangeListener var2) {
      this.changeSupport.addPropertyChangeListener(var1, var2);
   }

   @Override
   public final void removePropertyChangeListener(PropertyChangeListener var1) {
      this.changeSupport.removePropertyChangeListener(var1);
   }

   @Override
   public final void removePropertyChangeListener(String var1, PropertyChangeListener var2) {
      this.changeSupport.removePropertyChangeListener(var1, var2);
   }

   protected final void fireChange(String var1, Object var2, Object var3) {
      if (areDifferent(var2, var3)) {
         this.changeSupport.firePropertyChange(var1, var2, var3);
      }
   }

   private static boolean areDifferent(Object var0, Object var1) {
      return var0 == null ? var1 != null : !var0.equals(var1);
   }
}
