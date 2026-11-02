package org.etlforge.integration.data.script;

import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.EntityFilter;

public interface ExpressionSource {
   boolean originatesIn(IntegrationEntity var1);

   boolean matches(EntityFilter var1);
}
