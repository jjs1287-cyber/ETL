package org.etlforge.integration.client.ui.entity.wizard;

import org.etlforge.integration.data.function.Function;

public interface FunctionCallTransformationFactory {
   boolean isEnabledFor(Function var1);

   void createTransformation(Function var1);
}
