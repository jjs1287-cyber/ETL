package org.etlforge.integration.uidelegate.entity.tooltip;

import com.google.common.collect.Maps;
import java.util.Date;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.DebugMode;
import org.etlforge.integration.data.entity.EntityPath;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.util.KeyValueToolTip;
import org.etlforge.ui.util.decorator.DateCell;
import org.etlforge.util.string.StringLimiter;

public class IntegrationEntityToolTip {
   private static final int DESCRIPTION_MAX_LENGTH = 100;
   private final Map<Object, Object> pairs = Maps.newLinkedHashMap();
   private String tooltip;

   public IntegrationEntityToolTip(IntegrationEntity var1) {
      this(var1, false);
   }

   public IntegrationEntityToolTip(IntegrationEntity var1, boolean var2) {
      this.addEntry("Name:", var1.getName());
      if (DebugMode.IS_ENABLED) {
         this.addEntry("GUID:", var1.getID());
      }

      if (var2) {
         this.addEntry("Path:", new EntityPath(var1).toString());
      }

      this.addCreatedDate(var1);
      this.addModifiedDate(var1);
      this.addDescription(var1);
      if (var1.isDeleted()) {
         this.addDeletedAt(var1);
      }
   }

   public String getToolTip() {
      if (this.tooltip == null) {
         this.tooltip = KeyValueToolTip.createTable(this.pairs);
      }

      return this.tooltip;
   }

   public final void addEntry(Object var1, Object var2) {
      this.pairs.put(var1.toString(), this.translateValueObject(var2));
   }

   protected String translateValueObject(Object var1) {
      if (var1 instanceof String) {
         return (String)var1;
      } else {
         return var1 instanceof Date ? new DateCell((Date)var1).getDisplayString() : String.valueOf(var1);
      }
   }

   private void addCreatedDate(IntegrationEntity var1) {
      Date var2 = var1.getCreatedAt();
      if (var2 != null) {
         this.addEntry("Created:", var2);
      }
   }

   private void addModifiedDate(IntegrationEntity var1) {
      Date var2 = var1.getModifiedAt();
      if (var2 != null) {
         this.addEntry("Last Modified:", var2);
      }
   }

   private void addDeletedAt(IntegrationEntity var1) {
      Date var2 = var1.getDeletedAt();
      if (var2 != null) {
         this.addEntry("Deleted on: ", var2);
      }
   }

   private void addDescription(IntegrationEntity var1) {
      String var2 = var1.getDescription();
      if (StringUtils.isNotEmpty(var2)) {
         this.addEntry("Description: ", StringLimiter.limit(var2, 100));
      }
   }
}
