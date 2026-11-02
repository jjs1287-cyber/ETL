package org.etlforge.integration.salesforce;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Map;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.io.Indentation;

public final class DefaultPoolingConnectionFactory implements SalesforceConnectionFactory, PoolingConnectionFactory {
   private final SalesforceConnectionFactory underlyingConnectionFactory;
   private final Map<OrgIdentifier, PooledConnection> pooledConnections = Maps.newHashMap();
   private final Object lock = new Object();
   private final Pool pool = new Pool() {
      @Override
      public void removeIfSame(OrgIdentifier var1, SalesforceConnection var2) {
         synchronized (DefaultPoolingConnectionFactory.this.lock) {
            PooledConnection var4 = DefaultPoolingConnectionFactory.this.pooledConnections.get(var1);
            if (var4 == var2) {
               DefaultPoolingConnectionFactory.this.pooledConnections.remove(var1);
            }
         }
      }

      @Override
      public void remove(OrgIdentifier var1) {
         synchronized (DefaultPoolingConnectionFactory.this.lock) {
            DefaultPoolingConnectionFactory.this.pooledConnections.remove(var1);
         }
      }
   };

   public DefaultPoolingConnectionFactory(SalesforceConnectionFactory var1) {
      this.underlyingConnectionFactory = Preconditions.checkNotNull(var1, "underlyingConnectionFactory");
   }

   @Override
   public final SalesforceConnection newConnection(OrgIdentifier var1) {
      synchronized (this.lock) {
         PooledConnection var3 = this.getPooledConnection(var1);
         if (var3 != null) {
            return var3;
         }

         SalesforceConnection var4 = this.underlyingConnectionFactory.newConnection(var1);
         PooledConnection var5 = new PooledConnection(var4, this.pool, this.lock);
         this.pooledConnections.put(var1, var5);
         return var5;
      }
   }

   private final PooledConnection getPooledConnection(OrgIdentifier var1) {
      PooledConnection var2 = this.pooledConnections.get(var1);
      if (var2 != null && var2.isExpired()) {
         var2.terminate();
         var2 = null;
      }

      return var2;
   }

   @Override
   public final String getPooledConnectionsReport() {
      StringBuilder var1 = new StringBuilder("Salesforce Connection Pool Report:\n");
      synchronized (this.lock) {
         int var3 = this.pooledConnections.size();
         Indentation var4 = new Indentation().increase();
         if (var3 == 0) {
            var4.writeTo(var1);
            var1.append("No pooled connections.");
         } else {
            for (PooledConnection var6 : this.pooledConnections.values()) {
               var6.reportTo(var1, var4);
               var1.append(SystemUtils.LINE_SEPARATOR);
            }
         }
      }

      return var1.toString();
   }

   @Override
   public void forgetPooledConnections() {
      synchronized (this.lock) {
         this.pooledConnections.clear();
      }
   }
}
