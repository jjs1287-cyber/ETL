package org.etlforge.integration.data.script;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class NativeJavaScript {
   private static boolean loaded;
   private static final class Snapshot {
      final String source; final List<int[]> blocks;
      Snapshot(String source,List<int[]> blocks){this.source=source;this.blocks=blocks;}
   }
   private static volatile Snapshot snapshot;
   private NativeJavaScript() {}
   private static synchronized void load() {
      if (loaded) return;
      String override = System.getProperty("etlforge.javascript.library");
      try {
         File library;
         if (override != null) library = new File(override);
         else {
            File origin = new File(NativeJavaScript.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            library = new File(origin.getParentFile().getParentFile(), "bin/ETLForgeJavaScript.dll");
            if(origin.isDirectory()) {
               File directory=origin;
               for(int i=0;i<6&&directory!=null;i++,directory=directory.getParentFile()){
                  File candidate=new File(directory,"dist/bin/ETLForgeJavaScript.dll");
                  if(candidate.isFile()){library=candidate;break;}
               }
            }
         }
         System.load(library.getCanonicalPath());
         loaded = true;
      } catch (Exception | LinkageError failure) {
         throw new IllegalStateException("The x64 native JavaScript validator could not be loaded: " + failure.getMessage(), failure);
      }
   }
   private static native int[] inspectNative(byte[] source);
   private static native String[] referencesNative(byte[] source);
   private static byte[] utf8(String source) {
      try {
         java.nio.ByteBuffer encoded=StandardCharsets.UTF_8.newEncoder().onMalformedInput(java.nio.charset.CodingErrorAction.REPORT).onUnmappableCharacter(java.nio.charset.CodingErrorAction.REPORT).encode(java.nio.CharBuffer.wrap(source));
         byte[] bytes=new byte[encoded.remaining()];encoded.get(bytes);return bytes;
      } catch(java.nio.charset.CharacterCodingException failure) {throw new IllegalArgumentException("Script contains an unpaired Unicode surrogate",failure);}
   }
   public static String[] references(String source) {
      if (!source.contains("<javascript>")) return new String[0];
      load();return referencesNative(utf8(source));
   }
   public static synchronized List<int[]> inspect(String source) {
      List<int[]> existing=cached(source);if(existing!=null)return existing;
      load();
      byte[] bytes = utf8(source);
      int[] spans = inspectNative(bytes);
      List<int[]> blocks = new ArrayList<>();
      for (int i = 0; i < spans.length; i += 5) {
         int[] block = new int[5]; block[0] = spans[i];
         for (int j = 1; j < 5; j++) block[j] = new String(bytes, 0, spans[i+j], StandardCharsets.UTF_8).length();
         blocks.add(block);
      }
      List<int[]> result=java.util.Collections.unmodifiableList(blocks);snapshot=new Snapshot(source,result);return result;
   }
   public static List<int[]> cached(String source) {
      Snapshot current=snapshot;return current!=null&&source.equals(current.source)?current.blocks:null;
   }
   public static int nextBlock(String source, int start) {
      int trans=source.indexOf("<trans>",start), js=source.indexOf("<javascript>",start);
      return trans<0?js:js<0?trans:Math.min(trans,js);
   }
}
