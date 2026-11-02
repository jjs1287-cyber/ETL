package com.etlforge.integration.client.deploy.api;

import java.util.Date;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.user.UserId;

public interface LastUpdateInfo {
   IntegrationEntityId getId();

   UserId updatedBy();

   String getUserName();

   Date when();
}
