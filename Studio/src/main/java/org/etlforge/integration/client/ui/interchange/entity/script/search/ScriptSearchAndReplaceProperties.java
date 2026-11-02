package org.etlforge.integration.client.ui.interchange.entity.script.search;

import org.etlforge.integration.client.entity.search.ReplaceableKeyValueStringProperty;
import org.etlforge.integration.client.entity.search.SearchAndReplaceMetaData;

public final class ScriptSearchAndReplaceProperties extends SearchAndReplaceMetaData {
   public ScriptSearchAndReplaceProperties() {
      super(new ReplaceableKeyValueStringProperty("Script", "script"));
   }
}
