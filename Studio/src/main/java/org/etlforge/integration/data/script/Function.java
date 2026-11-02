package org.etlforge.integration.data.script;

import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.util.name.Nameable;
import org.etlforge.util.version.Version;

public interface Function extends Nameable {
   Version since();

   String getDisplayName();

   String getInsertValue();

   String getFormat();

   FCN getGrammar();

   String getDocumentation();

   boolean isInstanceFunction();

   boolean isReturnArray();

   boolean acceptsProjectItemAsInput(IntegrationEntity var1);

   boolean isHidden();
}
