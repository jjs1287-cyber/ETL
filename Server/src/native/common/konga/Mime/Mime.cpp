/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#include <konga/Utils/AutoCleanup.h>	                    

#include "Mime.h"

using namespace konga;

  
                                                                                                          
                                           
  

                                             
std::string Mime::ContentTypeFromFileName(const std::string& filename)
{
	std::string contentType = "application/octet-stream";                                                   

	std::string::size_type pos = filename.find_last_of('.');

	if( pos == std::string::npos ) return contentType;
	std::string extension = filename.substr(pos);
  
             
                                                                                                 
                       

                                
                                   
                                                                                              
  
                        

    
                                                                                     
                                                                      
    
                  
                                                      
                                                                                                       
                            
                      
                                              

                               
                     
                                        
                 
                     
                 
                                                                                                             
   
                                   
                                             
                                                                                                                   
        
                                                                                                                   
                                                         
     
                                    
                     
     
                                   
    
   

                     
  

                                  
 
                       
   
	return contentType;
}

std::string Mime::EncodeBase64(const std::string& filename)
{
	std::string res;

	FILE* in = fopen(filename.c_str(),"rb");
	if( !in )
	{
		char s[1000];
		sprintf(s,"Failed to open file \"%s\" for reading",filename.c_str());
		throw EncodingException(s);
	}

	fseek(in,0,SEEK_END);
	long size = ftell(in);
	fseek(in,0,SEEK_SET);
	res.reserve( static_cast<std::string::size_type>(size*1.7) );

	const char base64code[]="ABCDEFGHIJKLMNOPQRSTUVWXYZ"
		                    "abcdefghijklmnopqrstuvwxyz"
							"0123456789+/";
	#define ENCODE_BYTE(b) base64code[((unsigned char)b)&077];
	   
	#define buffer_length 864
	#define byte_per_line 54
	                                                  
	                                                     
	unsigned char buf[buffer_length+10];
	char chr[5];
	chr[4]=0;
	int k,ibyte,k2;
	while( (k= static_cast<int>(fread(buf,1,buffer_length,in))) > 0 )
	{
		ibyte=0;
		k2=k-2;
		while(ibyte<k2)
		{
			chr[0]=ENCODE_BYTE(buf[ibyte]>>2);
			chr[1]=ENCODE_BYTE(((buf[ibyte]&3)<<4)|((buf[ibyte+1]&240)>>4));
			chr[2]=ENCODE_BYTE(((buf[ibyte+1]&15)<<2)|((buf[ibyte+2]&192)>>6));
			chr[3]=ENCODE_BYTE(buf[ibyte+2]);
			                                                       
			res += chr;
			ibyte+=3;
			if((ibyte%byte_per_line)==0) res += "\n";                      
		}
		if(ibyte<k)
		{
			buf[k]=buf[k+1]=0;
			chr[0]=ENCODE_BYTE(buf[ibyte]>>2);
			chr[1]=ENCODE_BYTE(((buf[ibyte]&3)<<4)|((buf[ibyte+1]&240)>>4));
			if((ibyte+1)<k){
				chr[2]=ENCODE_BYTE(((buf[ibyte+1]&15)<<2)|((buf[ibyte+2]&192)>>6));
			} else {
				chr[2]='=';
			}
			chr[3]='=';
			                                                         
			res += chr;
		}
	}
	fclose(in);
	return res;
}

std::string Mime::EncodeQuotedPrintable(const std::string& filename)
{
	std::string res;

	FILE* in = fopen(filename.c_str(),"r");
	if( !in )
	{
		char s[1000];
		sprintf(s,"Failed to open file \"%s\" for reading",filename.c_str());
		throw EncodingException(s);
	}

	fseek(in,0,SEEK_END);
	long size = ftell(in);
	fseek(in,0,SEEK_SET);
	res.reserve( size*2 );

	char buf[4096],cbuf[10],*s;
	while( fgets(buf,4096,in) )
	{		
		s=buf;
		for(int i=0;*s;++s,++i)
		{
			if( i>73 )
				res += "=\n", i=0;
			if( *s == '=' )
			{
				res += "=3D";
				i+=2;
				continue;
			}
			if( isalnum(*s) || strchr("\"()+,-./:=?",*s) ||
				*s == '\t' || *s == ' ' )
			{
				res += *s;
				continue;
			}
			if( *s == '\r' )
			{
				res += '\r';
				if(!++s) break;
			}
			if( *s == '\n' )
			{
				res += '\n', i=0;
				continue;
			}
			sprintf(cbuf,"=%.2X",(unsigned char)*s);
			res += cbuf;
			i+=2;
		}
	}

	fclose(in);
	return res;
}
