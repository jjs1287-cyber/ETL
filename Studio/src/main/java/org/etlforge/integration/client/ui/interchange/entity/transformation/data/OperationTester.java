package org.etlforge.integration.client.ui.interchange.entity.transformation.data;

import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Transformation;

public interface OperationTester {
   void testOperation(Transformation var1);

   void testOperation(Operation var1);
}
