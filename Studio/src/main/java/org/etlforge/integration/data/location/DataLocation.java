package org.etlforge.integration.data.location;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.data.ExportableProperty;
import org.etlforge.integration.data.IntegrationData;
import org.etlforge.integration.data.IntegrationId;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.http.HttpVerb;
import org.etlforge.integration.http.SslVersion;

public abstract class DataLocation extends IntegrationData implements Cloneable {
   private final DataLocationType dataLocationType;

   protected DataLocation(DataLocationType var1) {
      this.dataLocationType = var1;
   }

   public final DataLocationType getDataLocationType() {
      return this.dataLocationType;
   }

   public IntegrationId getID() {
      return new DataLocation.DummyId();
   }

   public StringKeyValuePairs getPropertiesForSource() {
      return this.getPropertiesForSourceOrTarget(false);
   }

   public StringKeyValuePairs getPropertiesForTarget() {
      return this.getPropertiesForSourceOrTarget(true);
   }

   private StringKeyValuePairs getPropertiesForSourceOrTarget(boolean var1) {
      StringKeyValuePairs var2 = new StringKeyValuePairs();
      var2.put("document_id", "0");
      var2.put("timeout", "-1");
      if (!var1) {
         var2.put("ignore_first", "0");
         var2.put("ignore_last", "0");
      }

      var2.put("transfer_type", "-1");
      var2.put("encrypt_choice", "0");
      var2.put("port", "-1");
      var2.put("passive_mode", "0");
      HttpVerb var3 = var1 ? HttpLocation.getDefaultVerb(SourceTarget.Target) : HttpLocation.getDefaultVerb(SourceTarget.Source);
      var2.putInt("http_verb", var3.getId());
      var2.putInt("http_ssl_version", SslVersion.defaultVersion().getId());
      var2.putAll(super.getCopyOfKeyValues());
      return var2;
   }

   public DataLocation clone() {
      return (DataLocation)super.clone();
   }

   public ImmutableList<? extends ExportableProperty> getExportableProperties() {
      return ImmutableList.of();
   }

   public abstract DataLocation getCopyForSourceOrTarget(SourceTarget var1);

   public final boolean isFileBased() {
      return this.getDataLocationType().isFileBased();
   }

   @Override
   public void collectValidationMessages(ValidationMessageCollector var1) {
      super.collectValidationMessages(var1);
   }

   public ImmutableSet<String> getNamesOfPropertiesToCache() {
      return ImmutableSet.of();
   }

   private static class DummyId extends IntegrationId {
      private DummyId() {
      }
   }
}
