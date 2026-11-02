package org.etlforge.integration.data.entity.operation.pipeline;

import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.function.Function;

public interface FunctionCallPipelineBrowserFactory<T extends IntegrationEntity & Function, A extends EntityActivity<T>> {
   FunctionCallPipelineBrowser<T, A> createBrowser(OperationPipeline var1);
}
