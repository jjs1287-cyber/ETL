package org.etlforge.ui.graph;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class RootedTreeGraphVertex<T> {
   private RootedTreeGraphVertex<T> previous;
   public final T userObject;
   private final LinkedHashSet<RootedTreeGraphVertex<T>> outgoing;

   public RootedTreeGraphVertex(T var1) {
      this((T)var1, null, null);
   }

   public RootedTreeGraphVertex(T var1, RootedTreeGraphVertex<T> var2, RootedTreeGraphVertex<T> var3) throws IllegalConnectionException {
      this.userObject = (T)var1;
      this.previous = var2;
      this.outgoing = new LinkedHashSet<>();
      if (var3 == null) {
         if (var2 != null) {
            var2.addNextNode(this);
         }
      } else {
         if (var2 == null) {
            if (var3.hasPreviousNode()) {
               throw new IllegalConnectionException();
            }
         } else {
            if (var3.previous != var2) {
               throw new IllegalConnectionException();
            }

            var2.removeNextNode(var3);
            this.setPrevious(var2);
            var2.addNextNode(this);
         }

         var3.setPrevious(this);
         this.addNextNode(var3);
      }
   }

   public List<RootedTreeGraphVertex<T>> getNextVertices() {
      return new ArrayList<>(this.outgoing);
   }

   public RootedTreeGraphVertex<T> getPreviousVertex() {
      return this.previous;
   }

   public T getUserObject() {
      return this.userObject;
   }

   public List<T> getNextUserObjects() {
      ArrayList var1 = new ArrayList();

      for (RootedTreeGraphVertex var3 : this.outgoing) {
         var1.add(var3.userObject);
      }

      return var1;
   }

   public T getPreviousUserObject() {
      return this.previous != null ? this.previous.userObject : null;
   }

   public boolean isConnectedTo(RootedTreeGraphVertex<T> var1) {
      if (var1 == null) {
         throw new NullPointerException("other");
      } else {
         return this.previous == var1 || var1.getPreviousVertex() == this;
      }
   }

   public void connect(RootedTreeGraphVertex<T> var1) throws IllegalConnectionException {
      if (!this.isConnectedTo(var1)) {
         if (var1.getPreviousVertex() != null) {
            throw new IllegalConnectionException("The second vertex is already connected to another vertex. It needs to be disconnected from that vertex first");
         }

         this.addNextNode(var1);
         var1.setPrevious(this);
      }
   }

   public void disconnect() {
      if (this.previous != null) {
         this.previous.removeNextNode(this);
         this.previous = null;
      }

      for (RootedTreeGraphVertex var2 : this.outgoing) {
         var2.setPrevious(null);
      }

      this.outgoing.clear();
   }

   public void disconnect(RootedTreeGraphVertex<T> var1) {
      if (var1.getPreviousVertex() == this) {
         var1.setPrevious(null);
         this.outgoing.remove(var1);
      }
   }

   private boolean hasPreviousNode() {
      return this.previous != null;
   }

   private void removeNextNode(RootedTreeGraphVertex<T> var1) {
      this.outgoing.remove(var1);
   }

   private void addNextNode(RootedTreeGraphVertex<T> var1) {
      this.outgoing.add(var1);
   }

   private void setPrevious(RootedTreeGraphVertex<T> var1) {
      this.previous = var1;
   }
}
