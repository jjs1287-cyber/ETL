package com.etlforge.integration.client.admin.project;

import java.util.List;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;

public interface DeleteIntegrationProjectResult {
   IntegrationProjectId getProjectId();

   boolean success();

   List<String> getWarnings();

   String getError();
}
