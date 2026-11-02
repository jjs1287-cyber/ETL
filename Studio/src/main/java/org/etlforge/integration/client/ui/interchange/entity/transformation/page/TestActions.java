package org.etlforge.integration.client.ui.interchange.entity.transformation.page;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import java.util.Arrays;
import java.util.List;
import org.etlforge.application.ui.action.ApplicationAction;

final class TestActions {
   private final ImmutableList<ApplicationAction> actions;

   public TestActions(TransformationPage var1, TestActionsFactory var2) {
      List var3 = Arrays.asList(var2.newLoadSourceAction(var1), var2.newTestTransformationAction(var1), var2.newTestOperationAction(var1));
      this.actions = ImmutableList.copyOf(Optional.presentInstances(var3));
   }

   public boolean isEmpty() {
      return this.actions.isEmpty();
   }

   public Iterable<ApplicationAction> getAll() {
      return this.actions;
   }

   public void addPageActions(TransformationPage var1) {
      var1.addPageActions(this.actions.toArray(new ApplicationAction[this.actions.size()]));
   }
}
