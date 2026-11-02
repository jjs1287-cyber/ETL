package org.etlforge.integration.client.ui.script.text;

import java.util.List;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.script.Function;

public interface FunctionProjectItems {
   List<IntegrationEntity> getApplicableProjectItemArguments(Function var1);
}
