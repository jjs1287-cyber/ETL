package org.etlforge.integration.salesforce.soap.header;

import java.util.List;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;
import org.etlforge.system.ArgCheck;

public final class QueryOptions extends SoapHeader {
   public static final String NAME = "QueryOptions";
   public static final String FIELD = "batchSize";
   public static final int MAX_BATCH_SIZE = 2000;
   private final int batchSize;

   public QueryOptions() {
      this(2000);
   }

   public QueryOptions(int var1) {
      super("QueryOptions");
      this.batchSize = ArgCheck.checkPositive(var1);
   }

   public int getBatchSize() {
      return this.batchSize;
   }

   @Override
   protected void collectMappings(List<SoapHeader.Mapping> var1) {
      var1.add(SoapHeader.Mapping.forInt("batchSize$", this.batchSize));
   }

   @Override
   public String getStringRepresentation() {
      return "" + this.batchSize;
   }

   public static QueryOptions fromString(String var0) {
      try {
         return new QueryOptions(Integer.parseInt(var0));
      } catch (Exception var2) {
         var2.printStackTrace();
         return new QueryOptions();
      }
   }

   @Override
   public int hashCode() {
      return this.batchSize;
   }

   @Override
   public boolean equals(Object var1) {
      return var1 == this ? true : var1 instanceof QueryOptions && this.batchSize == ((QueryOptions)var1).batchSize;
   }

   @Override
   public String toString() {
      return "Batch size; " + this.batchSize;
   }
}
