package org.etlforge.integration.data.entity.transformation;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.link.LinkUtils;
import org.etlforge.integration.data.entity.script.ScriptedLink;
import org.etlforge.integration.data.script.GrammarUtils;

public final class TransformationScriptLinkCollector {
   private final Transformation transformation;
   private final IntegrationEntityLookup lookup;

   public TransformationScriptLinkCollector(Transformation var1, IntegrationEntityLookup var2) {
      this.transformation = var1;
      this.lookup = var2;
   }

   public List<ScriptedLink> getLinks() {
      ArrayList var1 = Lists.newArrayList();

      for (IntegrationEntityId var3 : GrammarUtils.getAllIdsFromTransformation(this.transformation)) {
         IntegrationEntity var4 = this.lookup.getEntity(var3);
         if (var4 != null) {
            var1.add(new ScriptedLink(var4, this.transformation));
         } else {
            LinkUtils.logMissingLink(this.transformation, var3.getEntityType(), var3);
         }
      }

      return var1;
   }
}
