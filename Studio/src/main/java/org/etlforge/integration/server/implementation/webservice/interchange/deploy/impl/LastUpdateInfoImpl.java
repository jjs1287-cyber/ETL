package org.etlforge.integration.server.implementation.webservice.interchange.deploy.impl;

import com.etlforge.integration.client.deploy.api.LastUpdateInfo;
import java.util.Date;
import org.etlforge.integration.common.server.ServerConversionUtils;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.CIntegrationEntity;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.CLastUpdate;
import org.etlforge.integration.user.UserId;

class LastUpdateInfoImpl implements LastUpdateInfo {
   private final IntegrationEntityId id;
   private final UserId userId;
   private final String userLogin;
   private final Date lastUpdated;

   public LastUpdateInfoImpl(CLastUpdate var1) {
      CIntegrationEntity var2 = var1.getItem();
      String var3 = var1.getUserGuid();
      this.id = EntityType.fromId(var2.getEntityTypeId()).getIntegrationId(var2.getId());
      this.userId = new UserId(var3);
      this.userLogin = var1.getUserLogin();
      this.lastUpdated = this.getLastUpdated(var1);
   }

   @Override
   public IntegrationEntityId getId() {
      return this.id;
   }

   @Override
   public UserId updatedBy() {
      return this.userId;
   }

   @Override
   public String getUserName() {
      return this.userLogin;
   }

   @Override
   public Date when() {
      return this.lastUpdated;
   }

   private Date getLastUpdated(CLastUpdate var1) {
      String var2 = var1.getModifiedDate();
      return ServerConversionUtils.fromServerDateFormat(var2);
   }
}
