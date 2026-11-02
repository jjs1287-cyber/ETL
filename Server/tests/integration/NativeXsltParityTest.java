import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class NativeXsltParityTest {
 static Path root=Paths.get("C:/ETL/Samples/NativeXsltParity");
 static String nativeExe="C:/ETL/Server/build-probe/native-xslt/bin/ETLForgeXsltWorker.exe";
 static String java="C:/ETL/Tools/jdk8u504-b01/bin/java.exe";
 static String cp="C:/ETL/Server/dist/lib/xslt/*";
 static String head="<xsl:stylesheet version='3.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>";
 static String wrap(String body){return head+body+"</xsl:stylesheet>";}
 static void write(Path p,String s)throws Exception{Files.write(p,s.getBytes(StandardCharsets.UTF_8));}
 static String read(Path p)throws Exception{return new String(Files.readAllBytes(p),StandardCharsets.UTF_8);}
 static class Example {
  String name,style;String[] inputs; boolean xml;
  Example(String n,String s,boolean x,String...i){name=n;style=s;xml=x;inputs=i;}
 }
 static long run(Path dir,int inputCount,boolean nativeMode,boolean success)throws Exception{
  List<String> command=new ArrayList<>();
  if(nativeMode) command.add(nativeExe);
  else Collections.addAll(command,java,"-Xmx256m","-cp",cp,"org.etlforge.integration.xslt.EtlForgeXsltRuntime");
  command.add(dir.resolve("style.xsl").toString());
  for(int i=1;i<=inputCount;i++)command.add(dir.resolve("input"+i+".xml").toString());
  command.add(dir.resolve(nativeMode?"native.out":"java.out").toString());
  command.add(dir.resolve(nativeMode?"native.error":"java.error").toString());
  ProcessBuilder pb=new ProcessBuilder(command).redirectErrorStream(true).redirectOutput(dir.resolve(nativeMode?"native.log":"java.log").toFile());
  long start=System.nanoTime();Process p=pb.start();
  if(!p.waitFor(30,TimeUnit.SECONDS)){p.destroyForcibly();throw new AssertionError("Timed out: "+dir);}
  long elapsed=System.nanoTime()-start;
  if((p.exitValue()==0)!=success)throw new AssertionError("Unexpected status "+p.exitValue()+" native="+nativeMode+" "+dir+" "+read(dir.resolve(nativeMode?"native.log":"java.log")));
  return elapsed;
 }
 static String canonical(Node n){
  StringBuilder b=new StringBuilder();
  if(n.getNodeType()==Node.ELEMENT_NODE){
   b.append('<').append(n.getNamespaceURI()).append(':').append(n.getLocalName());
   TreeMap<String,String> attrs=new TreeMap<>();NamedNodeMap a=n.getAttributes();
   for(int i=0;i<a.getLength();i++){Node v=a.item(i);if(!"http://www.w3.org/2000/xmlns/".equals(v.getNamespaceURI()))attrs.put(v.getNamespaceURI()+":"+v.getLocalName(),v.getNodeValue());}
   b.append(attrs).append('>');for(Node c=n.getFirstChild();c!=null;c=c.getNextSibling())b.append(canonical(c));b.append("</>");
  }else if(n.getNodeType()==Node.TEXT_NODE||n.getNodeType()==Node.CDATA_SECTION_NODE)b.append(n.getNodeValue());
  return b.toString();
 }
 static String xml(Path p)throws Exception{DocumentBuilderFactory f=DocumentBuilderFactory.newInstance();f.setNamespaceAware(true);f.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);return canonical(f.newDocumentBuilder().parse(p.toFile()).getDocumentElement());}
 static void compare(Path d,boolean xml)throws Exception{
  if(xml){if(!xml(d.resolve("java.out")).equals(xml(d.resolve("native.out"))))throw new AssertionError("XML mismatch "+d);}
  else if(!read(d.resolve("java.out")).equals(read(d.resolve("native.out"))))throw new AssertionError("Text mismatch "+d);
 }
 static double median(List<Long> times){Collections.sort(times);return times.get(times.size()/2)/1e6;}
 public static void main(String[] args)throws Exception{
  Files.createDirectories(root);
  String large="<rows>";StringBuilder data=new StringBuilder(large);for(int i=0;i<10000;i++)data.append("<row id='").append(i).append("'><name>Customer ").append(i).append("</name><amount>").append(i%101).append("</amount></row>");data.append("</rows>");
  List<Example> examples=Arrays.asList(
   new Example("01-embedded-xml",read(Paths.get("C:/ETL/Samples/XSLT/ExpandEmbeddedXml/transform.xsl")),true,read(Paths.get("C:/ETL/Samples/XSLT/ExpandEmbeddedXml/input.xml"))),
   new Example("02-identity",wrap("<xsl:mode on-no-match='shallow-copy'/><xsl:output indent='no'/>") ,true,"<root id='1'><child>hello</child><!--note--></root>"),
   new Example("03-namespaces",wrap("<xsl:template match='/'><result xmlns='urn:result'><xsl:copy-of select='/*/*'/></result></xsl:template>"),true,"<r:root xmlns:r='urn:input'><r:item r:id='7'>value</r:item></r:root>"),
   new Example("04-grouping",wrap("<xsl:template match='/'><groups><xsl:for-each-group select='rows/r' group-by='@key'><xsl:sort select='current-grouping-key()'/><group key='{current-grouping-key()}' total='{sum(current-group()/@n)}'/></xsl:for-each-group></groups></xsl:template>"),true,"<rows><r key='b' n='2'/><r key='a' n='3'/><r key='b' n='4'/></rows>"),
   new Example("05-multiple-inputs",wrap("<xsl:template name='start_here'><merged><xsl:copy-of select=\"doc(concat('param',1))/* | doc('urn:etlforge:xslt:param2')/*\"/></merged></xsl:template>"),true,"<customers><customer id='1'/></customers>","<orders><order customer='1'/></orders>"),
   new Example("06-unicode",wrap("<xsl:mode on-no-match='shallow-copy'/><xsl:output indent='no'/>") ,true,"<root>caf\u00e9 \u4e2d\u6587 \uD83D\uDE00 &amp; &lt; &gt; \" '</root>"),
   new Example("07-text",wrap("<xsl:output method='text'/><xsl:template match='/'><xsl:for-each select='rows/r'><xsl:value-of select='@id'/><xsl:text>,</xsl:text><xsl:value-of select='.'/><xsl:text>&#10;</xsl:text></xsl:for-each></xsl:template>"),false,"<rows><r id='1'>Alice</r><r id='2'>Bob</r></rows>"),
   new Example("08-html",wrap("<xsl:output method='html' indent='no'/><xsl:template match='/'><html><body><h1><xsl:value-of select='root/title'/></h1><br/></body></html></xsl:template>"),false,"<root><title>ETLForge &amp; XSLT</title></root>"),
   new Example("09-json",wrap("<xsl:output method='json' indent='no'/><xsl:template match='/'><xsl:sequence select=\"map {'name':string(root/name),'count':count(root/r),'items':array {root/r/string()}}\"/></xsl:template>"),false,"<root><name>Test</name><r>A</r><r>B</r></root>"),
   new Example("10-large",wrap("<xsl:output indent='no'/><xsl:template match='/'><results count='{count(rows/row)}' total='{sum(rows/row/amount)}'><xsl:for-each select='rows/row'><customer id='{@id}'><xsl:value-of select='name'/></customer></xsl:for-each></results></xsl:template>"),true,data.toString())
  );
  List<String> csv=new ArrayList<>();csv.add("Example,InputBytes,ByteIdentical,SemanticMatch,JavaMedianMs,NativeMedianMs,Speedup");
  for(Example e:examples){Path d=root.resolve(e.name);Files.createDirectories(d);write(d.resolve("style.xsl"),e.style);int bytes=0;for(int i=0;i<e.inputs.length;i++){write(d.resolve("input"+(i+1)+".xml"),e.inputs[i]);bytes+=e.inputs[i].getBytes(StandardCharsets.UTF_8).length;}
   run(d,e.inputs.length,false,true);run(d,e.inputs.length,true,true);compare(d,e.xml);
   List<Long> jt=new ArrayList<>(),nt=new ArrayList<>();for(int r=0;r<5;r++){if(r%2==0){jt.add(run(d,e.inputs.length,false,true));nt.add(run(d,e.inputs.length,true,true));}else{nt.add(run(d,e.inputs.length,true,true));jt.add(run(d,e.inputs.length,false,true));}compare(d,e.xml);}
   double j=median(jt),n=median(nt);boolean same=Arrays.equals(Files.readAllBytes(d.resolve("java.out")),Files.readAllBytes(d.resolve("native.out")));
   csv.add(String.format(Locale.ROOT,"%s,%d,%s,true,%.3f,%.3f,%.2f",e.name,bytes,same,j,n,j/n));System.out.println(csv.get(csv.size()-1));
  }
  Files.write(Paths.get("C:/ETL/Reports/native-xslt-parity-speed.csv"),csv,StandardCharsets.UTF_8);
  String[] blocked={"doc('file:///C:/Windows/win.ini')","unparsed-text('file:///C:/Windows/win.ini')","doc('http://127.0.0.1:1/blocked')","unparsed-text('https://example.com/')","collection('file:///C:/Windows')","doc('urn:etlforge:xslt:param99')"};
  int negatives=0;
  for(String expr:blocked){Path d=root.resolve("negative-"+(++negatives));Files.createDirectories(d);write(d.resolve("style.xsl"),wrap("<xsl:template match='/'><xsl:sequence select=\""+expr+"\"/></xsl:template>"));write(d.resolve("input1.xml"),"<root/>");run(d,1,false,false);run(d,1,true,false);}
  String[] badStyles={wrap("<xsl:include href='file:///C:/Windows/win.ini'/>"),wrap("<xsl:import href='urn:etlforge:xslt:param1'/>"),wrap("<xsl:template match='/'><xsl:result-document href='file:///C:/ETL/Samples/NativeXsltParity/forbidden.xml'><r/></xsl:result-document></xsl:template>"),"<broken>"};
  for(String style:badStyles){Path d=root.resolve("negative-"+(++negatives));Files.createDirectories(d);write(d.resolve("style.xsl"),style);write(d.resolve("input1.xml"),"<root/>");run(d,1,false,false);run(d,1,true,false);}
  Path broken=root.resolve("negative-"+(++negatives));Files.createDirectories(broken);write(broken.resolve("style.xsl"),wrap("<xsl:mode on-no-match='shallow-copy'/>"));write(broken.resolve("input1.xml"),"<broken>");run(broken,1,false,false);run(broken,1,true,false);
  Path dtd=root.resolve("negative-"+(++negatives));Files.createDirectories(dtd);write(dtd.resolve("style.xsl"),wrap("<xsl:mode on-no-match='shallow-copy'/>"));write(dtd.resolve("input1.xml"),"<!DOCTYPE root SYSTEM 'file:///C:/Windows/win.ini'><root/>");run(dtd,1,false,false);run(dtd,1,true,false);
  if(Files.exists(root.resolve("forbidden.xml")))throw new AssertionError("Unexpected secondary output");
  System.out.println("PASS: 10 examples, 5 timed runs per backend per example, "+negatives+" rejection cases per backend.");
 }
}
