package org.etlforge.integration.data.entity.httpendpoint;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.http.HttpVerb;
import org.etlforge.util.persist.Persistor;

public final class TriggeredOperations {
   private Map<HttpVerb, Set<TriggeredOperation>> operations;

   public TriggeredOperations() {
      this.operations = Maps.newHashMap();
   }

   public TriggeredOperations(TriggeredOperations var1) {
      this.operations = Maps.newHashMap(var1.operations);
   }

   public TriggeredOperations replaceOperations(Map<OperationId, OperationId> var1) {
      return new TriggeredOperations.Copier(this, var1).copy();
   }

   public void addOperation(TriggeredOperation var1) {
      addOperationImpl(var1, this.operations);
   }

   private static void addOperationImpl(TriggeredOperation var0, Map<HttpVerb, Set<TriggeredOperation>> var1) {
      Set var2 = (Set)var1.get(var0.getHttpVerb());
      if (var2 == null) {
         var2 = Sets.newLinkedHashSet();
         var1.put(var0.getHttpVerb(), var2);
      }

      var2.add(var0);
   }

   public LinkedHashSet<TriggeredOperation> getOperationsForVerb(HttpVerb var1) {
      Set var2 = this.operations.get(var1);
      return var2 != null ? Sets.newLinkedHashSet(var2) : new LinkedHashSet<>();
   }

   public Set<OperationId> getOperationIds() {
      HashSet var1 = Sets.newHashSet();

      for (Set var3 : this.operations.values()) {
         for (Object __etlItem56 : var3) {
            TriggeredOperation var5 = (TriggeredOperation) __etlItem56;
            var1.add(var5.getOperationId());
         }
      }

      return var1;
   }

   public boolean contains(Operation var1) {
      OperationId var2 = var1.getID();

      for (Set var4 : this.operations.values()) {
         for (Object __etlItem68 : var4) {
            TriggeredOperation var6 = (TriggeredOperation) __etlItem68;
            if (var6.getOperationId().equals(var2)) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean isEmpty() {
      for (Set var2 : this.operations.values()) {
         if (!var2.isEmpty()) {
            return false;
         }
      }

      return true;
   }

   public boolean isSynchronous(HttpVerb var1) {
      Set var2 = this.operations.get(var1);
      if (var2 == null) {
         return true;
      }

      for (Object __etlItem94 : var2) {
         TriggeredOperation var4 = (TriggeredOperation) __etlItem94;
         if (var4.getRunMode() == OperationRunMode.SYNCHRONOUS) {
            return true;
         }
      }

      return false;
   }

   public boolean removeOperation(OperationId var1) {
      boolean var2 = false;

      for (Set var4 : this.operations.values()) {
         Iterator var5 = var4.iterator();

         while (var5.hasNext()) {
            TriggeredOperation var6 = (TriggeredOperation)var5.next();
            if (var6.getOperationId().equals(var1)) {
               var5.remove();
               var2 = true;
            }
         }
      }

      return var2;
   }

   public OperationId getResponseGeneratingOperationId(HttpVerb var1) {
      Set var2 = this.operations.get(var1);
      if (var2 != null) {
         for (Object __etlItem124 : var2) {
            TriggeredOperation var4 = (TriggeredOperation) __etlItem124;
            if (var4.isResponseGenerator()) {
               return var4.getOperationId();
            }
         }
      }

      return null;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof TriggeredOperations ? this.operations.equals(((TriggeredOperations)var1).operations) : false;
      }
   }

   @Override
   public int hashCode() {
      return this.operations.hashCode();
   }

   public void writeTo(Persistor var1) {
      new TriggeredOperations.PersistorImpl(var1).write(this.operations);
   }

   public void restoreFrom(Persistor var1) {
      this.operations = new TriggeredOperations.PersistorImpl(var1).load();
   }

   private static class Copier {
      private final TriggeredOperations original;
      private TriggeredOperations copy;
      private final Map<OperationId, OperationId> clones;

      public Copier(TriggeredOperations var1, Map<OperationId, OperationId> var2) {
         this.original = var1;
         this.clones = var2;
      }

      public TriggeredOperations copy() {
         this.copy = new TriggeredOperations();

         for (HttpVerb var4 : HttpVerb.values()) {
            this.copyOperationsForVerb(var4);
         }

         this.copyOperationsForVerb(null);
         return this.copy;
      }

      private void copyOperationsForVerb(HttpVerb var1) {
         for (TriggeredOperation var3 : this.original.getOperationsForVerb(var1)) {
            OperationId var4 = var3.getOperationId();
            OperationId var5 = this.clones.get(var4);
            if (var5 != null) {
               this.copy.addOperation(var3.replaceOperationId(var5));
            } else {
               this.copy.addOperation(var3);
            }
         }
      }
   }

   private static class PersistorImpl {
      private static final String USE_REQUEST_AS_SOURCE = "useRequestAsSource";
      private static final String RESPONSE_GENERATOR = "responseGenerator";
      private static final String RUN_MODE = "mode";
      private static final String OPERATION_ID = "opId";
      private static final String OPERATION = "Operation";
      private static final String VERB_NAME = "name";
      private static final String VERB = "Verb";
      private final Persistor root;

      public PersistorImpl(Persistor var1) {
         this.root = var1;
      }

      public void write(Map<HttpVerb, Set<TriggeredOperation>> var1) {
         for (Entry var3 : var1.entrySet()) {
            Persistor var4 = this.root.createChild("Verb");
            if (var3.getKey() != null) {
               var4.putString("name", ((HttpVerb)var3.getKey()).name());
            }

            for (Object __etlItem211 : (Set)var3.getValue()) {
               TriggeredOperation var6 = (TriggeredOperation) __etlItem211;
               Persistor var7 = var4.createChild("Operation");
               var7.putString("opId", var6.getOperationId().toString());
               var7.putString("mode", var6.getRunMode().name());
               if (var6.isResponseGenerator()) {
                  var7.putBoolean("responseGenerator", true);
               }

               if (!var6.isUsingRequestAsSource()) {
                  var7.putBoolean("useRequestAsSource", false);
               }
            }
         }
      }

      public Map<HttpVerb, Set<TriggeredOperation>> load() {
         HashMap var1 = Maps.newHashMap();

         for (Persistor var3 : this.root.getChildren("Verb")) {
            try {
               String var4 = var3.getString("name");
               HttpVerb var5 = var4 != null ? HttpVerb.valueOf(var4) : null;

               for (Persistor var7 : var3.getChildren("Operation")) {
                  this.loadOperation(var1, var5, var7);
               }
            } catch (Exception var8) {
               ErrorLog.log(TriggeredOperations.class, "Invalid operation configuration in the HTTP Endpoint data.", var8);
            }
         }

         return var1;
      }

      private void loadOperation(Map<HttpVerb, Set<TriggeredOperation>> var1, HttpVerb var2, Persistor var3) {
         OperationId var4 = new OperationId(var3.getString("opId"));
         OperationRunMode var5 = OperationRunMode.valueOf(var3.getString("mode"));
         boolean var6 = readBoolean(var3, "responseGenerator", false);
         boolean var7 = readBoolean(var3, "useRequestAsSource", true);
         TriggeredOperation var8 = new TriggeredOperation(var4, var2, var5, var6, var7);
         TriggeredOperations.addOperationImpl(var8, var1);
      }

      private static boolean readBoolean(Persistor var0, String var1, boolean var2) {
         Boolean var3 = var0.getBoolean(var1);
         return var3 != null ? var3 : var2;
      }
   }
}
