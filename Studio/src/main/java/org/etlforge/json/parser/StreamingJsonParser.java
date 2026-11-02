package org.etlforge.json.parser;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonLocation;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

public final class StreamingJsonParser implements Closeable {
   private final JsonParser impl;
   private final InputStream inStream;

   public static StreamingJsonParser parseJson(final String var0) throws IOException {
      InputSupplier var1 = new InputSupplier<InputStream>() {
         public InputStream getInput() throws IOException {
            return new ByteArrayInputStream(var0.getBytes(Charsets.UTF_8));
         }
      };
      return new StreamingJsonParser(var1);
   }

   public static StreamingJsonParser parseFile(File var0) throws IOException {
      InputSupplier var1 = Files.newInputStreamSupplier(var0);
      return new StreamingJsonParser(var1);
   }

   public StreamingJsonParser(InputSupplier<? extends InputStream> var1) throws IOException {
      this.inStream = (InputStream)var1.getInput();
      JsonFactory var2 = new JsonFactory();
      this.impl = var2.createJsonParser(this.inStream);
   }

   @Override
   public void close() throws IOException {
      this.inStream.close();
   }

   public JsonToken nextToken() throws JsonParseException, IOException {
      return this.impl.nextToken();
   }

   public String getCurrentName() throws JsonParseException, IOException {
      return this.impl.getCurrentName();
   }

   public String getText() throws JsonParseException, IOException {
      return this.impl.getText();
   }

   public Number getNumberValue() throws JsonParseException, IOException {
      return this.impl.getNumberValue();
   }

   public boolean getBooleanValue() throws JsonParseException, IOException {
      return this.impl.getBooleanValue();
   }

   public JsonLocation getCurrentLocation() {
      return this.impl.getCurrentLocation();
   }
}
