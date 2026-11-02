package org.etlforge.integration.server.implementation.webservice.interchange.deploy.impl;

import com.google.common.collect.Lists;
import com.etlforge.integration.client.deploy.api.LastUpdateInfo;
import com.etlforge.integration.client.deploy.api.ServerDeployCheckResult;
import com.etlforge.integration.client.deploy.api.SharedItemInfo;
import java.util.List;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.CLastUpdate;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.CProjectMembership;

class DeployCheckResultImpl implements ServerDeployCheckResult {
   private final List<LastUpdateInfo> lastUpdateInfo = Lists.newArrayList();
   private final List<SharedItemInfo> sharedItems = Lists.newArrayList();

   public DeployCheckResultImpl(CLastUpdate[] var1, CProjectMembership[] var2) {
      if (var1 != null && var1.length > 0) {
         for (CLastUpdate var6 : var1) {
            this.lastUpdateInfo.add(new LastUpdateInfoImpl(var6));
         }
      }

      if (var2 != null && var2.length > 0) {
         for (CProjectMembership var10 : var2) {
            this.sharedItems.add(new SharedItemInfoImpl(var10));
         }
      }
   }

   @Override
   public List<LastUpdateInfo> getLastUpdatedInfo() {
      return this.lastUpdateInfo;
   }

   @Override
   public List<SharedItemInfo> getSharedItems() {
      return this.sharedItems;
   }

   @Override
   public boolean isEmpty() {
      return this.lastUpdateInfo.isEmpty() && this.sharedItems.isEmpty();
   }
}
