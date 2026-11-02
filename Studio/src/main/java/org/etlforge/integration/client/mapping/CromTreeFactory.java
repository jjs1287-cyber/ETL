package org.etlforge.integration.client.mapping;

import com.google.common.base.Preconditions;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.etlforge.application.Application;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.structure.crom.CROM_Tree;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructureException;

@ParametersAreNonnullByDefault
public final class CromTreeFactory {
   private final Transformation tx;
   private final SourceTarget st;

   public CromTreeFactory(Transformation var1, SourceTarget var2) {
      this.tx = Preconditions.checkNotNull(var1, "tx");
      this.st = Preconditions.checkNotNull(var2, "st");
   }

   @Nullable
   public CROM_Tree createTree() throws MappingTreeStructureException {
      MappingTreeStructure var1 = this.createMappingTreeStructure();
      return var1 == null ? null : CROM_Tree.fromMappingStructure(var1);
   }

   private MappingTreeStructure createMappingTreeStructure() throws MappingTreeStructureException {
      String var1 = "core.MappingStructureFactory";
      if (Application.containsBean(var1)) {
         MappingStructureFactory var2 = Application.getBean(var1, MappingStructureFactory.class);
         return this.st == SourceTarget.Source ? var2.getInputStructure(this.tx) : var2.getOutputStructure(this.tx);
      } else {
         ErrorLog.attention(this.getClass(), null, new Exception("The application is not configured to create mapping tree structures."));
         return null;
      }
   }
}
