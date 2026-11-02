package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.xml;

import java.io.*;
import java.net.URI;
import java.nio.file.*;
import java.util.*;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.SchemaFactory;
import org.w3c.dom.*;

public final class EditableXsd implements AutoCloseable {
 public static final String XS=XMLConstants.W3C_XML_SCHEMA_NS_URI;
 public enum Kind { ELEMENT, ATTRIBUTE, VALUE }
 public static final class Step {
  public final String name,namespace; public final boolean attribute;
  public Step(String name,String namespace,boolean attribute){this.name=name;this.namespace=namespace==null?"":namespace;this.attribute=attribute;}
 }
 private final Path workspace;
 private final Map<Path,Document> documents=new LinkedHashMap<>();
 private final Map<Document,Path> output=new IdentityHashMap<>();
 private final Path main;
 private Element selected;
 private Element parentElement;
 private Kind originalKind;
 public EditableXsd(File root,List<Step> path,boolean value) throws Exception {
  workspace=Files.createTempDirectory("etlforge-schema-edit-");
  try {
   main=root.toPath().toRealPath();load(main,new HashSet<Path>());
   String prefix="etlforge-edited-"+UUID.randomUUID().toString().replace("-","")+"-";
   int index=0;
   for(Document doc:documents.values())output.put(doc,workspace.resolve(prefix+(index++)+".xsd"));
   rewriteLocations();
   if(path.isEmpty())throw new IOException("Select an XML element, attribute or value node.");
   Step first=path.get(0);selected=findGlobal("element",first.namespace,first.name);
   for(int i=1;i<path.size();i++) {
    parentElement=selected;
    Element type=editableType(selected);
    selected=findChild(type,path.get(i),new HashSet<Element>());
   }
   originalKind=value?Kind.VALUE:(path.get(path.size()-1).attribute?Kind.ATTRIBUTE:Kind.ELEMENT);
  } catch(Exception e){close();throw e;}
 }
 public Kind getKind(){return originalKind;}
 public boolean isRoot(){return parentElement==null&&originalKind!=Kind.VALUE;}
 public String getName(){return selected.hasAttribute("name")?selected.getAttribute("name"):local(selected.getAttribute("ref"));}
 public int getMin(){return originalKind==Kind.ATTRIBUTE?("required".equals(selected.getAttribute("use"))?1:0):number(selected,"minOccurs",1);}
 public int getMax(){return originalKind==Kind.ATTRIBUTE?1:("unbounded".equals(selected.getAttribute("maxOccurs"))?-1:number(selected,"maxOccurs",1));}
 public boolean isScalar() throws Exception {
  Element declaration=dereference(selected);
  Element type=typeOf(declaration);
  return type==null?declaration.hasAttribute("type")&&!"anyType".equals(local(declaration.getAttribute("type"))):"simpleType".equals(type.getLocalName())||child(type,"simpleContent")!=null;
 }
 public void change(Kind kind,int min,int max,String name) throws Exception {
  if(min<0||max==0||max < -1||(max!=-1&&max<min))throw new IOException("Invalid minimum/maximum occurrence count.");
  if(originalKind==Kind.VALUE) {
   if(kind!=Kind.VALUE)throw new IOException("Select the containing element to convert its kind. A V node is its text value.");
   if(!isRoot()&&(min!=getMin()||max!=getMax()))throw new IOException("Set occurrence counts on the containing element.");
   return;
  }
  if(kind==originalKind) {
   if(kind==Kind.ATTRIBUTE){if(max!=1||min>1)throw new IOException("Attributes occur at most once.");selected.setAttribute("use",min==0?"optional":"required");}
   else {if(isRoot()){if(min!=1||max!=1)throw new IOException("The document root must occur exactly once.");return;}selected.setAttribute("minOccurs",Integer.toString(min));selected.setAttribute("maxOccurs",max==-1?"unbounded":Integer.toString(max));}
   return;
  }
  if(isRoot())throw new IOException("The XML document root must remain an element.");
  if(!isScalar())throw new IOException("Only scalar nodes can become attributes or text values. This node contains child elements.");
  Element declaration=dereference(selected);
  if(!namespaceOf(declaration).isEmpty()&&!namespaceOf(declaration).equals(namespaceOf(parentElement)))throw new IOException("Cross-namespace node-kind conversion is not supported; use the schema editor.");
  if(kind==Kind.VALUE){convertToValue(declaration,min,max);return;}
  if(max!=1||min>1)throw new IOException("Node-kind conversion requires zero or one occurrence. Change repetition separately for elements.");
  Document doc=selected.getOwnerDocument();
  Element replacement=doc.createElementNS(XS,"xs:"+(kind==Kind.ATTRIBUTE?"attribute":"element"));
  replacement.setAttribute("xmlns:xs",XS);
  replacement.setAttribute("name",getName());
  copyScalarType(declaration,replacement);
  for(String property:new String[]{"default","fixed"})if(declaration.hasAttribute(property))replacement.setAttribute(property,declaration.getAttribute(property));
  Element container=editableType(parentElement);
  if(kind==Kind.ATTRIBUTE){
   if(findNamedAttribute(container,getName()))throw new IOException("An attribute with this name already exists.");
   replacement.setAttribute("use",min==0?"optional":"required");
   Element content=child(container,"simpleContent");
   if(content!=null)container=firstContent(content);
   container.appendChild(replacement);
  } else {
   if(child(container,"simpleContent")!=null)throw new IOException("An element cannot be added beside simple text content.");
   Element sequence=child(container,"sequence");
   if(sequence==null){if(child(container,"choice")!=null||child(container,"all")!=null)throw new IOException("Convert this attribute using the schema editor for choice/all content.");sequence=doc.createElementNS(XS,"xs:sequence");sequence.setAttribute("xmlns:xs",XS);container.insertBefore(sequence,container.getFirstChild());}
   for(Element e:descendants(sequence,"element"))if(getName().equals(e.getAttribute("name")))throw new IOException("An element with this name already exists.");
   replacement.setAttribute("minOccurs",Integer.toString(min));replacement.setAttribute("maxOccurs","1");sequence.appendChild(replacement);
  }
  selected.getParentNode().removeChild(selected);selected=replacement;
 }
 private void convertToValue(Element declaration,int min,int max) throws Exception {
  if(min!=1||max!=1)throw new IOException("A text value must occur exactly once.");
  Element parentType=editableType(parentElement);
  List<Element> elements=descendants(parentType,"element");
  if(originalKind==Kind.ELEMENT&&(elements.size()!=1||elements.get(0)!=selected))throw new IOException("A text value can replace only the sole child element.");
  if(originalKind==Kind.ATTRIBUTE&&!elements.isEmpty())throw new IOException("A text value cannot replace an attribute while child elements remain.");
  if(child(parentType,"simpleContent")!=null||child(parentType,"complexContent")!=null)throw new IOException("This parent already has inherited or simple text content.");
  Element scalar=scalarType(declaration);
  if(scalar!=null&&"simpleType".equals(scalar.getLocalName()))throw new IOException("Custom restricted types cannot be converted to parent text content here.");
  String type=declaration.getAttribute("type");
  if(type.isEmpty())type="xs:string";
  Document doc=parentType.getOwnerDocument();
  Element content=doc.createElementNS(XS,"xs:simpleContent");content.setAttribute("xmlns:xs",XS);
  Element extension=doc.createElementNS(XS,"xs:extension");extension.setAttribute("base",type);copyNamespaces(declaration,extension);content.appendChild(extension);
  List<Element> attributes=descendants(parentType,"attribute");
  for(Element a:attributes)if(a!=selected)extension.appendChild(a);
  while(parentType.hasChildNodes())parentType.removeChild(parentType.getFirstChild());
  parentType.appendChild(content);selected=parentElement;
 }
 public File save() throws Exception {
  for(Document doc:documents.values()) {
   TransformerFactory factory=TransformerFactory.newInstance("com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl",null);factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING,true);
   factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD,"");factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET,"");
   Transformer writer=factory.newTransformer();writer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");writer.setOutputProperty(OutputKeys.INDENT,"yes");
   writer.transform(new DOMSource(doc),new StreamResult(output.get(doc).toFile()));
  }
  File result=output.get(documents.get(main)).toFile();
  SchemaFactory schemas=SchemaFactory.newInstance(XS,"com.sun.org.apache.xerces.internal.jaxp.validation.XMLSchemaFactory",null);schemas.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING,true);
  schemas.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD,"");schemas.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA,"file");
  schemas.newSchema(result);
  return result;
 }
 public File getWorkspace(){return workspace.toFile();}
 private void load(Path path,Set<Path> loading) throws Exception {
  if(documents.containsKey(path))return;
  if(documents.size()>=64||Files.size(path)>8*1024*1024)throw new IOException("Schema bundle exceeds the editor size limit.");
  DocumentBuilderFactory f=DocumentBuilderFactory.newInstance("com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl",null);f.setNamespaceAware(true);
  f.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);f.setXIncludeAware(false);f.setExpandEntityReferences(false);
  f.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD,"");f.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA,"");
  Document doc=f.newDocumentBuilder().parse(path.toFile());
  if(!XS.equals(doc.getDocumentElement().getNamespaceURI())||!"schema".equals(doc.getDocumentElement().getLocalName()))throw new IOException("This structure is not an XSD schema.");
  documents.put(path,doc);
  for(Element e:children(doc.getDocumentElement())) {
   if("redefine".equals(e.getLocalName()))throw new IOException("XSD redefine is not supported by this editor.");
   if("include".equals(e.getLocalName())||"import".equals(e.getLocalName())) {
    if(!e.hasAttribute("schemaLocation"))throw new IOException("Import needs a project-local schemaLocation.");
    Path dependency=resolve(path,e.getAttribute("schemaLocation"));load(dependency,loading);
   }
  }
 }
 private Path resolve(Path parent,String value) throws Exception {
  URI uri=new URI(value.replace(" ","%20"));
  if(uri.isAbsolute()||uri.getQuery()!=null||uri.getFragment()!=null)throw new IOException("Only relative project-local XSD dependencies can be edited.");
  Path result=parent.getParent().resolve(uri.getPath()).normalize().toRealPath();
  if(!result.startsWith(main.getParent()))throw new IOException("XSD dependency is outside the project schema folder.");
  return result;
 }
 private void rewriteLocations() throws Exception {
  for(Map.Entry<Path,Document> entry:documents.entrySet())for(Element e:children(entry.getValue().getDocumentElement()))
   if(e.hasAttribute("schemaLocation")){Document dependency=documents.get(resolve(entry.getKey(),e.getAttribute("schemaLocation")));e.setAttribute("schemaLocation",output.get(dependency).getFileName().toString());}
 }
 private Element findGlobal(String kind,String ns,String name) throws IOException {
  Element found=null;
  for(Document doc:documents.values()) {
   String tns=doc.getDocumentElement().getAttribute("targetNamespace");
   if(!tns.equals(ns))continue;
   for(Element e:children(doc.getDocumentElement()))if(kind.equals(e.getLocalName())&&name.equals(e.getAttribute("name"))) {
    if(found!=null)throw new IOException("Ambiguous schema declaration: "+name);found=e;
   }
  }
  if(found==null)throw new IOException("Cannot locate schema declaration: "+name);
  return found;
 }
 private Element dereference(Element e) throws Exception {
  if(!e.hasAttribute("ref"))return e;
  String ref=e.getAttribute("ref");return findGlobal(e.getLocalName(),uri(e,ref),local(ref));
 }
 private Element typeOf(Element e) throws Exception {
  for(Element c:children(e))if("complexType".equals(c.getLocalName())||"simpleType".equals(c.getLocalName()))return c;
  String type=e.getAttribute("type");if(type.isEmpty()||XS.equals(uri(e,type)))return null;
  try{return findGlobal("complexType",uri(e,type),local(type));}catch(IOException ex){return findGlobal("simpleType",uri(e,type),local(type));}
 }
 private Element editableType(Element element) throws Exception {
  if(element.hasAttribute("ref")) {
   Element declaration=dereference(element);
   Element copy=copyWithNamespaces(declaration,element.getOwnerDocument());
   copy.removeAttribute("minOccurs");copy.removeAttribute("maxOccurs");
   for(String a:new String[]{"minOccurs","maxOccurs"})if(element.hasAttribute(a))copy.setAttribute(a,element.getAttribute(a));
   if(!element.getOwnerDocument().getDocumentElement().getAttribute("targetNamespace").equals(namespaceOf(declaration)))throw new IOException("Editing inside a cross-namespace element reference is not supported.");
   copy.setAttribute("form","qualified");element.getParentNode().replaceChild(copy,element);
   if(selected==element)selected=copy;if(parentElement==element)parentElement=copy;element=copy;
  }
  Element type=typeOf(element);
  if(type==null||!"complexType".equals(type.getLocalName()))throw new IOException("Selected path does not have editable complex content.");
  if(type.getParentNode()!=element) {
   if(!type.getOwnerDocument().getDocumentElement().getAttribute("targetNamespace").equals(element.getOwnerDocument().getDocumentElement().getAttribute("targetNamespace")))throw new IOException("Editing inside a cross-namespace type requires the schema editor.");
   Element copy=copyWithNamespaces(type,element.getOwnerDocument());copy.removeAttribute("name");
   element.removeAttribute("type");element.appendChild(copy);type=copy;
  }
  if(child(type,"complexContent")!=null)throw new IOException("Inherited complex content must be edited in the schema editor.");
  return type;
 }
 private Element findChild(Element container,Step step,Set<Element> seen) throws Exception {
  List<Element> matches=new ArrayList<>();collect(container,step,matches,seen);
  if(matches.size()!=1)throw new IOException(matches.isEmpty()?"Cannot locate selected XSD node: "+step.name:"Ambiguous XSD node: "+step.name);
  return matches.get(0);
 }
 private void collect(Element parent,Step step,List<Element> found,Set<Element> seen) throws Exception {
  if(!seen.add(parent))throw new IOException("Recursive group cannot be edited here.");
  for(Element e:children(parent)) {
   String kind=e.getLocalName();
   if("element".equals(kind)||"attribute".equals(kind)) {
    if(step.attribute!="attribute".equals(kind))continue;
    String name=e.hasAttribute("ref")?local(e.getAttribute("ref")):e.getAttribute("name");
    String ns=e.hasAttribute("ref")?uri(e,e.getAttribute("ref")):namespaceOf(e);
    if(name.equals(step.name)&&ns.equals(step.namespace))found.add(e);
   } else if("sequence".equals(kind)||"choice".equals(kind)||"all".equals(kind)||"simpleContent".equals(kind)||"extension".equals(kind)||"restriction".equals(kind))collect(e,step,found,seen);
   else if("group".equals(kind)||"attributeGroup".equals(kind))throw new IOException("Referenced groups must be edited in the schema editor.");
  }
  seen.remove(parent);
 }
 private String namespaceOf(Element e) {
  Element schema=e.getOwnerDocument().getDocumentElement();
  if(e.getParentNode()==schema)return schema.getAttribute("targetNamespace");
  String form=e.getAttribute("form");if(form.isEmpty())form=schema.getAttribute("attribute".equals(e.getLocalName())?"attributeFormDefault":"elementFormDefault");
  return "qualified".equals(form)?schema.getAttribute("targetNamespace"):"";
 }
 private Element scalarType(Element e) throws Exception {Element t=typeOf(e);if(t!=null&&"complexType".equals(t.getLocalName()))throw new IOException("Complex scalar type conversion requires the schema editor.");return t;}
 private void copyScalarType(Element source,Element target) throws Exception {
  Element type=scalarType(source);
  if(type!=null){Element c=copyWithNamespaces(type,target.getOwnerDocument());c.removeAttribute("name");target.appendChild(c);}
  else {String value=source.getAttribute("type");target.setAttribute("type",value.isEmpty()?"xs:string":value);copyNamespaces(source,target);}
 }
 private static Element firstContent(Element e) throws IOException {for(Element c:children(e))if("extension".equals(c.getLocalName())||"restriction".equals(c.getLocalName()))return c;throw new IOException("Invalid simpleContent.");}
 private static boolean findNamedAttribute(Element e,String name){for(Element a:descendants(e,"attribute"))if(name.equals(a.getAttribute("name")))return true;return false;}
 private static Element copyWithNamespaces(Element source,Document target){Element copy=(Element)target.importNode(source,true);copyNamespaces(source,copy);return copy;}
 private static void copyNamespaces(Element source,Element target){
  Set<String> seen=new HashSet<>();for(Node n=source;n instanceof Element;n=n.getParentNode()){
   NamedNodeMap attrs=n.getAttributes();for(int i=0;i<attrs.getLength();i++){Node a=attrs.item(i);if(XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(a.getNamespaceURI())&&seen.add(a.getNodeName()))target.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI,a.getNodeName(),a.getNodeValue());}
  }
  target.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI,"xmlns:xs",XS);
 }
 private static String uri(Element e,String q){int i=q.indexOf(':');String ns=e.lookupNamespaceURI(i<0?null:q.substring(0,i));return ns==null?"":ns;}
 private static String local(String q){return q.substring(q.indexOf(':')+1);}
 private static int number(Element e,String key,int fallback){String v=e.getAttribute(key);return v.isEmpty()?fallback:Integer.parseInt(v);}
 private static List<Element> children(Element e){List<Element> r=new ArrayList<>();for(Node n=e.getFirstChild();n!=null;n=n.getNextSibling())if(n instanceof Element&&XS.equals(n.getNamespaceURI()))r.add((Element)n);return r;}
 private static Element child(Element e,String name){for(Element c:children(e))if(name.equals(c.getLocalName()))return c;return null;}
 private static List<Element> descendants(Element e,String name){List<Element> r=new ArrayList<>();for(Element c:children(e)){if(name.equals(c.getLocalName()))r.add(c);else if(!"element".equals(c.getLocalName())&&!"attribute".equals(c.getLocalName()))r.addAll(descendants(c,name));}return r;}
 @Override public void close() throws IOException {if(workspace!=null&&Files.exists(workspace))try(java.util.stream.Stream<Path> files=Files.walk(workspace)){for(Path p:(Iterable<Path>)files.sorted(Comparator.reverseOrder())::iterator)Files.deleteIfExists(p);}}
}
