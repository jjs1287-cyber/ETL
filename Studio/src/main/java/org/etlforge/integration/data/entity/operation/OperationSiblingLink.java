package org.etlforge.integration.data.entity.operation;

import com.google.common.collect.Lists;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.link.AbstractEntityLink;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectUtils;

final class OperationSiblingLink extends AbstractEntityLink {
   public OperationSiblingLink(Operation var1, Operation var2) {
      super(var1, var2);
   }

   @Override
   public boolean isStrong() {
      return false;
   }

   public Operation getDependantObject() {
      return (Operation)super.getDependantObject();
   }

   public Operation getMainObject() {
      return (Operation)super.getMainObject();
   }

   @Override
   protected void mainObjectChanged(PropertyChangeEvent var1) {
      Operation var2 = this.getMainObject();
      this.checkIfStillSibling(var2);
      super.mainObjectChanged(var1);
   }

   private void checkIfStillSibling(Operation var1) {
      IntegrationProject var2 = ProjectUtils.getProject(this.getDependantObject());
      if (var2 != null && !this.getDependantObject().canBeSibling(var1, var2.getItemLookup())) {
         this.removeSibling(var1);
      }
   }

   @Override
   protected void mainObjectDeleted() {
      this.removeSibling(this.getMainObject());
      super.mainObjectDeleted();
   }

   private void removeSibling(Operation var1) {
      Operation var2 = this.getDependantObject();
      OperationId var3 = var1.getID();
      ArrayList var4 = Lists.newArrayList();

      for (OperationId var6 : var2.getSiblingOperations()) {
         if (!var6.equals(var3)) {
            var4.add(var6);
         }
      }

      var2.setSiblingOperations(var4);
   }
}
