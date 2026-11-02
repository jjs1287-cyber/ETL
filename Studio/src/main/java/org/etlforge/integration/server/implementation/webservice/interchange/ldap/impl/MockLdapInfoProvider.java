package org.etlforge.integration.server.implementation.webservice.interchange.ldap.impl;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Random;
import org.etlforge.integration.client.mapping.serverapi.GetMappingStructureCallback;
import org.etlforge.integration.client.mapping.serverapi.ldap.GetLdapStructuresCallback;
import org.etlforge.integration.client.mapping.serverapi.ldap.GetObjectClassesCallback;
import org.etlforge.integration.client.mapping.serverapi.ldap.LdapClassSearchParameters;
import org.etlforge.integration.client.mapping.serverapi.ldap.LdapInfoProvider;
import org.etlforge.integration.client.mapping.serverapi.ldap.LdapStructureSearchParameters;
import org.etlforge.integration.data.structure.LdapStructure;
import org.etlforge.integration.ldap.structure.AttributeCardinality;
import org.etlforge.integration.ldap.structure.AttributeDataType;
import org.etlforge.integration.ldap.structure.AttributeSyntaxInfo;
import org.etlforge.integration.ldap.structure.AttributeUse;
import org.etlforge.integration.ldap.structure.LdapStructureAttributeBuilder;
import org.etlforge.integration.ldap.structure.NumericOid;
import org.etlforge.integration.ldap.structure.ObjectClassDefinition;
import org.etlforge.integration.ldap.structure.ObjectClassStructure;
import org.etlforge.integration.ldap.structure.ObjectClassStructureBuilder;
import org.etlforge.integration.ldap.structure.ObjectClassType;

public final class MockLdapInfoProvider implements LdapInfoProvider {
   private static final boolean LOTS_OF_CLASSES = false;

   @Override
   public void getLdapMappingStructure(LdapStructure var1, boolean var2, GetMappingStructureCallback var3) {
      MappingStructureBuilder var4 = var2 ? MappingStructureBuilder.newTargetStructureFactory() : MappingStructureBuilder.newSourceStructureFactory();
      var4.create(var1, var3);
   }

   @Override
   public void getObjectClasses(LdapClassSearchParameters var1, GetObjectClassesCallback var2) {
      ArrayList var3 = Lists.newArrayList();
      var3.add(new MockLdapInfoProvider.ObjectClassImpl("0.9.2342.19200300.100.4.5", "account"));
      var3.add(new MockLdapInfoProvider.ObjectClassImpl("1.2.840.113556.1.3.23", "container"));
      var3.add(new MockLdapInfoProvider.ObjectClassImpl("2.5.6.7", "organizationalPerson"));
      var3.add(new MockLdapInfoProvider.ObjectClassImpl("2.5.6.6", "person"));
      var3.add(new MockLdapInfoProvider.ObjectClassImpl("2.5.6.0", "top"));
      var3.add(new MockLdapInfoProvider.ObjectClassImpl("1.2.840.113556.1.5.9", "user"));
      var2.objectClassesRetrieved((ObjectClassDefinition[]) var3.toArray(new ObjectClassDefinition[var3.size()]));
   }


   @Override
   public void getLdapStructures(LdapStructureSearchParameters var1, GetLdapStructuresCallback var2) {
      int var3 = var1.getObjectClasses().length;
      ObjectClassStructure[] var4 = new ObjectClassStructure[var3];
      MockLdapInfoProvider.DummyStructureFactory var5 = new MockLdapInfoProvider.DummyStructureFactory();

      for (int var6 = 0; var6 < var3; var6++) {
         ObjectClassStructure var7 = var5.newStructure(var1.getObjectClasses()[var6].toString());
         var4[var6] = var7;
      }

      var2.structuresRetrieved(var4);
   }

   private static final class DummyStructureFactory {
      private DummyStructureFactory() {
      }

      public ObjectClassStructure newStructure(String var1) {
         ObjectClassStructureBuilder var2 = new ObjectClassStructureBuilder();
         var2.setOid(new NumericOid(var1));
         var2.setName(var1);
         this.addAttribute(var2, "FirstName", AttributeUse.REQUIRED, AttributeCardinality.ONE);
         this.addAttribute(var2, "LastName", AttributeUse.REQUIRED, AttributeCardinality.ONE);
         this.addAttribute(var2, "Phone", AttributeUse.OPTIONAL, AttributeCardinality.MANY);
         return var2.getStructure();
      }

      private void addAttribute(ObjectClassStructureBuilder var1, String var2, AttributeUse var3, AttributeCardinality var4) {
         LdapStructureAttributeBuilder var5 = new LdapStructureAttributeBuilder();
         var5.setOid(new NumericOid("1.2.3.4.5"));
         var5.setName(var2);
         var5.setSyntaxInfo(new AttributeSyntaxInfo() {
            @Override
            public AttributeDataType getDataType() {
               return AttributeDataType.STRING;
            }

            @Override
            public String getDescription() {
               return "Octet String";
            }

            @Override
            public String getOid() {
               return "1.3.6.1.4.1.1466.115.121.1.40";
            }
         });
         var5.setUse(var3);
         var5.setCardinality(var4);
         var1.addAttribute(var5.getAttribute());
      }
   }

   private static final class ObjectClassImpl implements ObjectClassDefinition {
      private final NumericOid oid;
      private final String name;

      public ObjectClassImpl(String var1, String var2) {
         this.oid = new NumericOid(var1);
         this.name = var2;
      }

      @Override
      public String getDescription() {
         return "This is a dummy description of the class " + this.name;
      }

      @Override
      public String getName() {
         return this.name;
      }

      @Override
      public NumericOid getOid() {
         return this.oid;
      }

      @Override
      public ObjectClassType getType() {
         return ObjectClassType.STRUCTURAL;
      }

      @Override
      public boolean isObsolete() {
         return false;
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else {
            return var1 instanceof ObjectClassDefinition ? this.oid.equals(((ObjectClassDefinition)var1).getOid()) : false;
         }
      }

      @Override
      public int hashCode() {
         return this.name.hashCode();
      }
   }
}
