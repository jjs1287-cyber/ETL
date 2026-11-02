package org.etlforge.integration.data.entity.transformation;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.script.ExpressionSource;
import org.etlforge.integration.structure.mapping.NodePath;

public final class TransformationExpressionSource implements ExpressionSource {
   private final Transformation tf;
   private final NodePath targetNode;

   public TransformationExpressionSource(Transformation var1, NodePath var2) {
      this.tf = Preconditions.checkNotNull(var1, "tf");
      this.targetNode = Preconditions.checkNotNull(var2, "targetNode");
   }

   @Override
   public boolean originatesIn(IntegrationEntity var1) {
      return var1 == this.tf;
   }

   @Override
   public boolean matches(EntityFilter var1) {
      return var1.apply(this.tf);
   }

   public Transformation getTransformation() {
      return this.tf;
   }

   public NodePath getTargetNode() {
      return this.targetNode;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof TransformationExpressionSource)) {
         return false;
      }

      TransformationExpressionSource var2 = (TransformationExpressionSource)var1;
      return this.tf.getID().equals(var2.tf.getID()) && this.targetNode.equals(var2.targetNode);
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.tf, this.targetNode);
   }
}
