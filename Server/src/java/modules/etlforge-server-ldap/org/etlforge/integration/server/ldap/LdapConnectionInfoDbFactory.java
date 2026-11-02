package org.etlforge.integration.server.ldap;

import java.sql.SQLException;
import org.etlforge.integration.ldap.LdapSecurityLevel;
import org.etlforge.integration.server.crypto.Crypto;
import org.etlforge.integration.server.db.trandb.SourcesTab;
import org.etlforge.integration.server.db.trandb.SourcesTabRow;
import org.etlforge.integration.server.db.trandb.TargetsTab;
import org.etlforge.integration.server.db.trandb.TargetsTabRow;
import org.etlforge.util.net.Port;

public abstract class LdapConnectionInfoDbFactory {
   public static LdapConnectionInfoDbFactory newSourceFactory(SourcesTab var0) {
      if (var0 == null) {
         throw new NullPointerException("srcTab");
      } else {
         return new LdapConnectionInfoDbFactory.SourceFactory(var0);
      }
   }

   public static LdapConnectionInfoDbFactory newTargetFactory(TargetsTab var0) {
      if (var0 == null) {
         throw new NullPointerException("tgtTab");
      } else {
         return new LdapConnectionInfoDbFactory.TargetFactory(var0);
      }
   }

   public abstract LdapConnectionInfo createConnectionInfo(int var1) throws LdapException;

   protected final void handleInvalidData(String var1, int var2) {
      throw new LdapException("The " + var1 + " with id " + var2 + " does not contain correct information for creating an LDAP connection");
   }

   private static class SourceFactory extends LdapConnectionInfoDbFactory {
      private final SourcesTab sourcesTab;

      public SourceFactory(SourcesTab var1) {
         this.sourcesTab = var1;
      }

      @Override
      public LdapConnectionInfo createConnectionInfo(int var1) throws LdapException {
         try {
            SourcesTabRow var2 = this.sourcesTab.query(var1);
            if (var2 == null) {
               throw new LdapException("The source with id " + var1 + " does not exist");
            }

            String var3 = var2.get_locator();
            Integer var4 = var2.get_port();
            Port var5 = var4 != null ? Port.valueOf(var4) : null;
            String var6 = var2.get_login();
            String var7 = this.getPassword(var2);
            LdapSecurityLevel var8 = this.getSecurityLevel(var2);
            return new LdapConnectionInfo(var3, var5, var6, var7, var8);
         } catch (SQLException var9) {
            throw new LdapException("Failed to create LdapConnectionInfo from the source with id " + var1, var9);
         } catch (NullPointerException var10) {
            this.handleInvalidData("source", var1);
         } catch (IllegalArgumentException var11) {
            this.handleInvalidData("source", var1);
         }

         return null;
      }

      private String getPassword(SourcesTabRow var1) {
         String var2 = var1.get_password();
         return Crypto.decryptString(var2);
      }

      private LdapSecurityLevel getSecurityLevel(SourcesTabRow var1) {
         int var2 = var1.get_secured();
         return LdapSecurityLevel.fromId(var2);
      }
   }

   private static class TargetFactory extends LdapConnectionInfoDbFactory {
      private final TargetsTab targetsTab;

      public TargetFactory(TargetsTab var1) {
         this.targetsTab = var1;
      }

      @Override
      public LdapConnectionInfo createConnectionInfo(int var1) throws LdapException {
         try {
            TargetsTabRow var2 = this.targetsTab.query(var1);
            if (var2 == null) {
               throw new LdapException("The target with id " + var1 + " does not exist");
            }

            String var3 = var2.get_locator();
            Integer var4 = var2.get_port();
            Port var5 = var4 != null ? Port.valueOf(var4) : null;
            String var6 = var2.get_login();
            String var7 = this.getPassword(var2);
            LdapSecurityLevel var8 = this.getSecurityLevel(var2);
            return new LdapConnectionInfo(var3, var5, var6, var7, var8);
         } catch (SQLException var9) {
            throw new LdapException("Failed to create LdapConnectionInfo from the target with id " + var1, var9);
         } catch (NullPointerException var10) {
            this.handleInvalidData("target", var1);
         } catch (IllegalArgumentException var11) {
            this.handleInvalidData("target", var1);
         }

         return null;
      }

      private String getPassword(TargetsTabRow var1) {
         String var2 = var1.get_password();
         return Crypto.decryptString(var2);
      }

      private LdapSecurityLevel getSecurityLevel(TargetsTabRow var1) {
         int var2 = var1.get_secured();
         return LdapSecurityLevel.fromId(var2);
      }
   }
}
