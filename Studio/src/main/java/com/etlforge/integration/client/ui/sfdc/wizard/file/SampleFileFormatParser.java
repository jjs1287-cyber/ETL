package com.etlforge.integration.client.ui.sfdc.wizard.file;

import com.google.common.base.Throwables;
import com.google.common.io.Closeables;
import com.google.common.io.InputSupplier;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.codehaus.jackson.JsonToken;
import org.etlforge.json.parser.StreamingJsonParser;
import org.etlforge.system.thread.TimeDuration;
import org.etlforge.xml.sax.SaxParsers;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public final class SampleFileFormatParser {
   private final InputSupplier<? extends InputStream> source;
   private TimeDuration timeout = new TimeDuration(2L, TimeUnit.SECONDS);

   public SampleFileFormatParser(InputSupplier<? extends InputStream> var1) {
      this.source = var1;
   }

   public void setTimeout(TimeDuration var1) {
      this.timeout = var1;
   }

   public SampleFileParseResult guessFormat() {
      SampleFileFormatParser.ParsingThread var1 = new SampleFileFormatParser.ParsingThread(this.source);
      ExecutorService var2 = Executors.newFixedThreadPool(2);

      try {
         Future var3 = var2.submit(var1);
         Future var4 = var2.submit(new SampleFileFormatParser.Interruptor(this.timeout, var3));
         SampleFileParseResult var5 = (SampleFileParseResult)var3.get();
         var4.cancel(true);
         return var5;
      } catch (ExecutionException var12) {
         throw Throwables.propagate(var12.getCause());
      } catch (CancellationException var13) {
      } catch (InterruptedException var14) {
         Thread.currentThread().interrupt();
      } finally {
         this.stopThreads(var2);
      }

      return var1.getResult();
   }

   private void stopThreads(ExecutorService var1) {
      var1.shutdown();

      try {
         var1.awaitTermination(this.timeout.getValue(), this.timeout.getUnit());
      } catch (InterruptedException var3) {
         Thread.currentThread().interrupt();
      }
   }

   private static class Interruptor implements Callable<Void> {
      private final TimeDuration timeout;
      private final Future<SampleFileParseResult> target;

      public Interruptor(TimeDuration var1, Future<SampleFileParseResult> var2) {
         this.timeout = var1;
         this.target = var2;
      }

      public Void call() throws Exception {
         this.timeout.sleep();
         this.target.cancel(true);
         return null;
      }
   }

   private static class JsonParser implements Closeable {
      private final StreamingJsonParser impl;
      private volatile boolean isJson;
      private volatile boolean isNotJson;

      public JsonParser(InputSupplier<? extends InputStream> var1) throws IOException {
         this.impl = new StreamingJsonParser(var1);
      }

      public void parse() {
         try {
            while (!Thread.currentThread().isInterrupted()) {
               JsonToken var1 = this.impl.nextToken();
               if (var1 == null) {
                  return;
               }

               this.isJson = true;
            }
         } catch (Exception var2) {
            this.isJson = false;
            this.isNotJson = true;
         }
      }

      public boolean isJson() {
         return this.isJson;
      }

      public boolean isNotJson() {
         return this.isNotJson;
      }

      @Override
      public void close() throws IOException {
         Closeables.closeQuietly(this.impl);
      }
   }

   private static class ParsingThread implements Callable<SampleFileParseResult> {
      private final InputSupplier<? extends InputStream> source;
      private volatile boolean isXml;
      private volatile boolean isNotXml;
      private volatile boolean isJson;
      private volatile boolean isNotJson;

      public ParsingThread(InputSupplier<? extends InputStream> var1) {
         this.source = var1;
      }

      public SampleFileParseResult call() throws Exception {
         this.tryXml();
         if (this.isXml) {
            return SampleFileParseResult.definitelyXml();
         }

         this.tryJson();
         return this.isJson ? SampleFileParseResult.definitelyJson() : this.getResult();
      }

      private void tryXml() {
         SampleFileFormatParser.XmlParser var1 = new SampleFileFormatParser.XmlParser(this.source);
         var1.parse();
         this.isXml = var1.isXml();
         this.isNotXml = var1.isNotXml();
      }

      private void tryJson() {
         SampleFileFormatParser.JsonParser var1 = null;

         try {
            var1 = new SampleFileFormatParser.JsonParser(this.source);
            var1.parse();
            this.isJson = var1.isJson();
            this.isNotJson = var1.isNotJson();
         } catch (IOException var6) {
            var6.printStackTrace();
            this.isNotJson = true;
         } finally {
            Closeables.closeQuietly(var1);
         }
      }

      public SampleFileParseResult getResult() {
         return new SampleFileParseResult(this.isXml, this.isNotXml, this.isJson, this.isNotJson);
      }
   }

   private static class XmlParser {
      private final InputSupplier<? extends InputStream> source;
      private volatile boolean isXml;
      private volatile boolean isNotXml;

      public XmlParser(InputSupplier<? extends InputStream> var1) {
         this.source = var1;
      }

      public void parse() {
         try {
            SaxParsers.parseInput(this.source, new SampleFileFormatParser.XmlParser.Handler(), false);
         } catch (SAXException var2) {
            this.isXml = false;
            this.isNotXml = true;
         } catch (IOException var3) {
            this.isXml = false;
            this.isNotXml = true;
         } catch (SampleFileFormatParser.XmlParser.ParsingInterruptedException var4) {
         }
      }

      public boolean isXml() {
         return this.isXml;
      }

      public boolean isNotXml() {
         return this.isNotXml;
      }

      private class Handler extends DefaultHandler {
         private Handler() {
         }

         @Override
         public void startDocument() throws SAXException {
            XmlParser.this.isXml = true;
         }

         @Override
         public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
            this.checkInterrupted();
         }

         private void checkInterrupted() {
            if (Thread.currentThread().isInterrupted()) {
               throw new SampleFileFormatParser.XmlParser.ParsingInterruptedException();
            }
         }

         @Override
         public void endElement(String var1, String var2, String var3) throws SAXException {
            this.checkInterrupted();
         }

         @Override
         public void characters(char[] var1, int var2, int var3) throws SAXException {
            this.checkInterrupted();
         }
      }

      private static class ParsingInterruptedException extends RuntimeException {
         private ParsingInterruptedException() {
         }
      }
   }
}
