package org.etlforge.integration.data.entity.transformation;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.entity.link.LinkUtils;
import org.etlforge.integration.data.entity.link.WeakEntityLink;
import org.etlforge.integration.data.script.GrammarUtils;

class TransformationLinksFromScriptFactory {
   private final Transformation transformation;
   private final IntegrationEntityLookup dataLookup;

   public TransformationLinksFromScriptFactory(Transformation var1, IntegrationEntityLookup var2) {
      this.transformation = var1;
      this.dataLookup = var2;
   }

   public Collection<EntityLink> getLinks() {
      Set var1 = GrammarUtils.getAllIdsFromTransformation(this.transformation);
      HashSet var2 = Sets.newHashSet();

      for (Object __etlItem30 : var1) {
         IntegrationEntityId var4 = (IntegrationEntityId) __etlItem30;
         IntegrationEntity var5 = this.dataLookup.getEntity(var4);
         if (var5 != null) {
            var2.add(new WeakEntityLink(var5, this.transformation));
         } else {
            LinkUtils.logMissingLink(this.transformation, EntityType.None, var4);
         }
      }

      return var2;
   }
}
