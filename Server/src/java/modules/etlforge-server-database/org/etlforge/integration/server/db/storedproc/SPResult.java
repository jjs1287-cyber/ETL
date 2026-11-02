package org.etlforge.integration.server.db.storedproc;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;

public class SPResult {
   private final ImmutableList<String[]> resultSet;
   private final ImmutableList<SPParameter> outputPrms;
   private final String returnVal;
   private final int updateCount;

   public SPResult(List<String[]> var1, List<SPParameter> var2, String var3, int var4) {
      this.resultSet = ImmutableList.copyOf(var1);
      this.outputPrms = ImmutableList.copyOf(var2);
      this.returnVal = var3;
      this.updateCount = var4;
   }

   public List<String[]> GetResultSet() {
      return Lists.newArrayList(this.resultSet);
   }

   public List<SPParameter> GetOutputPrms() {
      return Lists.newArrayList(this.outputPrms);
   }

   public String GetReturnVal() {
      return this.returnVal;
   }

   public int GetUpdateCount() {
      return this.updateCount;
   }
}
