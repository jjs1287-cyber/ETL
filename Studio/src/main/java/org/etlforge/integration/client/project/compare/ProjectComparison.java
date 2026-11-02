package org.etlforge.integration.client.project.compare;

import java.util.*;
import java.nio.file.*;
import java.security.MessageDigest;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import java.io.StringReader;
import org.etlforge.integration.client.project.*;
import org.etlforge.integration.data.entity.*;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.util.persist.XmlPersistor;

public final class ProjectComparison {
 public static final class Difference {
  public final String object,type,change;
  public final SortedMap<String,String> left,right;
  Difference(String object,String type,String change,SortedMap<String,String> left,SortedMap<String,String> right){this.object=object;this.type=type;this.change=change;this.left=left;this.right=right;}
 }
 public static String identity(IntegrationEntity e){LinkedList<String> parts=new LinkedList<>();for(IntegrationEntity p=e;p!=null&&!(p instanceof IntegrationProject);p=p.getParent())parts.addFirst(p instanceof RootFolder?((RootFolder)p).getItemType().name():p.getName());return e.getEntityType().name()+":"+String.join("/",parts);}
 private static Map<String,String> identities(IntegrationProject p){Map<String,String> map=new HashMap<>();map.put(p.getID().toString(),"[project]");for(IntegrationEntity e:p.getAllEntities())map.put(e.getID().toString(),"["+identity(e)+"]");return map;}
 private static String normalize(String text,Map<String,String> ids){for(Map.Entry<String,String> e:ids.entrySet())text=text.replace(e.getKey(),e.getValue());return text.replace("\r\n","\n");}
 private static boolean secret(String s){return s.toLowerCase(Locale.ROOT).matches(".*(password|passphrase|secret|token|privatekey|access_key).*");}
 private static String hash(byte[] bytes)throws Exception{StringBuilder s=new StringBuilder();for(byte b:MessageDigest.getInstance("SHA-256").digest(bytes))s.append(String.format("%02x",b&255));return s.toString();}
 private static void flatten(Element e,String prefix,SortedMap<String,String> out,Map<String,String> ids,boolean masked)throws Exception{
  String n=e.getTagName();if(n.equals("Item")&&Arrays.asList("IntegrationEntity#CreatedAt","IntegrationEntity#MofidiedAt","IntegrationEntity#DeployDirty","IntegrationEntity#Transient","Description").contains(e.getAttribute("key")))return;if(n.equals("GraphLayout")||n.equals("operationgraphlayouttab"))return;
  String path=prefix+"/"+n+(e.hasAttribute("key")?"["+e.getAttribute("key")+"]":e.hasAttribute("name")?"["+e.getAttribute("name")+"]":"");NamedNodeMap attrs=e.getAttributes();boolean hide=masked||secret(n);
  for(String k:new String[]{"key","name","Name"})if(e.hasAttribute(k)&&secret(e.getAttribute(k)))hide=true;
  for(int i=0;i<attrs.getLength();i++){Node a=attrs.item(i);String k=a.getNodeName();if(n.equals("Header")&&Arrays.asList("ID","Deployed","DeployDirty","HasMoved").contains(k))continue;String value=normalize(a.getNodeValue(),ids);out.put(path+"/@"+k,(hide||secret(k))?"[protected SHA256 "+hash(value.getBytes(java.nio.charset.StandardCharsets.UTF_8))+"]":value);}
  Map<String,Integer> indexes=new HashMap<>();StringBuilder text=new StringBuilder();boolean children=false;
  for(Node c=e.getFirstChild();c!=null;c=c.getNextSibling())if(c instanceof Element){children=true;Element child=(Element)c;int index=indexes.getOrDefault(child.getTagName(),0);indexes.put(child.getTagName(),index+1);flatten(child,path+((child.hasAttribute("key")||child.hasAttribute("name"))?"":"["+index+"]"),out,ids,hide);}else if(c.getNodeType()==Node.TEXT_NODE||c.getNodeType()==Node.CDATA_SECTION_NODE)text.append(c.getNodeValue());
  if(!children||n.equals("konga.string")){String value=normalize(text.toString(),ids);out.put(path,hide?"[protected SHA256 "+hash(value.getBytes(java.nio.charset.StandardCharsets.UTF_8))+"]":value);}
 }
 public static SortedMap<String,String> definition(IntegrationEntity entity,IntegrationProject project)throws Exception{
  XmlPersistor p=XmlPersistor.newRoot("Object");entity.writeTo(p);
  DocumentBuilderFactory f=DocumentBuilderFactory.newInstance();f.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);f.setExpandEntityReferences(false);
  Document d=f.newDocumentBuilder().parse(new InputSource(new StringReader(p.toXml())));
  SortedMap<String,String> result=new TreeMap<>();flatten(d.getDocumentElement(),"",result,identities(project),entity instanceof ProjectVariable&&((ProjectVariable)entity).isPassword());return result;
 }
 private static SortedMap<String,IntegrationEntity> objects(IntegrationProject p){SortedMap<String,IntegrationEntity> map=new TreeMap<>();for(IntegrationEntity e:p.getAllEntities())if(!(e instanceof Folder)&&!(e instanceof BuiltInIntegrationEntity)){String key=identity(e);if(map.put(key,e)!=null)throw new IllegalArgumentException("Ambiguous object: "+key);}return map;}
 public static List<Difference> compare(IntegrationProject left,IntegrationProject right)throws Exception{
  SortedMap<String,IntegrationEntity> a=objects(left),b=objects(right);SortedSet<String> keys=new TreeSet<>(a.keySet());keys.addAll(b.keySet());List<Difference> result=new ArrayList<>();
  for(String key:keys){IntegrationEntity x=a.get(key),y=b.get(key),item=x==null?y:x;SortedMap<String,String> l=x==null?new TreeMap<>():definition(x,left),r=y==null?new TreeMap<>():definition(y,right);if(!l.equals(r))result.add(new Difference(item.getName(),item.getEntityType().getDisplayName(),x==null?"Right only":y==null?"Left only":"Changed",l,r));}return result;
 }
 public static List<Difference> compare(ManagedProject left,ManagedProject right)throws Exception{
  List<Difference> result=compare(left.getProject(),right.getProject());
  SortedMap<String,IntegrationEntity> a=objects(left.getProject()),b=objects(right.getProject());
  for(String key:a.keySet())if(b.containsKey(key)){
   IntegrationEntity x=a.get(key),y=b.get(key);SortedMap<String,String> l=resources(left,x),r=resources(right,y);
   if(!l.equals(r))result.add(new Difference(x.getName()+" (resources)",x.getEntityType().getDisplayName(),"Changed",l,r));
  }
  SortedMap<String,String> assetsLeft=files(ProjectFolders.getFileStoreRootFolder(left.getLocation()).toPath()),assetsRight=files(ProjectFolders.getFileStoreRootFolder(right.getLocation()).toPath());if(!assetsLeft.equals(assetsRight))result.add(new Difference("Project files","Resources","Changed",assetsLeft,assetsRight));
  return result;
 }
 private static SortedMap<String,String> resources(ManagedProject project,IntegrationEntity e)throws Exception{
  Path root=new java.io.File(new java.io.File(new java.io.File(ProjectFolders.getSettingsFolder(project.getLocation()).asJavaFile(),"Entity"),e.getEntityType().name()),"Resources/"+e.getID()).toPath();
  return files(root);
 }
 private static SortedMap<String,String> files(Path root)throws Exception{
  SortedMap<String,String> result=new TreeMap<>();if(!Files.exists(root))return result;
  try(java.util.stream.Stream<Path> paths=Files.walk(root)){for(Path p:(Iterable<Path>)paths::iterator){if(Files.isSymbolicLink(p)||Files.readAttributes(p,java.nio.file.attribute.BasicFileAttributes.class,LinkOption.NOFOLLOW_LINKS).isOther())throw new java.io.IOException("Resource links cannot be compared");if(!Files.isRegularFile(p))continue;String name=root.relativize(p).toString().replace('\\','/');if(name.equals("GraphLayout.xml")||name.startsWith(".konga."))continue;result.put("Resource/"+name,"SHA256 "+fileHash(p));}}
  return result;
 }
 private static String fileHash(Path path)throws Exception{MessageDigest digest=MessageDigest.getInstance("SHA-256");try(java.io.InputStream in=Files.newInputStream(path)){byte[] buffer=new byte[65536];int n;while((n=in.read(buffer))!=-1)digest.update(buffer,0,n);}StringBuilder s=new StringBuilder();for(byte b:digest.digest())s.append(String.format("%02x",b&255));return s.toString();}
 private ProjectComparison(){}
}

