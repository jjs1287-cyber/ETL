package org.etlforge.integration.data.entity.sfdc;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public final class UCURequestHeader {
   public static final boolean TRUNCATE_FIELDS_DEFAULT = true;
   public static final boolean TRIGGER_EMAIL_DEFAULT = false;
   public static final SfdcDebugMode DEBUG_MODE_DEFAULT = SfdcDebugMode.NONE;
   private final boolean truncateFields;
   private final boolean triggerEmail;
   private final SfdcDebugMode debugMode;

   public UCURequestHeader() {
      this(true, false, DEBUG_MODE_DEFAULT);
   }

   public UCURequestHeader(boolean var1, boolean var2, SfdcDebugMode var3) {
      Preconditions.checkNotNull(var3);
      this.truncateFields = var1;
      this.triggerEmail = var2;
      this.debugMode = var3;
   }

   public boolean isTruncateFieldsEnabled() {
      return this.truncateFields;
   }

   public boolean isEmailTriggerEnabled() {
      return this.triggerEmail;
   }

   public SfdcDebugMode getDebugMode() {
      return this.debugMode;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof UCURequestHeader)) {
         return false;
      }

      UCURequestHeader var2 = (UCURequestHeader)var1;
      return this.truncateFields == var2.truncateFields && this.triggerEmail == var2.triggerEmail && this.debugMode == var2.debugMode;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.truncateFields, this.triggerEmail, this.debugMode);
   }
}
