using System; using System.IO; using System.IO.Compression; using System.Text;
public static class SetupStreamReader {
 public static void Extract(string path,string dest) {
 Directory.CreateDirectory(dest); using(var r=new BinaryReader(File.OpenRead(path))) {
 if(Encoding.ASCII.GetString(r.ReadBytes(14)).TrimEnd('\0')!="ISSetupStream")throw new Exception("signature");
 int count=r.ReadUInt16(); uint type=r.ReadUInt32(); r.BaseStream.Position=46;
 for(int n=0;n<count;n++) {
 int nl=r.ReadInt32(); uint flags=r.ReadUInt32(); r.ReadUInt16(); int size=r.ReadInt32(); r.ReadBytes(8); int packed=r.ReadUInt16();
 if(nl<1||nl>520||size<0)throw new Exception("header");
 if(type==4)r.ReadBytes(24); string name=Encoding.Unicode.GetString(r.ReadBytes(nl)).TrimEnd('\0');
 string target=Path.GetFullPath(Path.Combine(dest,name)); if(!target.StartsWith(Path.GetFullPath(dest)+Path.DirectorySeparatorChar,StringComparison.OrdinalIgnoreCase))throw new Exception("path");
 byte[] data=r.ReadBytes(size); if(data.Length!=size)throw new Exception("truncated");
 byte[] seed=Encoding.UTF8.GetBytes(name); byte[] magic={0x13,0x35,0x86,7};
 if((flags&6)!=0)for(int i=0;i<data.Length;i++){int k=((flags&4)!=0?i%1024:i)%seed.Length;data[i]=(byte)~((seed[k]^magic[k%4])^((data[i]<<4)|(data[i]>>4)));}
 Directory.CreateDirectory(Path.GetDirectoryName(target));
 if(packed!=0){using(var input=new MemoryStream(data))using(var z=new ZLibStream(input,CompressionMode.Decompress))using(var output=File.Create(target))z.CopyTo(output);}else File.WriteAllBytes(target,data);
 Console.WriteLine(name+" "+new FileInfo(target).Length);
 }
 }
 }
}
