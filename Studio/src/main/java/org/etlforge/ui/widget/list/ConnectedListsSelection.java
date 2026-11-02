package org.etlforge.ui.widget.list;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.etlforge.ui.selection.Selection;

public final class ConnectedListsSelection implements Selection {
   private final ConnectedLists source;
   private final List<Object> leftObjects;
   private final List<Object> rightObjects;
   private List<ConnectedListsSelection.Connection> possibleConnections;
   private List<ConnectedListsSelection.Connection> currentConnections;

   public ConnectedListsSelection(ConnectedLists var1, Object[] var2, Object[] var3) {
      this.source = var1;
      this.leftObjects = Arrays.asList(var2);
      this.rightObjects = Arrays.asList(var3);
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

   public List<Object> getLeftObjects() {
      return Collections.unmodifiableList(this.leftObjects);
   }

   public Object getLeftObject() {
      return this.leftObjects.isEmpty() ? null : this.leftObjects.get(0);
   }

   public List<Object> getRightObjects() {
      return Collections.unmodifiableList(this.rightObjects);
   }

   public Object getRightObject() {
      return this.rightObjects.isEmpty() ? null : this.rightObjects.get(0);
   }

   public boolean canConnect() {
      if (this.possibleConnections != null) {
         return !this.possibleConnections.isEmpty();
      }

      for (Object var2 : this.leftObjects) {
         for (Object var4 : this.rightObjects) {
            if (this.canBeConnected(var2, var4)) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean canBeConnected(Object var1, Object var2) {
      return !this.source.areConnected(var1, var2) && this.source.canConnect(var1, var2);
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

   public List<ConnectedListsSelection.Connection> getPossibleConnections() {
      if (this.possibleConnections == null) {
         ArrayList var1 = Lists.newArrayList();

         for (Object var3 : this.leftObjects) {
            for (Object var5 : this.rightObjects) {
               if (this.canBeConnected(var3, var5)) {
                  var1.add(new ConnectedListsSelection.Connection(this.source, var3, var5));
               }
            }
         }

         this.possibleConnections = Collections.unmodifiableList(var1);
      }

      return this.possibleConnections;
   }

   public List<ConnectedListsSelection.Connection> getCurrentConnections() {
      if (this.currentConnections == null) {
         ArrayList var1 = Lists.newArrayList();

         for (Object var3 : this.leftObjects) {
            for (Object var5 : this.rightObjects) {
               if (this.source.areConnected(var3, var5)) {
                  var1.add(new ConnectedListsSelection.Connection(this.source, var3, var5));
               }
            }
         }

         this.currentConnections = Collections.unmodifiableList(var1);
      }

      return this.currentConnections;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.leftObjects, this.rightObjects);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof ConnectedListsSelection)) {
         return false;
      }

      ConnectedListsSelection var2 = (ConnectedListsSelection)var1;
      return Objects.equal(this.leftObjects, var2.leftObjects) && Objects.equal(this.rightObjects, var2.rightObjects);
   }

   public static class Connection {
      private final ConnectedLists lists;
      private final Object left;
      private final Object right;

      public Connection(ConnectedLists var1, Object var2, Object var3) {
         this.lists = var1;
         this.left = var2;
         this.right = var3;
      }

      public void make() {
         this.lists.addConnection(this.left, this.right);
      }

      public void breakUp() {
         this.lists.removeConnection(this.left, this.right);
      }
   }
}
