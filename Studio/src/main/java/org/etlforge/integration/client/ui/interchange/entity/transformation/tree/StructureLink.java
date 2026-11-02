package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import com.google.common.base.Objects;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class StructureLink {
   private final IntegrationEntity item;

   public static StructureLink none() {
      return new StructureLink(null);
   }

   public StructureLink(IntegrationEntity var1) {
      this.item = var1;
   }

   public boolean isEnabled() {
      return this.item != null;
   }

   public void follow() {
      if (this.item != null) {
         UiCompanion.of(this.item).openPage();
      }
   }

   public String getName() {
      return this.item != null ? this.item.getName() : "";
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof StructureLink ? Objects.equal(this.item, ((StructureLink)var1).item) : false;
      }
   }

   @Override
   public int hashCode() {
      return this.item != null ? this.item.hashCode() : 0;
   }
}
