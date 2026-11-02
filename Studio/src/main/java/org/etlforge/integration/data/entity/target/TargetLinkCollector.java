package org.etlforge.integration.data.entity.target;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.entity.link.LinkUtils;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.HttpLocation;

public final class TargetLinkCollector {
   private final Target target;
   private final IntegrationEntityLookup dataLookup;
   private final List<EntityLink> links;

   public TargetLinkCollector(Target var1, IntegrationEntityLookup var2) {
      Preconditions.checkNotNull(var1, "tgt");
      Preconditions.checkNotNull(var2, "lookup");
      this.target = var1;
      this.dataLookup = var2;
      this.links = Lists.newArrayList();
      this.collect();
   }

   private void collect() {
      DataLocation var1 = this.target.getLocation();
      if (var1 instanceof HttpLocation) {
         this.collectLinkToResponseTarget((HttpLocation)var1);
      }
   }

   private void collectLinkToResponseTarget(HttpLocation var1) {
      TargetId var2 = var1.getTargetIdForResponse();
      if (var2 != null) {
         Target var3 = this.dataLookup.getEntity(var2, Target.class);
         if (var3 != null) {
            this.links.add(new HttpTargetResponseLink(var3, this.target));
         } else {
            LinkUtils.logMissingLink(this.target, EntityType.Target, var2);
         }
      }
   }

   public List<? extends EntityLink> getLinks() {
      return this.links;
   }
}
