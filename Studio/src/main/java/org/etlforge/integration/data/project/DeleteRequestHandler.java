package org.etlforge.integration.data.project;

import java.util.List;
import org.etlforge.integration.data.entity.IntegrationEntity;

public interface DeleteRequestHandler {
   boolean deleteRequested(List<IntegrationEntity> var1);
}
