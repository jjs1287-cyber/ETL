package org.etlforge.integration.salesforce.wsdl;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.concurrent.ConcurrentMap;
import org.etlforge.lang.exception.ExceptionHandler;

public abstract class ObjectSchemaLookup implements Function<String, SObjectSchema> {
   private volatile boolean failed;
   private final ConcurrentMap<String, SObjectSchema> known = Maps.newConcurrentMap();
   private volatile ExceptionHandler<Throwable> errorReporter = ExceptionHandler.STANDARD_ERR;

   public static ObjectSchemaLookup fromSchemaProvider(final SObjectSchemaProvider var0) {
      return new ObjectSchemaLookup() {
         @Override
         protected SObjectSchema lookup(String var1) throws Exception {
            return var0.describeObject(var1);
         }
      };
   }

   public ObjectSchemaLookup() {
   }

   public ObjectSchemaLookup(SObjectSchema var1) {
      this.remember(var1);
   }

   private void remember(SObjectSchema var1) {
      this.known.put(var1.getName(), var1);
   }

   public void setErrorReporter(ExceptionHandler<Throwable> var1) {
      this.errorReporter = Preconditions.checkNotNull(var1);
   }

   public final SObjectSchema apply(String var1) {
      if (this.failed) {
         return null;
      }

      SObjectSchema var2 = this.known.get(var1);
      if (var2 != null) {
         return var2;
      }

      try {
         var2 = this.lookup(var1);
         if (var2 != null) {
            this.remember(var2);
         }

         return var2;
      } catch (Exception var4) {
         this.failed = true;
         this.errorReporter.caught(var4);
         return null;
      }
   }

   protected abstract SObjectSchema lookup(String var1) throws Exception;

   public final boolean isSuccess() {
      return !this.failed;
   }

   protected final void reset() {
      this.failed = false;
      this.known.clear();
   }
}
