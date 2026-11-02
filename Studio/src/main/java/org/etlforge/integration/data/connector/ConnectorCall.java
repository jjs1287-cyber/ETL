package org.etlforge.integration.data.connector;

import org.etlforge.integration.data.function.Function;
import org.etlforge.integration.data.structure.XsdBasedInputStructure;
import org.etlforge.integration.data.structure.XsdBasedOutputStructure;

public interface ConnectorCall extends Function {
   XsdBasedInputStructure getInputStructure();

   XsdBasedOutputStructure getOutputStructure();
}
