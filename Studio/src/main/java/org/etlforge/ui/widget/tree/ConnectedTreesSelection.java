package org.etlforge.ui.widget.tree;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.ui.selection.Selection;

public final class ConnectedTreesSelection implements Selection {
   private final ConnectedTrees source;
   private final ImmutableList<Object> leftObjects;
   private final ImmutableList<Object> rightObjects;
   private ImmutableList<ConnectedTrees.Connection> possibleConnections;
   private ImmutableList<ConnectedTrees.Connection> currentConnections;

   public ConnectedTreesSelection(ConnectedTrees var1, List<Object> var2, List<Object> var3) {
      this.source = var1;
      this.leftObjects = ImmutableList.copyOf(var2);
      this.rightObjects = ImmutableList.copyOf(var3);
   }

   public ConnectedTrees getSource() {
      return this.source;
   }

   @Override
   public boolean isEmpty() {
      return this.leftObjects.isEmpty() && this.rightObjects.isEmpty();
   }

   @Override
   public Iterable<? extends Object> items() {
      ArrayList var1 = Lists.newArrayList(this.leftObjects);
      var1.addAll(this.rightObjects);
      return var1;
   }

   public ImmutableList<Object> getLeftObjects() {
      return this.leftObjects;
   }

   public Object getLeftObject() {
      return this.leftObjects.isEmpty() ? null : this.leftObjects.get(0);
   }

   public ImmutableList<Object> getRightObjects() {
      return this.rightObjects;
   }

   public Object getRightObject() {
      return this.rightObjects.isEmpty() ? null : this.rightObjects.get(0);
   }

   public ImmutableList<ConnectedTrees.Connection> getPossibleConnections() {
      if (this.possibleConnections == null) {
         ArrayList var1 = Lists.newArrayList();

         for (Object var3 : this.leftObjects) {
            for (Object var5 : this.rightObjects) {
               if (this.canBeConnected(var3, var5)) {
                  var1.add(new ConnectedTrees.Connection(this.source, var3, var5));
               }
            }
         }

         this.possibleConnections = ImmutableList.copyOf(var1);
      }

      return this.possibleConnections;
   }

   private boolean canBeConnected(Object var1, Object var2) {
      return !this.source.areConnected(var1, var2) && this.source.canConnect(var1, var2);
   }

   public void makePossibleConnections() {
      for (ConnectedTrees.Connection var3 : this.getPossibleConnections()) {
         var3.make();
      }
   }

   public boolean hasConnections() {
      if (this.currentConnections != null) {
         return !this.currentConnections.isEmpty();
      }

      for (Object var2 : this.leftObjects) {
         for (Object var4 : this.rightObjects) {
            if (this.source.areConnected(var2, var4)) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean canConnect() {
      return !this.getPossibleConnections().isEmpty();
   }

   public List<ConnectedTrees.Connection> getCurrentConnections() {
      if (this.currentConnections == null) {
         ArrayList var1 = Lists.newArrayList();

         for (Object var3 : this.leftObjects) {
            for (Object var5 : this.rightObjects) {
               if (this.source.areConnected(var3, var5)) {
                  var1.add(new ConnectedTrees.Connection(this.source, var3, var5));
               }
            }
         }

         this.currentConnections = ImmutableList.copyOf(var1);
      }

      return this.currentConnections;
   }

   public void breakAllConnections() {
      for (ConnectedTrees.Connection var3 : this.getCurrentConnections()) {
         var3.breakUp();
      }
   }
}
