package com.etlforge.integration.data.download.entity;

import com.etlforge.integration.data.download.InvalidDownloadedDataException;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.common.server.ClientServerCommunicationUtils;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Script;

public class ScriptFactory extends CustomEntityFactory<Script> {
   public ScriptFactory() {
      super(EntityType.Script, Script.class);
   }

   @Override
   protected String getIdKey() {
      return "script_id";
   }

   @Override
   protected String getNameKey() {
      return "script_name";
   }

   protected void customizeItem(Script var1, StringKeyValuePairs var2) throws InvalidDownloadedDataException {
      String var3 = var2.get("script");
      String var4 = ClientServerCommunicationUtils.decodeString(var3);
      var1.setScript(var4);
   }
}
