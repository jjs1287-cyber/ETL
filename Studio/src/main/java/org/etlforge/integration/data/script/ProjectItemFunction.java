package org.etlforge.integration.data.script;

import com.google.common.base.Optional;
import java.util.List;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.project.IntegrationProject;

public interface ProjectItemFunction extends Function {
   Optional<? extends IntegrationEntityId> getItemId(String var1);

   List<IntegrationEntity> getApplicableProjectItems(IntegrationProject var1);
}
