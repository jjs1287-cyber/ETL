package org.etlforge.integration.data.connector;

import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IdProperty;
import org.etlforge.integration.data.entity.NameProperty;
import org.etlforge.integration.data.entity.id.MSCrmUpsertId;
import org.etlforge.integration.mscrm.FunctionType;

public final class MSCrmUpsert extends MSCrmFunction {
   @IdProperty
   public static final String MSCRM_UPSERT_ID = "mscrmupsert_id";
   @NameProperty
   public static final String MSCRM_UPSERT_NAME = "mscrmupsert_name";
   public static final String MSCRM_UPSERT_KEY_SEPERATOR = ",";

   public MSCrmUpsert(String var1) {
      super(null, var1, EntityType.MSCrmUpsert);
   }

   public MSCrmUpsert(MSCrmUpsertId var1, String var2) {
      super(var1, var2, EntityType.MSCrmUpsert);
   }

   public MSCrmUpsertId getID() {
      return (MSCrmUpsertId)super.getID();
   }

   @Override
   public FunctionType getFunctionType() {
      return FunctionType.UPSERT;
   }

   @Override
   public String[] getPropertyKeys() {
      return this.getPropertyKeys("mscrmupsert_id", "mscrmupsert_name");
   }

   @Deprecated
   public List<String> getUpsertKeyNames() {
      ArrayList var1 = new ArrayList();
      synchronized (this.getDataLock()) {
         String var3 = this.getProperty("mscrmupsert_keys");
         if (var3 != null && var3.trim().length() != 0) {
            String[] var4 = var3.trim().split(",");
            if (var4 != null && var4.length != 0) {
               for (String var8 : var4) {
                  if (var8 != null && var8.trim().length() != 0) {
                     var1.add(var8.trim());
                  }
               }

               return var1;
            } else {
               return var1;
            }
         } else {
            return var1;
         }
      }
   }

   @Deprecated
   public void setUpsertKeyNames(List<String> var1) {
      synchronized (this.getDataLock()) {
         if (var1 != null && var1.size() != 0) {
            String var3 = "";

            for (int var4 = 0; var4 < var1.size(); var4++) {
               String var5 = (String)var1.get(var4);
               var3 = var3 + var5.trim();
               if (var4 != var1.size() - 1) {
                  var3 = var3 + ",";
               }
            }

            String var8 = this.getProperty("mscrmupsert_keys");
            this.setProperty("mscrmupsert_keys", var3.trim());
            if (!var3.equals(var8)) {
               this.firePropertyChange("mscrmupsert_keys", var8, var3);
            }
         }
      }
   }

   @Deprecated
   public String getUpsertRequestXml() {
      synchronized (this.getDataLock()) {
         return this.getProperty("mscrmupsert_keys");
      }
   }
}
