package org.etlforge.integration.data.entity.transformation;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import java.util.List;
import org.etlforge.integration.data.script.SourceDataElementContainer;
import org.etlforge.integration.data.script.Transform;
import org.etlforge.integration.structure.mapping.Mapping;
import org.etlforge.system.ArgCheck;

public final class StraightMappingPredicate implements Predicate<Mapping> {
   private final String sourcePath;

   public StraightMappingPredicate(String var1) {
      this.sourcePath = ArgCheck.checkNotEmpty(var1);
   }

   public boolean apply(Mapping var1) {
      List var2 = this.parseExpression(var1);
      if (var2 == null) {
         return false;
      }

      boolean var3 = false;

      for (Object __etlItem26 : var2) {
         Transform.Token var5 = (Transform.Token) __etlItem26;
         switch (var5.m_id) {
            case -2:
            case -1:
            case 310:
            case 1100:
            case 1101:
               break;
            case 405:
               if (var3) {
                  return false;
               }

               if (!this.sourcePath.equals(var5.m_str)) {
                  return false;
               }

               var3 = true;
               break;
            default:
               return false;
         }
      }

      return var3;
   }

   private List<Transform.Token> parseExpression(Mapping var1) {
      Transform var2 = new Transform();
      var2.setSourceDataElementContainer(new SourceDataElementContainer() {
         @Override
         public boolean isFlat() {
            return true;
         }

         @Override
         public boolean contains(String var1) {
            return true;
         }
      });
      return var2.tokenize(var1.getExpression());
   }

   @Override
   public int hashCode() {
      return this.sourcePath.hashCode();
   }

   @Override
   public boolean equals(Object var1) {
      return var1 == this ? true : var1 instanceof StraightMappingPredicate && this.sourcePath.equals(((StraightMappingPredicate)var1).sourcePath);
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this).add("sourcePath", this.sourcePath).toString();
   }
}
