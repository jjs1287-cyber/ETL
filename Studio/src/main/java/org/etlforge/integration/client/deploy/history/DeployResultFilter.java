package org.etlforge.integration.client.deploy.history;

import com.google.common.base.Predicate;
import org.etlforge.integration.client.deploy.serverapi.DeployResult;

public interface DeployResultFilter extends Predicate<DeployResult> {
   DeployResultFilter ACCEPTS_ALL = new DeployResultFilter() {
      @Override
      public boolean apply(DeployResult var1) {
         return true;
      }
   };

   boolean apply(DeployResult var1);
}
