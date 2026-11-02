package com.etlforge.integration.client.ui.sfdc.wizard;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.etlforge.integration.client.ui.sfdc.wizard.file.SampleFile;
import com.etlforge.integration.client.ui.sfdc.wizard.file.SampleFileFormatParser;
import com.etlforge.integration.client.ui.sfdc.wizard.file.SampleFileParseResult;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.FileLocation;
import org.etlforge.integration.data.location.file.FileFilter;

public final class RequestSourceTypeDeducer {
   private final Source source;
   private final Optional<SampleFile> sampleFile;

   public RequestSourceTypeDeducer(Source var1, Optional<SampleFile> var2) {
      this.source = Preconditions.checkNotNull(var1);
      this.sampleFile = Preconditions.checkNotNull(var2);
   }

   public Optional<RequestSourceType> deduceType() {
      return Optional.fromNullable(this.deduceTypeImpl());
   }

   private RequestSourceType deduceTypeImpl() {
      return this.sampleFile.isPresent() ? this.deduceTypeFromSampleFile() : this.deduceTypeFromSource();
   }

   private RequestSourceType deduceTypeFromSampleFile() {
      SampleFileFormatParser var1 = new SampleFileFormatParser(this.sampleFile.get().asInputSupplier());
      SampleFileParseResult var2 = var1.guessFormat();
      if (var2.isNotXml && var2.isNotJson) {
         return RequestSourceType.TEXT;
      } else if (var2.isXml) {
         return RequestSourceType.XML;
      } else {
         return var2.isJson ? RequestSourceType.JSON : this.getTypeFromExtension(this.sampleFile.get().getExtension());
      }
   }

   private RequestSourceType getTypeFromExtension(String var1) {
      if (var1.equalsIgnoreCase("xml")) {
         return RequestSourceType.XML;
      } else if (var1.equalsIgnoreCase("json")) {
         return RequestSourceType.JSON;
      } else {
         return !var1.equalsIgnoreCase("txt") && !var1.equalsIgnoreCase("csv") ? null : RequestSourceType.TEXT;
      }
   }

   private RequestSourceType deduceTypeFromSource() {
      DataLocation var1 = this.source.getLocation();
      if (var1 instanceof FileLocation) {
         FileFilter var2 = ((FileLocation)var1).getFileFilter();
         String var3 = var2.getFilter();
         if (var3.contains(".")) {
            String var4 = StringUtils.substringAfter(var3, ".");
            return this.getTypeFromExtension(var4);
         }
      }

      return null;
   }
}
