package org.etlforge.integration.salesforce;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;

public final class SalesforceEndpoint {
   public final String url;
   public final String sessionId;

   public SalesforceEndpoint(String var1, String var2) {
      Preconditions.checkArgument(StringUtils.isNotEmpty(var1), "url cannot be null or empty");
      Preconditions.checkArgument(StringUtils.isNotEmpty(var2), "sessionId cannot be null or empty");
      this.url = var1;
      this.sessionId = var2;
   }
}
