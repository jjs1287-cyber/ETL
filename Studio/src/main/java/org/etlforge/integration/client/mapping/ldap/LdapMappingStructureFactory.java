package org.etlforge.integration.client.mapping.ldap;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import javax.annotation.Nullable;
import org.etlforge.integration.client.mapping.AbstractMappingStructureFactory;
import org.etlforge.integration.client.mapping.DefaultMappingStructureCallback;
import org.etlforge.integration.client.mapping.serverapi.ldap.LdapInfoProvider;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.LdapStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructureException;

public final class LdapMappingStructureFactory extends AbstractMappingStructureFactory {
   private final LdapInfoProvider infoProvider;

   public LdapMappingStructureFactory(LdapInfoProvider var1) {
      this.infoProvider = Preconditions.checkNotNull(var1);
   }

   @Override
   public Iterable<? extends Class<? extends DataStructure>> getSupportedStructures() {
      return ImmutableList.of(LdapStructure.class);
   }

   @Nullable
   @Override
   public MappingTreeStructure getInputStructure(Transformation var1) throws MappingTreeStructureException {
      return this.createStructure((LdapStructure)var1.getInputStructure(), false);
   }

   @Nullable
   @Override
   public MappingTreeStructure getOutputStructure(Transformation var1) throws MappingTreeStructureException {
      return this.createStructure((LdapStructure)var1.getOutputStructure(), true);
   }

   @Nullable
   private MappingTreeStructure createStructure(LdapStructure var1, boolean var2) throws MappingTreeStructureException {
      DefaultMappingStructureCallback var3 = new DefaultMappingStructureCallback();
      this.infoProvider.getLdapMappingStructure(var1, var2, var3);
      return var3.getMappingStructureOrThrow();
   }
}
