package org.etlforge.integration.server.idmappings;

import com.google.common.base.Preconditions;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.etlforge.db.KongaDbUtils;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.lang.exception.NotImplementedYetException;

public final class GuidToIdDbMapper implements GuidToIdMapper {
   private final Connection tranDbConn;

   public GuidToIdDbMapper(Connection var1) {
      Preconditions.checkNotNull(var1, "tranDbConn");
      this.tranDbConn = var1;
   }

   @Override
   public Long getId(IntegrationEntityId var1) throws GuidToIdMappingException {
      EntityType var2 = var1.getEntityType();
      PreparedStatement var3 = null;
      ResultSet var4 = null;

      try {
         var3 = this.tranDbConn.prepareStatement("SELECT Item_Id FROM " + getTableNameForEntity(var2) + " WHERE Guid = ?");
         var3.setString(1, var1.toString());
         var4 = var3.executeQuery();
         return var4.next() ? var4.getLong(1) : null;
      } catch (SQLException var9) {
         throw new GuidToIdMappingException("Failed to retrieve the ID for the " + var2 + " with GUID " + var1, var9);
      } finally {
         KongaDbUtils.close(var4);
         KongaDbUtils.close(var3);
      }
   }

   @Override
   public IntegrationEntityId getGuid(EntityType var1, long var2) throws GuidToIdMappingException {
      Statement var4 = null;
      ResultSet var5 = null;

      try {
         var4 = this.tranDbConn.createStatement();
         var5 = var4.executeQuery("SELECT Guid FROM " + getTableNameForEntity(var1) + " WHERE Item_Id = " + var2);
         return var5.next() ? var1.getIntegrationId(var5.getString(1)) : null;
      } catch (SQLException var11) {
         throw new GuidToIdMappingException("Failed to retrieve the GUID for the " + var1 + " with ID " + var2, var11);
      } catch (IllegalArgumentException var12) {
         throw new GuidToIdMappingException("The GUID stored for the " + var1 + " with ID " + var2 + " is invalid.", var12);
      } finally {
         KongaDbUtils.close(var5);
         KongaDbUtils.close(var4);
      }
   }

   @Override
   public void deleteItem(EntityType var1, long var2) throws GuidToIdMappingException {
      throw new NotImplementedYetException();
   }

   @Override
   public void deleteItem(IntegrationEntityId var1) throws GuidToIdMappingException {
      throw new NotImplementedYetException();
   }

   @Override
   public void setGuidForItem(EntityType var1, long var2, IntegrationEntityId var4) throws GuidToIdMappingException {
      throw new NotImplementedYetException();
   }

   private static String getTableNameForEntity(EntityType var0) {
      String var1;
      if (var0 == EntityType.SalesforceBulkLoad) {
         var1 = "SalesforceBulkLoads";
      } else {
         var1 = var0.getPlural().replace(" ", "");
      }

      return "GuidsFor" + var1 + "Tab";
   }
}
