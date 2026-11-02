package org.etlforge.integration.client.mapping.xml;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import javax.annotation.Nullable;
import org.etlforge.integration.client.mapping.AbstractMappingStructureFactory;
import org.etlforge.integration.client.mapping.DefaultMappingStructureCallback;
import org.etlforge.integration.client.mapping.serverapi.transformation.TreeMapperInfoProvider;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.XmlStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructureException;

public final class XmlMappingStructureFactory extends AbstractMappingStructureFactory {
   private final TreeMapperInfoProvider infoProvider;

   public XmlMappingStructureFactory(TreeMapperInfoProvider var1) {
      this.infoProvider = Preconditions.checkNotNull(var1);
   }

   @Override
   public Iterable<? extends Class<? extends DataStructure>> getSupportedStructures() {
      return ImmutableList.of(XmlStructure.class);
   }

   @Nullable
   @Override
   public MappingTreeStructure getInputStructure(Transformation var1) throws MappingTreeStructureException {
      return this.createStructure((XmlStructure)var1.getInputStructure(), false);
   }

   @Nullable
   @Override
   public MappingTreeStructure getOutputStructure(Transformation var1) throws MappingTreeStructureException {
      return this.createStructure((XmlStructure)var1.getOutputStructure(), true);
   }

   @Nullable
   private MappingTreeStructure createStructure(XmlStructure var1, boolean var2) throws MappingTreeStructureException {
      DefaultMappingStructureCallback var3 = new DefaultMappingStructureCallback();
      this.infoProvider.getJtrForStructure(var1.getFilePath(), var1.getRootNodeName(), var2, var3);
      return var3.getMappingStructureOrThrow();
   }
}
