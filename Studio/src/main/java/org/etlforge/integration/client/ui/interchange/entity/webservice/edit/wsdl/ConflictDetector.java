package org.etlforge.integration.client.ui.interchange.entity.webservice.edit.wsdl;

import com.google.common.base.Predicate;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.webservicecall.WebServiceInfo;

class ConflictDetector implements Predicate<WebServiceCall> {
   private final WebServiceCall wsCall;
   private final WebServiceInfo wsInfo;

   public ConflictDetector(WebServiceCall var1, WebServiceInfo var2) {
      this.wsCall = var1;
      this.wsInfo = var2;
   }

   public boolean apply(WebServiceCall var1) {
      return OpInfoFinder.findMatchingOpInfo(this.wsCall, this.wsInfo) == null;
   }
}
