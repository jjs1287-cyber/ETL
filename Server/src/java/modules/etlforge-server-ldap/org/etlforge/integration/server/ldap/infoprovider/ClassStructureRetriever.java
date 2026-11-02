package org.etlforge.integration.server.ldap.infoprovider;

import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchResult;
import org.etlforge.integration.ldap.structure.AttributeCardinality;
import org.etlforge.integration.ldap.structure.AttributeSyntaxInfo;
import org.etlforge.integration.ldap.structure.AttributeUse;
import org.etlforge.integration.ldap.structure.LdapStructureAttribute;
import org.etlforge.integration.ldap.structure.LdapStructureAttributeBuilder;
import org.etlforge.integration.ldap.structure.NumericOid;
import org.etlforge.integration.ldap.structure.ObjectClassStructure;
import org.etlforge.integration.ldap.structure.ObjectClassStructureBuilder;
import org.etlforge.integration.server.ServerDebugLog;
import org.etlforge.integration.server.ldap.LdapConnectionInfo;
import org.etlforge.integration.server.ldap.LdapException;
import org.etlforge.util.jndi.JndiUtils;

public final class ClassStructureRetriever {
   public ObjectClassStructure[] lookupStructures(LdapConnectionInfo var1, NumericOid[] var2, boolean var3) throws LdapException {
      ServerDebugLog.LOG.info("Entering lookupStructures, connInfo = " + var1);
      ServerDebugLog.LOG.info("Asking for the following classes: " + Arrays.toString(var2));
      DirContext var4 = var1.asDirContext();

      try {
         DirContext var5 = var4.getSchema("");
         ClassStructureRetriever.StructureFactory var17 = new ClassStructureRetriever.StructureFactory(var1, var5, var3);
         ArrayList var7 = new ArrayList();

         for (NumericOid var11 : var2) {
            var7.add(var17.getStructure(var11));
         }

         return (ObjectClassStructure[]) var7.toArray(new ObjectClassStructure[var7.size()]);
      } catch (NamingException var15) {
         LdapException var6 = new LdapException("Failed to lookup class definitions.", var15);
         var6.setConnectionInfo(var1);
         ServerDebugLog.LOG.throwing(this.getClass().getName(), "lookupStructures", var15);
         throw var6;
      } finally {
         JndiUtils.close(var4);
      }
   }

   private static NumericOid getNumericOid(Attributes var0) throws NamingException {
      String var1 = JndiUtils.getSingleStringAttributeValue(var0, "NUMERICOID", null);
      return new NumericOid(var1);
   }

   private static class AttributeFactory {
      private final DirContext schema;
      private final boolean skipObsoleteAttributes;
      private final SyntaxToInfoMapper syntaxMapper;

      public AttributeFactory(DirContext var1, boolean var2) {
         this.schema = var1;
         this.skipObsoleteAttributes = var2;
         this.syntaxMapper = new HardCodedSyntaxToInfoMapper();
      }

      public LdapStructureAttribute getAttribute(String var1, boolean var2) throws NamingException {
         LdapStructureAttributeBuilder var3 = new LdapStructureAttributeBuilder();
         return this.getAttributeImpl(var1, var2, var3);
      }

      private LdapStructureAttribute getAttributeImpl(String var1, boolean var2, LdapStructureAttributeBuilder var3) throws NamingException {
         DirContext var4 = this.lookupAttributeDefinition(var1, var2);
         if (var4 == null) {
            assert !var2 : "lookupAttributeDefinition should throw an Exception if it cannot find a required attribute.";
            return null;
         }

         Attributes var5 = var4.getAttributes("");
         if (this.skipObsoleteAttributes) {
            String var6 = JndiUtils.getSingleStringAttributeValue(var5, "OBSOLETE", "false");
            if ("true".equals(var6)) {
               return null;
            }
         }

         NumericOid var8 = ClassStructureRetriever.getNumericOid(var5);
         var3.setOid(var8);
         var3.setName(var1);
         var3.setUse(var2 ? AttributeUse.REQUIRED : AttributeUse.OPTIONAL);
         this.checkSuperClass(var5);
         var3.setDescription(JndiUtils.getSingleStringAttributeValue(var5, "DESC", null));
         var3.setUsageDecription(JndiUtils.getSingleStringAttributeValue(var5, "USAGE", null));
         var3.setCardinality(this.getCardinality(var5));
         var3.setReadOnly(this.getReadOnly(var5));
         this.getSyntax(var5, var3);
         LdapStructureAttribute var7 = var3.getAttribute();
         this.logAttribute(var7);
         return var7;
      }

      private void logAttribute(LdapStructureAttribute var1) {
         ServerDebugLog.LOG
            .finest(
               "Attribute: "
                  + var1.getName()
                  + "["
                  + var1.getOid()
                  + "] "
                  + var1.getSyntaxInfo().getDataType()
                  + " "
                  + var1.getUse()
                  + " "
                  + var1.getCardinality()
                  + " Description: "
                  + var1.getDescription()
            );
      }

      private DirContext lookupAttributeDefinition(String var1, boolean var2) throws NamingException {
         try {
            return (DirContext)this.schema.lookup("AttributeDefinition/" + var1);
         } catch (NameNotFoundException var4) {
            if (var2) {
               throw var4;
            }

            ServerDebugLog.LOG.info("The optional attribute " + var1 + " was not found on the LDAP server. It will not be included in the LDAP structure.");
            return null;
         }
      }

      private void getSyntax(Attributes var1, LdapStructureAttributeBuilder var2) throws NamingException {
         String var3 = JndiUtils.getSingleStringAttributeValue(var1, "SYNTAX", "");
         AttributeSyntaxInfo var4 = this.syntaxMapper.fromSyntax(var3);
         var2.setSyntaxInfo(var4);
      }

