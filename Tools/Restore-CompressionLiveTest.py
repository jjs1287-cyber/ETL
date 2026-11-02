from pathlib import Path
import re
p=Path('C:/ETL/Server/tests/integration/EndpointCompressionLiveTest.java');s=p.read_text(encoding='utf-8');s=re.sub(r'/\*.*?\*/\s*','',s,flags=re.S)
a=s.index('    static void setup()');b=s.index('        Files.copy(Paths.get(',a)
s=s[:a]+'''    static void setup() throws Exception {
        String string;String[] stringArray;
        Files.createDirectories(ROOT);Files.write(ROOT.resolve("alpha.dat"),A);Files.write(ROOT.resolve("beta.dat"),B);
        try(ZipOutputStream z=new ZipOutputStream(Files.newOutputStream(ROOT.resolve("input.zip")))){z.putNextEntry(new ZipEntry("nested/alpha.dat"));z.write(A);z.closeEntry();z.putNextEntry(new ZipEntry("beta.dat"));z.write(B);z.closeEntry();}
        tool("a","-tzip","-mem=AES256","-pfixture-password",ROOT.resolve("encrypted.zip").toString(),ROOT.resolve("alpha.dat").toString(),ROOT.resolve("beta.dat").toString());
        tool("a","-t7z",ROOT.resolve("input.7z").toString(),ROOT.resolve("alpha.dat").toString(),ROOT.resolve("beta.dat").toString());
        try(GZIPOutputStream g=new GZIPOutputStream(Files.newOutputStream(ROOT.resolve("payload.txt.gz")))){g.write(A);}
'''+s[b:]
s=s.replace('deflaterOutputStream = new String[]','String[] formats = new String[]').replace('(String)((Object)deflaterOutputStream[i])','formats[i]').replace('(T[])new String[0][]','new String[0][]')
s=s.replace('string = new DeployInterchangeDataServiceLocator()','org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.CDeployResults deployed = new DeployInterchangeDataServiceLocator()').replace('string != null && string.getDeployStatus() <= 1','deployed != null && deployed.getDeployStatus() <= 1')
a=s.index('    public static void main(');b=s.index('    static {',a)
s=s[:a]+''' public static void main(String[] args)throws Exception{
  LocalVariablesRuntimeTest.init();hash=LocalVariablesRuntimeTest.hash;setup();
  for(int n=0;n<15;n++){
   String status=run(n);if(n==7||n==9||n==10){check(status.equals("6"),"invalid archive/password/combined GZIP fails cleanly "+n);try(java.util.stream.Stream<Path> f=Files.list(out(n))){check(f.count()==0,"failure delivered no partial output "+n);}continue;}
   check(status.equals("3"),"operation succeeded "+n);
   if(n==11){bytes(out(n).resolve("copy.zip"),Files.readAllBytes(ROOT.resolve("input.zip")),"compression disabled preserves original ZIP");continue;}
   if(n==13){bytes(out(n).resolve("entry_payload.txt"),A,"GZIP auto-detection and original filename");continue;}
   if(n==14){bytes(out(n).resolve("entry_input.txt"),"hello".getBytes(StandardCharsets.UTF_8),"TAR.GZ auto-detection handles ./ paths");continue;}
   if(n==0||n==8||n==12){bytes(out(n).resolve("entry_alpha.dat"),A,"extracted Unicode text "+n);bytes(out(n).resolve("entry_beta.dat"),B,"extracted binary bytes "+n);continue;}
   if(n==2||n==6){String ext=n==2?"zip":"gz";for(String leaf:new String[]{"alpha","beta"}){Path unpacked=ROOT.resolve("verify-"+n+"-"+leaf);unpack(out(n).resolve("entry_"+leaf+".dat."+ext),unpacked,"");bytes(unpacked.resolve("entry_"+leaf+".dat"),leaf.equals("alpha")?A:B,"independent individual archive bytes "+n+" "+leaf);}continue;}
   String ext=n==3?"7z":n==5?"tar.gz":"zip";Path unpacked=ROOT.resolve("verify-"+n);unpack(out(n).resolve("bundle."+ext),unpacked,n==4?"fixture-password":"");
   if(n==5){Path tar;try(java.util.stream.Stream<Path> f=Files.list(unpacked)){tar=f.findFirst().get();}Path second=ROOT.resolve("verify-tar");unpack(tar,second,"");unpacked=second;}
   bytes(unpacked.resolve("entry_alpha.dat"),A,"independent combined archive text "+n);bytes(unpacked.resolve("entry_beta.dat"),B,"independent combined archive binary "+n);
  }
  check(Files.exists(ROOT.resolve("input.zip")),"source archive preserved");System.out.println("Passed "+checks+" live compression checks; fixtures="+ROOT);
 }
'''+s[b:];p.write_text(s,encoding='utf-8')
