package org.etlforge.integration.server.db;

public final class JdbcDriverLookupFactory {
   public static JdbcDriverLookup newLookup() {
      return new JdbcDriverLookupImpl();
   }

   private JdbcDriverLookupFactory() {
   }
}
