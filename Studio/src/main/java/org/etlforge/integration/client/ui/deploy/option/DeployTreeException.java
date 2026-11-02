package org.etlforge.integration.client.ui.deploy.option;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.etlforge.integration.data.entity.IntegrationEntity;

final class DeployTreeException extends Exception {
   private final boolean emptySelection;
   private final ImmutableList<IntegrationEntity> invalidItems;

   private DeployTreeException(String var1, boolean var2, List<IntegrationEntity> var3) {
      super(var1);
      this.emptySelection = var2;
      this.invalidItems = var3 == null ? ImmutableList.of() : ImmutableList.copyOf(var3);
   }

   public static DeployTreeException emptySelection() {
      return new DeployTreeException(Strings.get("Deploy.Tree.Exception.EmptySelection"), true, null);
   }

   public static DeployTreeException invalidItems(List<IntegrationEntity> var0) {
      return new DeployTreeException(Strings.get("Deploy.Tree.Exception.InvalidItems"), false, var0);
   }

   public boolean isEmptySelection() {
      return this.emptySelection;
   }

   public ImmutableList<IntegrationEntity> getInvalidItems() {
      return this.invalidItems;
   }
}
