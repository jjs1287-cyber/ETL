package org.etlforge.integration.data.script;

import org.etlforge.integration.data.script.de.GlobalDataElements;

public interface DataElementAwareFunctionInserter extends FunctionInserter {
   void setDataElements(GlobalDataElements var1);
}