      private void checkSuperClass(Attributes var1) throws NamingException {
         String var2 = JndiUtils.getSingleStringAttributeValue(var1, "SUP", null);
         if (var2 != null) {
            System.out.println("TODO: Handle super attribute");
         }
      }

      private AttributeCardinality getCardinality(Attributes var1) throws NamingException {
         String var2 = JndiUtils.getSingleStringAttributeValue(var1, "SINGLE-VALUE", "false");
         return "true".equals(var2) ? AttributeCardinality.ONE : AttributeCardinality.MANY;
      }

      private boolean getReadOnly(Attributes var1) throws NamingException {
         String var2 = JndiUtils.getSingleStringAttributeValue(var1, "NO-USER-MODIFICATION", "false");
         return "true".equals(var2);
      }
   }

   private static class StructureFactory {
      private final LdapConnectionInfo connInfo;
      private final DirContext schema;
      private final Map<NumericOid, ObjectClassStructure> knownStructures;
      private final ClassStructureRetriever.AttributeFactory attributeFactory;

      public StructureFactory(LdapConnectionInfo var1, DirContext var2, boolean var3) {
         this.connInfo = var1;
         this.schema = var2;
         this.knownStructures = Maps.newHashMap();
         this.attributeFactory = new ClassStructureRetriever.AttributeFactory(var2, var3);
      }

      public ObjectClassStructure getStructure(NumericOid var1) throws LdapException {
         String var2 = null;

         try {
            ObjectClassStructureBuilder var3 = new ObjectClassStructureBuilder();
            var3.setOid(var1);
            Attributes var7 = this.getSchemaDefinition(var1);
            var2 = this.lookupName(var7, var1);
            if (this.knownStructures.containsKey(var1)) {
               return this.knownStructures.get(var1);
            }

            var3.setName(var2);
            ServerDebugLog.LOG.fine("Creating structure for the class " + var2);
            ServerDebugLog.LOG.finer("Found the following attributes: " + var7);
            this.populateStructure(var7, var3);
            ObjectClassStructure var8 = var3.getStructure();
            this.knownStructures.put(var1, var8);
            return var8;
         } catch (NamingException var6) {
            String var4 = "Failed to lookup class definitions.";
            if (var2 != null) {
               var4 = var4 + " An error occurred while retrieving the definition of the class \"" + var2 + "\".";
            }

            LdapException var5 = new LdapException(var4, var6);
            var5.setConnectionInfo(this.connInfo);
            ServerDebugLog.LOG.throwing(this.getClass().getName(), "lookupStructures", var6);
            throw var5;
         }
      }

      private Attributes getSchemaDefinition(NumericOid var1) throws NamingException {
         NamingEnumeration var2 = this.schema.search("ClassDefinition", "NUMERICOID=" + var1.toString(), null);
         if (var2.hasMore()) {
            SearchResult var3 = (SearchResult)var2.next();
            return var3.getAttributes();
         } else {
            return null;
         }
      }

      private void populateStructure(Attributes var1, ObjectClassStructureBuilder var2) throws NamingException {
         this.checkSuperClass(var1, var2);
         this.insertRequiredAttributes(var1, var2);
         this.insertOptionalAttributes(var1, var2);
      }

      private void checkSuperClass(Attributes var1, ObjectClassStructureBuilder var2) throws NamingException {
         NumericOid var3 = this.lookupSuperClassOid(var1);
         if (var3 != null) {
            ObjectClassStructure var4 = this.getStructure(var3);
            this.copyAttributes(var4, var2);
         }
      }

      private void copyAttributes(ObjectClassStructure var1, ObjectClassStructureBuilder var2) {
         for (LdapStructureAttribute var6 : var1.getAttributes()) {
            var2.addAttribute(var6);
         }
      }

      private void insertRequiredAttributes(Attributes var1, ObjectClassStructureBuilder var2) throws NamingException {
         Attribute var3 = var1.get("MUST");
         if (var3 != null) {
            int var4 = 0;

            for (int var5 = var3.size(); var4 < var5; var4++) {
               this.insertAttribute((String)var3.get(var4), true, var2);
            }
         }
      }

      private void insertOptionalAttributes(Attributes var1, ObjectClassStructureBuilder var2) throws NamingException {
         Attribute var3 = var1.get("MAY");
         if (var3 != null) {
            int var4 = 0;

            for (int var5 = var3.size(); var4 < var5; var4++) {
               this.insertAttribute((String)var3.get(var4), false, var2);
            }
         }
      }

      private void insertAttribute(String var1, boolean var2, ObjectClassStructureBuilder var3) throws NamingException {
         LdapStructureAttribute var4 = this.attributeFactory.getAttribute(var1, var2);
         if (var4 != null) {
            var3.addAttribute(var4);
         }
      }

      private String lookupName(Attributes var1, NumericOid var2) throws NamingException {
         return JndiUtils.getSingleStringAttributeValue(var1, "NAME", var2.toString());
      }

      private NumericOid lookupSuperClassOid(Attributes var1) throws NamingException {
         DirContext var2 = null;

         try {
            String var3 = this.lookupSuperClassName(var1);
            if (var3 == null) {
               return null;
            }

            var2 = (DirContext)this.schema.lookup("ClassDefinition/" + var3);
            Attributes var4 = var2.getAttributes("");
            return ClassStructureRetriever.getNumericOid(var4);
         } finally {
            JndiUtils.close(var2);
         }
      }

      private String lookupSuperClassName(Attributes var1) throws NamingException {
         return JndiUtils.getSingleStringAttributeValue(var1, "SUP", null);
      }
   }
}
