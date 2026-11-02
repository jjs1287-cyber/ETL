package org.etlforge.integration.server.implementation.webservice.interchange.deploy.impl;

import com.etlforge.integration.client.deploy.api.SharedItemInfo;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.CIntegrationEntity;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.CProjectMembership;
import org.etlforge.integration.user.UserId;

final class SharedItemInfoImpl implements SharedItemInfo {
   private final IntegrationEntityId itemId;
   private final IntegrationProjectId projectId;
   private final String projectName;
   private final UserId owner;
   private final String ownerLogin;

   public SharedItemInfoImpl(CProjectMembership var1) {
      CIntegrationEntity var2 = var1.getItem();
      String var3 = var1.getProjectGuid();
      String var4 = var1.getOwnerGuid();
      this.itemId = EntityType.fromId(var2.getEntityTypeId()).getIntegrationId(var2.getId());
      this.projectId = new IntegrationProjectId(var3);
      this.projectName = var1.getProjectName();
      this.owner = new UserId(var4);
      this.ownerLogin = var1.getOwnerLoginName();
   }

   @Override
   public IntegrationEntityId getId() {
      return this.itemId;
   }

   @Override
   public IntegrationProjectId getOtherProjectId() {
      return this.projectId;
   }

   @Override
   public String getProjectName() {
      return this.projectName;
   }

   @Override
   public UserId getProjectOwner() {
      return this.owner;
   }

   @Override
   public String getProjectOwnerUserName() {
      return this.ownerLogin;
   }
}
