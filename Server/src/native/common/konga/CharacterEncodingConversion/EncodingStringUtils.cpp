/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include "EncodingStringUtils.h"

namespace konga {

EncodingStringUtils::EncodingStringUtils(void)
{
}

EncodingStringUtils::~EncodingStringUtils(void)
{
}

bool EncodingStringUtils::IsUtf8LeadingByte(const unsigned char c)  
{
	bool is= (c<=0x7F || c>=0xC0);
	return is;
}

std::size_t EncodingStringUtils::Utf8CharacterSize(const std::string& utf8string)  
{                              
	std::size_t size=0;
	for(size_t idx=0;idx<utf8string.size();idx++)
	{
		if (IsUtf8LeadingByte(utf8string[idx])) size++;
	}
	return size;
}

std::size_t EncodingStringUtils::Utf8ByteLengthRight(const std::string& utf8string,const std::size_t characterlength)  
{
	if(characterlength==0) return 0;
	if(characterlength>=utf8string.size()||utf8string.size()==0) return utf8string.size();

	std::size_t bytelength=0;
	std::size_t howmaycharacters=0;
	for(size_t idx=utf8string.size ();idx>0;idx--)
	{
		bytelength++;
		if (IsUtf8LeadingByte(utf8string[idx-1])) howmaycharacters++;
		if (characterlength==howmaycharacters) break;
	}
	return bytelength;
}

std::size_t EncodingStringUtils::Utf8ByteLength(const std::string& utf8string,const std::size_t startbyteposition,const std::size_t characterlength)  
{                              
	if (startbyteposition ==std::string::npos || startbyteposition>=utf8string.size ()|| characterlength==0) 
	{
		return 0;
	}
	std::size_t leftbytes=utf8string.size () - startbyteposition;
	if (characterlength >=(leftbytes))
	{
		return leftbytes;                         
	}

	std::size_t bytelength=0;
	std::size_t howmaycharacters=0;
	for(size_t idx=startbyteposition;idx<utf8string.size ();idx++)
	{
		bytelength++;
		if (IsUtf8LeadingByte(utf8string[idx])) howmaycharacters++;
		if (howmaycharacters==characterlength) break;
	}
	                                                                          
	for(size_t idx=startbyteposition+bytelength;idx<utf8string.size ();idx++)
	{
		if (IsUtf8LeadingByte(utf8string[idx])) break;
		bytelength++;
	}
	return bytelength;
}

std::string EncodingStringUtils::Utf8Truncate(const std::string& utf8string,const int nfirstcharacter,const int nlastcharacter)  
{
	if ( nlastcharacter<0 || nfirstcharacter<0 || utf8string.size()==0 ) return "";
	std::size_t nfirstbyte,nlastbyte;
	nfirstbyte = Utf8ByteLength(utf8string,0,nfirstcharacter);
	if ((nfirstbyte >= utf8string.size()) ) return "";
	nlastbyte = Utf8ByteLengthRight(utf8string,nlastcharacter);

	if ((nlastbyte >= utf8string.size())||( nlastbyte >= ( utf8string.size() - nfirstbyte) )) return "";

	std::size_t bytelength= utf8string.size() - nfirstbyte - nlastbyte;
	std::string substr=utf8string.substr(nfirstbyte,bytelength);;
	return substr;
}


std::string EncodingStringUtils::Utf8SubString(const std::string& utf8string,const int startcharacterposition,const int characterlength)  
{
	if(characterlength<=0 || startcharacterposition<0 || utf8string.size()==0 ) return "";
	
	std::size_t startbyteposition=Utf8ByteLength(utf8string,0,startcharacterposition);
	if(startbyteposition>=utf8string.size()) return "";

	const std::size_t bytelength=Utf8ByteLength(utf8string,startbyteposition,characterlength);
	std::string substr = utf8string.substr(startbyteposition,bytelength);
	return substr;
}

std::string EncodingStringUtils::Utf8Left(const std::string& utf8string,const int characterlength) 
{
	if (characterlength<=0) return "";
	return Utf8SubString(utf8string,0,characterlength);
}

std::string EncodingStringUtils::Utf8Right(const std::string& utf8string,const int characterlength)  
{
	if (characterlength<=0) return "";
	std::size_t bytelength=Utf8ByteLengthRight(utf8string,characterlength);
	if(bytelength==0) return "";
	if(bytelength>=utf8string.size()) return utf8string;

	std::size_t startbyteposition = utf8string.size()- bytelength ; 
	std::string substr=utf8string.substr(startbyteposition,bytelength);;
	return substr;
}



}

