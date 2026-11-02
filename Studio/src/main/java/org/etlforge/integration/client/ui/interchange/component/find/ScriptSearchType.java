package org.etlforge.integration.client.ui.interchange.component.find;

import java.util.Collection;
import java.util.Collections;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Script;

public final class ScriptSearchType extends AbstractScriptBasedSearchType {
   public ScriptSearchType() {
      super("Scripts", EntityType.Script);
   }

   @Override
   protected Collection<String> getRawStrings(IntegrationEntity var1) {
      return Collections.singleton(((Script)var1).getScript());
   }
}
