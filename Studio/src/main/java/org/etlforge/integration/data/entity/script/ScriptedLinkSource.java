package org.etlforge.integration.data.entity.script;

import java.util.List;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;

public interface ScriptedLinkSource {
   IntegrationEntityId getID();

   List<ScriptedLink> getScriptedLinks(IntegrationEntityLookup var1);

   void addScriptedLinkSourceListener(ScriptedLinkSourceListener var1);

   void removeScriptedLinkSourceListener(ScriptedLinkSourceListener var1);
}
