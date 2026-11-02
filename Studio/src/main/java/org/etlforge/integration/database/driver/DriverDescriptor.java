package org.etlforge.integration.database.driver;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.database.BeginEndQuote;

public abstract class DriverDescriptor {
   private final String name;
   private final DriverType type;
   private final BeginEndQuote quotes;

   public DriverDescriptor(String var1, DriverType var2) {
      this(var1, var2, null);
   }

   public DriverDescriptor(String var1, DriverType var2, BeginEndQuote var3) {
      Preconditions.checkNotNull(var1, "name");
      Preconditions.checkNotNull(var2, "type");
      this.name = var1;
      this.type = var2;
      this.quotes = var3;
   }

   public final String getName() {
      return this.name;
   }

   public final DriverType getType() {
      return this.type;
   }

   public final BeginEndQuote getBeginEndQuotes() {
      return this.quotes;
   }

   public abstract String getConnectionString(ConnectionParams var1);

   public abstract void accept(DriverDescriptorVisitor var1);

   public abstract boolean isProbablyOracle();

   public abstract boolean isProbablyPostgreSQL();

   public abstract boolean isManualSqlStatementSupported();

   public void checkConnectionParams(ConnectionParams var1) throws InvalidConnectionParamsException {
      String var2 = var1.getServer();
      String var3 = var1.getDatabase();
      if (StringUtils.isBlank(var2) || StringUtils.isBlank(var3)) {
         throw new InvalidConnectionParamsException(var1, "Must specify a server and a database.");
      }
   }

   protected final boolean equalsDescriptor(DriverDescriptor var1) {
      return this.name.equals(var1.name) && this.type == var1.type && Objects.equal(this.quotes, var1.quotes);
   }

   @Override
   public String toString() {
      return this.name;
   }
}
