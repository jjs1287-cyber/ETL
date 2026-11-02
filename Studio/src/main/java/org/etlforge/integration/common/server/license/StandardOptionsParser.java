package org.etlforge.integration.common.server.license;

import java.util.Map;
import org.etlforge.integration.common.server.ServerEdition;
import org.etlforge.integration.common.server.ServerEditionLookup;

public final class StandardOptionsParser implements ServerEditionLookup {
   public static final String TRIAL_KEY = "trial";
   public static final String EDITION_KEY = "edition";
   public static final String TRUE = "true";
   public static final String FALSE = "false";
   public static final String PROFESSIONAL_EDITION = "professional";
   public static final String ENTERPRISE_EDITION = "enterprise";
   private Map<String, String> keyValues;

   public void parse(String var1) {
      this.keyValues = new OptionsParser().parse(var1);
   }

   public boolean isTrial() {
      return this.hasValue("trial", "true");
   }

   private boolean hasValue(String var1, String var2) {
      String var3 = this.keyValues.get(var1);
      return var2.equalsIgnoreCase(var3);
   }

   public boolean isProfessional() {
      return this.hasValue("edition", "professional");
   }

   public boolean isEnterprise() {
      return this.hasValue("edition", "enterprise");
   }

   @Override
   public ServerEdition getServerEdition() {
      boolean var1 = this.isEnterprise();
      boolean var2 = this.isProfessional();
      boolean var3 = this.isTrial();
      if (var3) {
         if (var1) {
            return ServerEdition.ENTERPRISE_TRIAL;
         } else {
            return var2 ? ServerEdition.PROFESSIONAL_TRIAL : ServerEdition.TRIAL;
         }
      } else if (var1) {
         return ServerEdition.ENTERPRISE;
      } else {
         return var2 ? ServerEdition.PROFESSIONAL : ServerEdition.UNKNOWN;
      }
   }
}
