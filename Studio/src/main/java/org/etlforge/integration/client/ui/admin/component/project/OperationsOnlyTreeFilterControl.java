package org.etlforge.integration.client.ui.admin.component.project;

import com.google.common.collect.ImmutableSet;
import org.etlforge.application.ui.widget.tree.TreePanel;
import org.etlforge.integration.client.ui.project.info.FixedEntityTypeFilter;
import org.etlforge.integration.client.ui.project.info.TreeFilterControl;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.ui.UiCanvas;

public final class OperationsOnlyTreeFilterControl implements TreeFilterControl {
   private final TreeFilterControl impl = new FixedEntityTypeFilter(ImmutableSet.of(EntityType.Project, EntityType.Operation));

   @Override
   public void setTree(TreePanel var1) {
      this.impl.setTree(var1);
   }

   @Override
   public void update() {
      this.impl.update();
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.impl.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.impl.requestFocus();
   }
}
