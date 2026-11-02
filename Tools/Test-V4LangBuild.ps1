$ErrorActionPreference='Stop'
$out='C:\ETL\Server\build-probe\v4-lang'
New-Item -ItemType Directory -Force "$out\classes" | Out-Null
Get-ChildItem 'C:\ETL\Reference\v4\jitterbit-code-r45309-branches-v4-4.1.x\common\java\org.jitterbit.lang\src' -Recurse -Filter *.java | ForEach-Object {'"'+$_.FullName.Replace('\','/')+'"'} | Set-Content "$out\sources.txt"
& C:\ETL\Tools\jdk8u504-b01\bin\javac.exe -source 6 -target 6 -cp 'C:\ETL\Reference\v5\studio-linux\opt\jitterbit-studio56\lib\*' -d "$out\classes" "@$out\sources.txt" 2>&1 | Tee-Object "$out\compile.log"
if($LASTEXITCODE -ne 0){throw "Compilation failed: $LASTEXITCODE"}
