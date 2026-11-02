package com.etlforge.integration.client.deploy.api;

import com.google.common.collect.Lists;
import java.util.List;

public interface ServerDeployCheckResult {
   ServerDeployCheckResult EMPTY = new ServerDeployCheckResult() {
      @Override
      public List<LastUpdateInfo> getLastUpdatedInfo() {
         return Lists.newArrayList();
      }

      @Override
      public List<SharedItemInfo> getSharedItems() {
         return Lists.newArrayList();
      }

      @Override
      public boolean isEmpty() {
         return true;
      }
   };

   List<SharedItemInfo> getSharedItems();

   List<LastUpdateInfo> getLastUpdatedInfo();

   boolean isEmpty();
}
