package com.etlforge.integration.client.deploy.api;

import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;
import org.etlforge.integration.user.UserId;

public interface SharedItemInfo {
   IntegrationEntityId getId();

   IntegrationProjectId getOtherProjectId();

   String getProjectName();

   UserId getProjectOwner();

   String getProjectOwnerUserName();
}
