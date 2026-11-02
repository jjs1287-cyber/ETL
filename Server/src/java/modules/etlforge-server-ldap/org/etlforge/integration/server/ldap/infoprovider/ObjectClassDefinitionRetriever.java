package org.etlforge.integration.server.ldap.infoprovider;

import java.util.ArrayList;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import org.etlforge.integration.ldap.structure.NumericOid;
import org.etlforge.integration.ldap.structure.ObjectClassDefinition;
import org.etlforge.integration.ldap.structure.ObjectClassType;
import org.etlforge.integration.server.ServerDebugLog;
import org.etlforge.integration.server.ldap.LdapConnectionInfo;
import org.etlforge.integration.server.ldap.LdapException;
import org.etlforge.util.jndi.JndiUtils;

public class ObjectClassDefinitionRetriever {
   public ObjectClassDefinition[] lookupObjectClassDefinitions(LdapConnectionInfo var1, boolean var2, boolean var3) throws LdapException {
      ServerDebugLog.LOG.info("lookupObjectClassDefinitions called - connInfo = " + var1);
      DirContext var4 = var1.asDirContext();

      try {
         DirContext var5 = var4.getSchema("");
         NamingEnumeration var13 = this.getClassNames(var5);
         return this.getClassDefinitions(var5, var13, var2, var3);
      } catch (NamingException var11) {
         LdapException var6 = new LdapException("Failed to retrieve requested object classes: " + var11.getMessage(), var11);
         var6.setConnectionInfo(var1);
         ServerDebugLog.LOG.throwing(this.getClass().getName(), "lookupObjectClassDefinitions", var11);
         throw var6;
      } finally {
         JndiUtils.close(var4);
      }
   }

   private NamingEnumeration<NameClassPair> getClassNames(DirContext var1) throws NamingException {
      DirContext var2 = (DirContext)var1.lookup("ClassDefinition");
      return var2.list("");
   }

   private ObjectClassDefinition[] getClassDefinitions(DirContext var1, NamingEnumeration<NameClassPair> var2, boolean var3, boolean var4) throws NamingException {
      ServerDebugLog.LOG.info("Looping over returned NameClassPairs");
      ArrayList var5 = new ArrayList();

      while (var2.hasMore()) {
         NameClassPair var6 = (NameClassPair)var2.next();
         ObjectClassDefinition var7 = this.getClassDefinition(var1, var6.getName(), var3, var4);
         if ((!var3 || var7.getType() != ObjectClassType.ABSTRACT) && (!var4 || !var7.isObsolete())) {
            var5.add(var7);
         }
      }

      ServerDebugLog.LOG.info("Returning found objects (" + var5.size() + ")");
      return (ObjectClassDefinition[]) var5.toArray(new ObjectClassDefinition[var5.size()]);
   }

   private ObjectClassDefinition getClassDefinition(DirContext var1, String var2, boolean var3, boolean var4) throws NamingException {
      DirContext var5 = (DirContext)var1.lookup("ClassDefinition/" + var2);
      Attributes var6 = var5.getAttributes("");
      return this.getClassDefinition(var6);
   }

   private ObjectClassDefinition getClassDefinition(Attributes var1) throws NamingException {
      String var2 = JndiUtils.getSingleStringAttributeValue(var1, "NUMERICOID", "");
      String var3 = JndiUtils.getSingleStringAttributeValue(var1, "NAME", "");
      String var4 = JndiUtils.getSingleStringAttributeValue(var1, "DESC", "");
      String var5 = JndiUtils.getSingleStringAttributeValue(var1, "OBSOLETE", "false");
      return new ObjectClassDefinitionRetriever.ClassDefinitionImpl(var2, var3, ObjectClassType.fromAttributes(var1), var4, "true".equals(var5));
   }

   private static class ClassDefinitionImpl implements ObjectClassDefinition {
      private final NumericOid oid;
      private final String name;
      private final String description;
      private final ObjectClassType type;
      private final boolean isObsolete;

      public ClassDefinitionImpl(String var1, String var2, ObjectClassType var3, String var4, boolean var5) {
         this.oid = new NumericOid(var1);
         this.name = var2;
         this.description = var4;
         this.type = var3;
         this.isObsolete = var5;
      }

      @Override
      public String getDescription() {
         return this.description;
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
         return this.type;
      }

      @Override
      public boolean isObsolete() {
         return this.isObsolete;
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
         return this.oid.hashCode();
      }
   }
}
