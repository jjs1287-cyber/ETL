import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.validation.*;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.*;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.xml.EditableXsd;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.xml.EditableXsd.Kind;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.xml.EditableXsd.Step;

public class XmlNodeSettingsTest {
 static int passed;
 static Path root;
 static final String XS=EditableXsd.XS;
 static String base="<xs:schema xmlns:xs='"+XS+"'><xs:element name='Customers'><xs:complexType><xs:sequence><xs:element name='Customer' minOccurs='1' maxOccurs='unbounded'><xs:complexType><xs:sequence><xs:element name='Name' type='xs:string'/><xs:element name='Age' type='xs:int' minOccurs='0'/></xs:sequence><xs:attribute name='id' type='xs:string'/></xs:complexType></xs:element></xs:sequence></xs:complexType></xs:element></xs:schema>";
 static File file(String name,String text)throws Exception{Path p=root.resolve(name);Files.write(p,text.getBytes(StandardCharsets.UTF_8));return p.toFile();}
 static List<Step> path(String...names){List<Step> p=new ArrayList<>();for(String n:names)p.add(new Step(n.startsWith("@")?n.substring(1):n,"",n.startsWith("@")));return p;}
 static void ok(boolean value,String name){if(!value)throw new AssertionError(name);passed++;System.out.println("PASS "+name);}
 interface Work{void run()throws Exception;}
 static void reject(String name,Work work)throws Exception{try{work.run();throw new AssertionError("Accepted: "+name);}catch(AssertionError e){throw e;}catch(Exception expected){ok(true,name);}}
 static Document read(File f)throws Exception{DocumentBuilderFactory b=DocumentBuilderFactory.newInstance();b.setNamespaceAware(true);return b.newDocumentBuilder().parse(f);}
 static Element declaration(File f,String kind,String name)throws Exception{NodeList list=read(f).getElementsByTagNameNS(XS,kind);for(int i=0;i<list.getLength();i++){Element e=(Element)list.item(i);if(name.equals(e.getAttribute("name")))return e;}return null;}
 static void valid(File schema,String xml)throws Exception{SchemaFactory.newInstance(XS).newSchema(schema).newValidator().validate(new StreamSource(new StringReader(xml)));}
 public static void main(String[]args)throws Exception{
  root=Files.createTempDirectory("etlforge-node-tests-");File original=file("customers.xsd",base);byte[] bytes=Files.readAllBytes(original.toPath());
  for(int[] range:new int[][]{{1,1},{0,1},{1,-1},{0,-1},{2,5}}){try(EditableXsd e=new EditableXsd(original,path("Customers","Customer"),false)){
   ok(e.getKind()==Kind.ELEMENT&&e.getMin()==1&&e.getMax()==-1,"read current E+ "+Arrays.toString(range));
   e.change(Kind.ELEMENT,range[0],range[1],"Customer");File f=e.save();Element c=declaration(f,"element","Customer");
   ok(c.getAttribute("minOccurs").equals(""+range[0])&&c.getAttribute("maxOccurs").equals(range[1]==-1?"unbounded":""+range[1]),"occurrence XSD "+Arrays.toString(range));
   try(EditableXsd reopened=new EditableXsd(f,path("Customers","Customer"),false)){ok(reopened.getMin()==range[0]&&reopened.getMax()==range[1],"refresh persistence "+Arrays.toString(range));}
  }}
  ok(Arrays.equals(bytes,Files.readAllBytes(original.toPath())),"original XSD byte-for-byte unchanged");
  try(EditableXsd e=new EditableXsd(original,path("Customers","Customer","@id"),false)){e.change(Kind.ATTRIBUTE,1,1,"id");File f=e.save();ok("required".equals(declaration(f,"attribute","id").getAttribute("use")),"required attribute");valid(f,"<Customers><Customer id='1'><Name>José 😀</Name></Customer></Customers>");ok(true,"UTF-8 values validate");}
  try(EditableXsd e=new EditableXsd(original,path("Customers","Customer","Name"),false)){e.change(Kind.ATTRIBUTE,1,1,"Name");File f=e.save();ok(declaration(f,"attribute","Name")!=null&&declaration(f,"element","Name")==null,"element to attribute");valid(f,"<Customers><Customer Name='Hello'/></Customers>");ok(true,"converted attribute document");}
  try(EditableXsd e=new EditableXsd(original,path("Customers","Customer","@id"),false)){e.change(Kind.ELEMENT,0,1,"id");File f=e.save();ok(declaration(f,"element","id")!=null&&declaration(f,"attribute","id")==null,"attribute to element");valid(f,"<Customers><Customer><Name>x</Name><id>1</id></Customer></Customers>");ok(true,"converted element document");}
  File scalar=file("scalar.xsd","<xs:schema xmlns:xs='"+XS+"'><xs:element name='Root'><xs:complexType><xs:sequence><xs:element name='Value' type='xs:string'/></xs:sequence><xs:attribute name='id' type='xs:int'/></xs:complexType></xs:element></xs:schema>");
  try(EditableXsd e=new EditableXsd(scalar,path("Root","Value"),false)){e.change(Kind.VALUE,1,1,"Value");File f=e.save();valid(f,"<Root id='2'>hello</Root>");ok(read(f).getElementsByTagNameNS(XS,"simpleContent").getLength()==1,"element to V parent text preserving attributes");}
  try(EditableXsd e=new EditableXsd(original,path("Customers","Customer"),false)){reject("complex cannot become attribute",()->e.change(Kind.ATTRIBUTE,1,1,"Customer"));}
  try(EditableXsd e=new EditableXsd(original,path("Customers","Customer","Name"),false)){reject("text conversion cannot discard siblings",()->e.change(Kind.VALUE,1,1,"Name"));}
  try(EditableXsd e=new EditableXsd(original,path("Customers"),false)){reject("root repetition rejected",()->e.change(Kind.ELEMENT,0,-1,"Customers"));reject("root attribute rejected",()->e.change(Kind.ATTRIBUTE,1,1,"Customers"));}
  try(EditableXsd e=new EditableXsd(original,path("Customers","Customer"),false)){reject("invalid range rejected",()->e.change(Kind.ELEMENT,5,2,"Customer"));}
  try(EditableXsd e=new EditableXsd(original,path("Customers","Customer","@id"),false)){reject("repeating attribute rejected",()->e.change(Kind.ATTRIBUTE,0,-1,"id"));}
  File named=file("named.xsd","<xs:schema xmlns:xs='"+XS+"' xmlns:t='urn:test' targetNamespace='urn:test' elementFormDefault='qualified'><xs:complexType name='Person'><xs:sequence><xs:element name='Name' type='xs:string'/></xs:sequence></xs:complexType><xs:element name='Root'><xs:complexType><xs:sequence><xs:element name='First' type='t:Person'/><xs:element name='Second' type='t:Person'/></xs:sequence></xs:complexType></xs:element></xs:schema>");
  List<Step> np=Arrays.asList(new Step("Root","urn:test",false),new Step("First","urn:test",false),new Step("Name","urn:test",false));
  try(EditableXsd e=new EditableXsd(named,np,false)){e.change(Kind.ELEMENT,0,-1,"Name");File f=e.save();valid(f,"<Root xmlns='urn:test'><First/><Second><Name>one</Name></Second></Root>");ok(true,"named type isolated to selected branch");reject("other shared type remains required",()->valid(f,"<Root xmlns='urn:test'><First/><Second/></Root>"));}
  File include=file("include.xsd","<xs:schema xmlns:xs='"+XS+"'><xs:include schemaLocation='types.xsd'/><xs:element name='Root' type='Person'/></xs:schema>");
  file("types.xsd","<xs:schema xmlns:xs='"+XS+"'><xs:complexType name='Person'><xs:sequence><xs:element name='Name' type='xs:string'/></xs:sequence></xs:complexType></xs:schema>");
  try(EditableXsd e=new EditableXsd(include,path("Root","Name"),false)){e.change(Kind.ELEMENT,0,1,"Name");File f=e.save();valid(f,"<Root/>");ok(e.getWorkspace().listFiles().length==2,"included schemas copied and rewritten");try(EditableXsd reopen=new EditableXsd(f,path("Root","Name"),false)){ok(reopen.getMin()==0,"included schema refresh");}}
  File ref=file("ref.xsd","<xs:schema xmlns:xs='"+XS+"'><xs:element name='Item' type='xs:string'/><xs:element name='Root'><xs:complexType><xs:sequence><xs:element ref='Item'/></xs:sequence></xs:complexType></xs:element></xs:schema>");
  try(EditableXsd e=new EditableXsd(ref,path("Root","Item"),false)){e.change(Kind.ELEMENT,0,-1,"Item");valid(e.save(),"<Root><Item>a</Item><Item>b</Item></Root>");ok(true,"element reference occurrence");}
  File evil=file("evil.xsd","<!DOCTYPE x [<!ENTITY p SYSTEM 'file:///C:/Windows/win.ini'>]><xs:schema xmlns:xs='"+XS+"'/>");reject("external entity rejected",()->{try(EditableXsd e=new EditableXsd(evil,path("Root"),false)) {}});
  File remote=file("remote.xsd","<xs:schema xmlns:xs='"+XS+"'><xs:include schemaLocation='https://example.invalid/x.xsd'/></xs:schema>");reject("remote dependency rejected",()->{try(EditableXsd e=new EditableXsd(remote,path("Root"),false)) {}});
  Path cleanup;try(EditableXsd e=new EditableXsd(original,path("Customers","Customer"),false)){cleanup=e.getWorkspace().toPath();}ok(!Files.exists(cleanup),"temporary workspace removed");
  System.out.println("PASSED "+passed+" checks");
 }
}
