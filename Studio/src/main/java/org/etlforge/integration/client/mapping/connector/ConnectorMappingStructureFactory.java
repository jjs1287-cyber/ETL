package org.etlforge.integration.client.mapping.connector;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import javax.annotation.Nullable;
import org.etlforge.integration.client.mapping.AbstractMappingStructureFactory;
import org.etlforge.integration.client.mapping.DefaultMappingStructureCallback;
import org.etlforge.integration.client.mapping.serverapi.transformation.TreeMapperInfoProvider;
import org.etlforge.integration.data.connector.ConnectorCall;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.project.ProjectUtils;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.XsdBasedInputStructure;
import org.etlforge.integration.data.structure.XsdBasedOutputStructure;
import org.etlforge.integration.data.structure.XsdBasedStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructureException;

public final class ConnectorMappingStructureFactory extends AbstractMappingStructureFactory {
   private final TreeMapperInfoProvider infoProvider;

   public ConnectorMappingStructureFactory(TreeMapperInfoProvider var1) {
      this.infoProvider = Preconditions.checkNotNull(var1);
   }

   @Override
   public Iterable<? extends Class<? extends DataStructure>> getSupportedStructures() {
      return ImmutableList.of(XsdBasedInputStructure.class, XsdBasedOutputStructure.class);
   }

   @Nullable
   @Override
   public MappingTreeStructure getInputStructure(Transformation var1) throws MappingTreeStructureException {
      XsdBasedStructure var2 = (XsdBasedStructure)var1.getInputStructure();
      IntegrationEntityId var3 = var2.getFunctionCallId();
      IntegrationEntityLookup var4 = ProjectUtils.getLookup(var1);
      ConnectorCall var5 = (ConnectorCall)var4.getEntity(var3);
      boolean var6 = var5.hasRequestTransformation();
      return var6 ? this.createCombinedRequestAndResponse(var5) : this.createStructure(var2, false);
   }

   private MappingTreeStructure createCombinedRequestAndResponse(ConnectorCall var1) throws MappingTreeStructureException {
      XsdBasedInputStructure var2 = var1.getInputStructure();
      XsdBasedOutputStructure var3 = var1.getOutputStructure();
      DefaultMappingStructureCallback var4 = new DefaultMappingStructureCallback();
      this.infoProvider.getJtrForCombinedRequestAndResponse(var2.getXsdPath(), var2.getRootName(), var3.getXsdPath(), var3.getRootName(), false, var4);
      return var4.getMappingStructureOrThrow();
   }

   @Nullable
   @Override
   public MappingTreeStructure getOutputStructure(Transformation var1) throws MappingTreeStructureException {
      return this.createStructure((XsdBasedStructure)var1.getOutputStructure(), true);
   }

   private MappingTreeStructure createStructure(XsdBasedStructure var1, boolean var2) throws MappingTreeStructureException {
      DefaultMappingStructureCallback var3 = new DefaultMappingStructureCallback();
      this.infoProvider.getJtrForStructure(var1.getXsdPath(), var1.getRootName(), var2, var3);
      return var3.getMappingStructureOrThrow();
   }
}
