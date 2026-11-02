package org.etlforge.integration.database.info;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.etlforge.integration.database.BeginEndQuote;

public final class DatabaseInfo {
   private final ImmutableList<DatabaseObject> objects;
   private final BeginEndQuote quotes;

   public DatabaseInfo(List<DatabaseObject> var1, BeginEndQuote var2) {
      this.objects = ImmutableList.copyOf(var1);
      this.quotes = var2;
   }

   public ImmutableList<DatabaseObject> getObjects() {
      return this.objects;
   }

   public BeginEndQuote getBeginEndQuote() {
      return this.quotes;
   }
}
