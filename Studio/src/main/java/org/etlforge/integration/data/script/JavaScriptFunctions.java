package org.etlforge.integration.data.script;

import java.util.ArrayList;
import java.util.List;
import org.etlforge.application.VersionFactory;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.util.version.Version;

public final class JavaScriptFunctions {
   private JavaScriptFunctions() {}
   private static final List<FunctionCategory> CATEGORIES = new ArrayList<>();
   static {
      add("JavaScript — ETLForge", JavaScriptHostNames.HOSTS);
      add("JavaScript — Common", new String[]{"decodeURI", "decodeURIComponent", "encodeURI", "encodeURIComponent", "eval", "isFinite", "isNaN", "JSON.parse", "JSON.stringify", "Number", "parseFloat", "parseInt", "String"});
      add("JavaScript — Math", new String[]{"Math.abs", "Math.ceil", "Math.exp", "Math.floor", "Math.max", "Math.min", "Math.pow", "Math.random", "Math.sqrt"});
      add("JavaScript — Mapping", JavaScriptHostNames.MAPPING);
   }
   private static void add(final String name, String[] names) {
      final List<Function> entries=new ArrayList<>(); for(String value:names)entries.add(new Entry(value));
      CATEGORIES.add(new FunctionCategory(){
         public String getName(){return name;}
         public Version since(){return VersionFactory.newVersion(42,0,0,0);}
         public Iterable<Function> getFunctions(){return java.util.Collections.unmodifiableList(entries);}
      });
   }
   public static Iterable<FunctionCategory> categories(){return java.util.Collections.unmodifiableList(CATEGORIES);}
   public static Function find(String name){for(FunctionCategory category:CATEGORIES)for(Function function:category.getFunctions())if(function.getName().equals(name))return function;return null;}
   public static boolean isJavaScript(Function function){return function instanceof Entry;}
   public static boolean isInside(String source,int caret){
      if(!source.contains("<javascript>"))return false;
      java.util.List<int[]> blocks=NativeJavaScript.cached(source);
      if(blocks!=null){for(int[] block:blocks)if(block[0]==1&&caret>=block[2]&&caret<=block[3])return true;return false;}
      boolean inside=false,line=false,comment=false,regex=false,characterClass=false;char quote=0,previous='=';
      for(int i=0;i<Math.min(caret,source.length());i++){
         char c=source.charAt(i),next=i+1<source.length()?source.charAt(i+1):0;
         if(!inside){if(source.startsWith("<javascript>",i)){inside=true;i+=11;previous='=';}continue;}
         if(line){if(c=='\n'||c=='\r'||c=='\u2028'||c=='\u2029')line=false;continue;}
         if(comment){if(c=='*'&&next=='/'){comment=false;i++;}continue;}
         if(quote!=0){if(c=='\\')i++;else if(c==quote)quote=0;continue;}
         if(regex){if(c=='\\'){i++;continue;}if(c=='[')characterClass=true;else if(c==']')characterClass=false;else if(c=='/'&&!characterClass)regex=false;continue;}
         if(source.startsWith("</javascript>",i)){inside=false;i+=12;continue;}
         if(c=='/'&&next=='/'){line=true;i++;continue;}if(c=='/'&&next=='*'){comment=true;i++;continue;}
         if(c=='\''||c=='"'){quote=c;continue;}if(c=='/'&&"=([,:;!?&|{".indexOf(previous)>=0){regex=true;continue;}
         if(!Character.isWhitespace(c))previous=c;
      }
      return inside;
   }
   private static final class Entry implements Function {
      private final String name;
      Entry(String name){this.name=name;}
      public String getName(){return name;}
      public String getDisplayName(){return JavaScriptHostNames.signature(name);}
      public String getInsertValue(){return name+"()";}
      public String getFormat(){return name+"()";}
      public FCN getGrammar(){return new FCN(0,0,true);}
      public String getDocumentation(){return "Native JavaScript function. Use inside &lt;javascript&gt; blocks. SetScriptResult(value) emits a block result; no setter emits no text. Host file and database calls use existing authorized connectors.";}
      public Version since(){return VersionFactory.newVersion(42,0,0,0);}
      public boolean isInstanceFunction(){return false;}
      public boolean isReturnArray(){return name.equals("ETLForge.DbExecute");}
      public boolean acceptsProjectItemAsInput(IntegrationEntity entity){return false;}
      public boolean isHidden(){return false;}
   }
}
