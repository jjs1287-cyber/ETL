package com.etlforge.integration.client.ui.sfdc.wizard.file;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

public final class SampleFileParseResult {
   public final boolean isXml;
   public final boolean isNotXml;
   public final boolean isJson;
   public final boolean isNotJson;

   public static SampleFileParseResult definitelyXml() {
      return new SampleFileParseResult(true, false, false, true);
   }

   public static SampleFileParseResult definitelyJson() {
      return new SampleFileParseResult(false, true, true, false);
   }

   public SampleFileParseResult(boolean var1, boolean var2, boolean var3, boolean var4) {
      Preconditions.checkArgument(!var1 || !var2, "Both isXml and isNotXml cannot be true");
      Preconditions.checkArgument(!var3 || !var4, "Both isXml and isNotXml cannot be true");
      Preconditions.checkArgument(!var1 || !var3, "Both isXml and isJson cannot be true");
      this.isXml = var1;
      this.isNotXml = var2;
      this.isJson = var3;
      this.isNotJson = var4;
   }

   public Optional<SampleFileFormat> getFormat() {
      if (this.isXml) {
         return Optional.of(SampleFileFormat.XML);
      } else {
         return this.isJson ? Optional.of(SampleFileFormat.JSON) : Optional.absent();
      }
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this)
         .add("isXml", this.isXml)
         .add("isNotXml", this.isNotXml)
         .add("isJson", this.isJson)
         .add("isNotJson", this.isNotJson)
         .toString();
   }
}
