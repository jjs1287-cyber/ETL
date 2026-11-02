package org.etlforge.integration.client.ui.interchange.entity.webservice;

import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.uidelegate.entity.tooltip.IntegrationEntityToolTip;

public final class WebServiceCallToolTip extends IntegrationEntityToolTip {
   public WebServiceCallToolTip(WebServiceCall var1) {
      super(var1);
      this.addEntry("URL:", var1.getUrl());
   }
}
