package org.etlforge.integration.client.project.caseconverter;

import org.etlforge.util.string.Case;

public enum Conversion {
   SQL_SERVER_TO_ORACLE,
   ORACLE_TO_SQL_SERVER,
   ORACLE_TO_ORACLE;

   public Case getNewCase() {
      switch (this) {
         case SQL_SERVER_TO_ORACLE:
         case ORACLE_TO_ORACLE:
            return Case.UPPER;
         case ORACLE_TO_SQL_SERVER:
            return Case.LOWER;
         default:
            throw new AssertionError();
      }
   }

   public String from() {
      switch (this) {
         case SQL_SERVER_TO_ORACLE:
            return "SQL Server";
         case ORACLE_TO_ORACLE:
         case ORACLE_TO_SQL_SERVER:
            return "Oracle";
         default:
            throw new AssertionError();
      }
   }

   @Override
   public String toString() {
      switch (this) {
         case SQL_SERVER_TO_ORACLE:
            return "SQL Server to Oracle";
         case ORACLE_TO_ORACLE:
            return "Oracle to Oracle (change schema name)";
         case ORACLE_TO_SQL_SERVER:
            return "Oracle to SQL Server";
         default:
            throw new AssertionError();
      }
   }
}
