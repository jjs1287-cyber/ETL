$ErrorActionPreference='Stop'
Add-Type -AssemblyName System.Drawing
$root='C:\ETL\Studio\src\main\resources\etlforge-integration-client-icons\org\etlforge\integration\client\ui\icons'
foreach($spec in @(@('ETLForge.Logo.png',470,200),@('ETLForge.StartScreen.png',300,92),@('Application16.png',16,16),@('Application32.png',32,32))){
 $width=[int]$spec[1];$height=[int]$spec[2]
 $bitmap=[Drawing.Bitmap]::new($width,$height)
 $graphics=[Drawing.Graphics]::FromImage($bitmap)
 $font=$null;$brush=[Drawing.SolidBrush]::new([Drawing.Color]::FromArgb(25,70,105))
 try{
  $graphics.SmoothingMode=[Drawing.Drawing2D.SmoothingMode]::AntiAlias
  $graphics.TextRenderingHint=[Drawing.Text.TextRenderingHint]::AntiAliasGridFit
  $graphics.Clear([Drawing.Color]::Transparent)
  if($width -lt 40){
   $graphics.FillRectangle($brush,0,0,$width,$height)
   $font=[Drawing.Font]::new('Segoe UI',[single]($height*0.68),[Drawing.FontStyle]::Bold,[Drawing.GraphicsUnit]::Pixel)
   $graphics.DrawString('E',$font,[Drawing.Brushes]::White,[single]($width*0.16),[single]0)
  }else{
   $font=[Drawing.Font]::new('Segoe UI',[single]($width*0.13),[Drawing.FontStyle]::Bold,[Drawing.GraphicsUnit]::Pixel)
   $graphics.DrawString('ETLForge',$font,$brush,[single]($width*0.035),[single]($height*0.16))
   $font.Dispose();$font=[Drawing.Font]::new('Segoe UI',[single]($width*0.047),[Drawing.FontStyle]::Regular,[Drawing.GraphicsUnit]::Pixel)
   $graphics.DrawString('WINDOWS x64',$font,$brush,[single]($width*0.045),[single]($height*0.67))
  }
  $bitmap.Save((Join-Path $root $spec[0]),[Drawing.Imaging.ImageFormat]::Png)
 }finally{if($font){$font.Dispose()};$brush.Dispose();$graphics.Dispose();$bitmap.Dispose()}
}

