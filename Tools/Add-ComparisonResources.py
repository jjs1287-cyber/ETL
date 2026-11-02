from pathlib import Path
p=Path('C:/ETL/Studio/src/main/java/org/etlforge/integration/client/project/compare/ProjectComparison.java');s=p.read_text(encoding='utf-8');s=s.replace(' private ProjectComparison(){}',''' public static List<Difference> compare(ManagedProject left,ManagedProject right)throws Exception{
  List<Difference> result=compare(left.getProject(),right.getProject());
  Map<String,Difference> byName=new HashMap<>();
  SortedMap<String,IntegrationEntity> a=objects(left.getProject()),b=objects(right.getProject());
  for(String key:a.keySet())if(b.containsKey(key)){
   IntegrationEntity x=a.get(key),y=b.get(key);SortedMap<String,String> l=resources(left,x),r=resources(right,y);
   if(!l.equals(r))result.add(new Difference(x.getName()+" (resources)",x.getEntityType().getDisplayName(),"Changed",l,r));
  }
  return result;
 }
 private static SortedMap<String,String> resources(ManagedProject project,IntegrationEntity e)throws Exception{
  Path root=new java.io.File(new java.io.File(new java.io.File(ProjectFolders.getSettingsFolder(project.getLocation()).getLocation(),"Entity"),e.getEntityType().name()),"Resources/"+e.getID()).toPath();
  SortedMap<String,String> result=new TreeMap<>();if(!Files.exists(root))return result;
  try(java.util.stream.Stream<Path> paths=Files.walk(root)){for(Path p:(Iterable<Path>)paths::iterator){if(Files.isSymbolicLink(p))throw new java.io.IOException("Resource links cannot be compared");if(!Files.isRegularFile(p))continue;String name=root.relativize(p).toString().replace('\\\\','/');if(name.equals("GraphLayout.xml")||name.startsWith(".konga."))continue;result.put("Resource/"+name,"SHA256 "+hash(Files.readAllBytes(p)));}}
  return result;
 }
 private ProjectComparison(){}''');p.write_text(s,encoding='utf-8')
