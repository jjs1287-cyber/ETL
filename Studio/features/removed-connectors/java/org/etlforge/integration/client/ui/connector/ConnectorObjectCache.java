package org.etlforge.integration.client.ui.connector;

import com.google.common.collect.ImmutableList;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import java.util.Arrays;
import java.util.List;
import org.etlforge.integration.data.connector.ConnectorEndpoint;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;

public interface ConnectorObjectCache {
   ConnectorObjectCache EMPTY = new ConnectorObjectCache() {
      @Override
      public List<ConnectorObject> getObjects(ConnectorObjectCache.Key var1) {
         return ImmutableList.of();
      }

      @Override
      public void storeObjects(ConnectorObjectCache.Key var1, Iterable<? extends ConnectorObject> var2) {
      }

      @Override
      public void remove(ConnectorObjectCache.Key var1) {
      }

      @Override
      public ImmutableList<String> evictOldEntries() {
         return ImmutableList.of();
      }
   };

   List<ConnectorObject> getObjects(ConnectorObjectCache.Key var1);

   void storeObjects(ConnectorObjectCache.Key var1, Iterable<? extends ConnectorObject> var2);

   void remove(ConnectorObjectCache.Key var1);

   ImmutableList<String> evictOldEntries();

   final class Key {
      private final ConnectorEndpoint endpoint;
      private final HashCode fingerprint;

      public Key(ConnectorEndpoint var1, ConnectorObject... var2) {
         this(var1, Arrays.asList(var2));
      }

      public Key(ConnectorEndpoint var1, Iterable<ConnectorObject> var2) {
         this.endpoint = var1;
         this.fingerprint = buildFingerprint(var1, var2);
      }

      public ConnectorEndpoint getConnectorEndpoint() {
         return this.endpoint;
      }

      private static HashCode buildFingerprint(ConnectorEndpoint var0, Iterable<ConnectorObject> var1) {
         HashFunction var2 = Hashing.md5();
         Hasher var3 = var2.newHasher();
         var0.contributeToCachingKey(var3);

         for (ConnectorObject var5 : var1) {
            var3.putString(var5.getName());
         }

         return var3.hash();
      }

      public HashCode fingerprint() {
         return this.fingerprint;
      }

      @Override
      public int hashCode() {
         return this.fingerprint.hashCode();
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else {
            return var1 instanceof ConnectorObjectCache.Key ? this.fingerprint.equals(((ConnectorObjectCache.Key)var1).fingerprint) : false;
         }
      }

      @Override
      public String toString() {
         return this.fingerprint.toString();
      }
   }
}
