/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <stdio.h>	           

#include <boost/algorithm/string/case_conv.hpp>	                     
#include <boost/thread/mutex.hpp>

#include "PortDefs.h"		                          
#include "StringUtils.h"	         
#include "Settings.h"		                           
#include "RCStringTable.h"
#include "ErrorResolver.h"
#include "CrException.h"

#include "KString.h"

using std::string;

                                           
std::map<int,std::string> KString::m_mapStringTable;
bool KString::m_bInitialized=false;

KString::KString(const KString& cs) : m_buf(0), m_lock(false) {
	if(cs.m_lock&&cs.m_buf){
		m_s.assign(cs.m_buf);
	}
	else{
		m_s.assign(cs.m_s);
	}
}

KString::KString(int i) : m_s(konga::toString(i)), m_buf(0), m_lock(false) {}

KString::KString(size_t n) : m_s(konga::toString(n)), m_buf(0), m_lock(false) {}

void KString::Format( const char* pszFormat, ... )
{
	va_list argList;
	va_start( argList, pszFormat );
	FormatV( pszFormat, argList );
	va_end( argList );
}

namespace {
	void ThrowPrintfError(long lErr, const string& sFormat) {
		string sErr = ErrorResolver().Resolve(lErr);
		throw konga::CrException("Internal error: KString::FormatV failed to format string using format string \""
			+ sFormat + "\".\nPlease report this bug! The system reported the error: " + sErr);
	}

	const int maxFormatVBufSize = 100;
}

void KString::FormatV( const char* pszFormat, va_list args )
{
	ReleaseBuffer(0);	                 
	if(!pszFormat) throw konga::CrException("KString::FormatV - format string is NULL");

	                                                                                                
#ifdef WIN32
	int nCharsToWrite = _vscprintf(pszFormat,args);
	if ( nCharsToWrite < 0 ) {
		ThrowPrintfError(GetLastError(), pszFormat);
	}
	char* buf = new char[nCharsToWrite+1];
	vsprintf(buf, pszFormat, args);
#else
	char* buf = new char[maxFormatVBufSize];
	const int nSize = vsnprintf(buf, maxFormatVBufSize, pszFormat, args);
	if ( nSize < 0 ) {
		delete[] buf;
		ThrowPrintfError(GetLastError(), pszFormat);
	}
	else if ( nSize >= maxFormatVBufSize ) {
		delete[] buf;
		buf = new char[nSize+1];
		vsprintf(buf, pszFormat, args);
	}
#endif

	m_s = buf;
	delete[] buf;
}

namespace {
	                                                       
	boost::mutex g_StringTableInitMutex;
}

bool KString::LoadString( unsigned int nID ){
	{                      
		boost::mutex::scoped_lock lock(g_StringTableInitMutex);
		if( !m_bInitialized ){
			konga::RCStringTable rcs;
			const std::string stRCFileName( konga::GetServerHomeWithSlash()+"Resources/TransformationEngine/MapTransform.rc" );
			if( !rcs.Parse(stRCFileName, m_mapStringTable) ){
				m_mapStringTable.clear();
				throw konga::CrException("Server Configuration Error: Failed to parse MapTransform resource file. " \
					"\"" + stRCFileName + "\". Reason: " + rcs.getErrorString());
			}
			m_bInitialized = true;
		}
	}                           

	std::map<int,std::string>::const_iterator it=m_mapStringTable.find(nID);
    if(it == m_mapStringTable.end())return false;
	ReleaseBuffer(0);
    m_s=it->second;
    return true;
}

                                                              
char* KString::GetBuffer(size_t nMinBufLength){
	ReleaseBuffer();
	size_t nBufLength=m_s.size();
	if(nMinBufLength>nBufLength){
		nBufLength=nMinBufLength;
	}

	m_lock=true;
	m_buf=new char[nBufLength+1];
	strncpy(m_buf,m_s.c_str(),nBufLength);
	m_buf[nBufLength]=0;
	return m_buf;
}

char* KString::GetBufferSetLength(size_t nNewLength ){
	ReleaseBuffer();
	m_lock=true;
	m_buf=new char[nNewLength+1];
    strncpy(m_buf,m_s.c_str(),nNewLength);
	m_buf[nNewLength]=0;
	return m_buf;
}

                                                                                                 
                                                      
                                 
void KString::ReleaseBuffer(int n          ) {
	if( m_lock ){
		if ( m_buf ) {
			if ( n < 0 )
				m_s.assign(m_buf);
			else if ( n == 0 )
				m_s.erase();
			else {         
				int nToCopy = (int)GetLength();
				if ( n < nToCopy )
					nToCopy = n;
				m_s.assign(m_buf, nToCopy);
			}
			delete[] m_buf;
			m_buf = 0;
		}
		else
			m_s.erase();
		m_lock = false;
	}
}

KString& KString::operator+=(const KString& s) {
	ReleaseBuffer();
	m_s+=(const char*)s;
	return *this;
}

KString& KString::operator+=(const char* s) {
	ReleaseBuffer();
	if(s!=0)m_s+=s;
	return *this;
}

KString& KString::operator+=(const char c) {
	ReleaseBuffer();
	m_s+=c;
	return *this;
}

KString& KString::operator+=(const std::string& s) {
	ReleaseBuffer();
	m_s+=s;
	return *this;
}

KString& KString::operator=(const KString& s) {
	if(this==&s)return*this;
	ReleaseBuffer(0);
	if(s.m_lock&&s.m_buf)
		m_s.assign(s.m_buf);
	else
		m_s = s.m_s;
	return *this;
}

KString& KString::operator=(const char*s) {
	ReleaseBuffer(0);
	if(s!=0) m_s = s;
	else m_s.clear();
	return *this;
}

KString& KString::operator=(const std::string& s) {
	ReleaseBuffer(0);
	m_s = s;
	return *this;
}

KString operator+( const KString& str1, const KString& str2 ){
	                                                  
	if(!str1.m_lock&&!str2.m_lock) return KString(str1.m_s+str2.m_s);

	if(str1.m_lock&&str2.m_lock){
		return KString(std::string(str1.m_buf)+str2.m_buf);
	} else if(str1.m_lock){
		return KString(str1.m_buf+str2.m_s);
	} else {
		return KString(str1.m_s+str2.m_buf);
	}
}
KString operator+( const KString& str1, const char* str2 ){
	if(str2==0) return str1;
	if(str1.m_lock&&str1.m_buf)return KString(std::string(str1.m_buf)+str2);
	return KString( str1.m_s+str2 );
}
KString operator+( const KString& str, char c ){
	if(str.m_lock)return KString(std::string(str.m_buf)+c);
	return KString( str.m_s+c );
}
KString operator+( const KString& str, int i ){
	char buf[20];
	if(str.m_lock)return KString( std::string(str.m_buf)+konga::itoa(i,buf,10));
	return KString( str.m_s+konga::itoa(i,buf,10));
}
KString operator+( const KString& str, long i )
{
	char buf[20];
	if(str.m_lock)return KString( std::string(str.m_buf)+konga::itoa(i,buf,10));
	return KString( str.m_s+konga::itoa(i,buf,10) );
}
KString operator+( const KString& str, bool b ){
	if(str.m_lock) return KString( std::string(str.m_buf)+(b?"TRUE":"FALSE"));
	return KString( str.m_s+(b?"TRUE":"FALSE"));
}
KString operator+( const char* str1, const KString& str2 ){
	if(str1==0) return str2;
	if(str2.m_lock) return KString(std::string(str1)+str2.m_buf);
	return KString(std::string(str1)+str2.m_s);
}
KString operator+( char c, const KString& str ){
	if(str.m_lock) return KString(std::string(&c,1)+str.m_buf);
	return KString(std::string(&c,1)+str.m_s);
}
KString operator+( int i, const KString& str ){
	char buf[20];
	if(str.m_lock) return KString(std::string(konga::itoa(i,buf,10))+str.m_buf);
	return KString(std::string(konga::itoa(i,buf,10))+str.m_s);
}
KString operator+( long i, const KString& str ){
	char buf[20];
	if(str.m_lock) return KString(std::string(konga::itoa(i,buf,10))+str.m_buf);
	return KString(std::string(konga::itoa(i,buf,10))+str.m_s);
}
KString operator+( bool b, const KString& str ){
	if(str.m_lock) return KString(std::string(b?"TRUE":"FALSE")+str.m_buf);
	return KString(std::string(b?"TRUE":"FALSE")+str.m_s);
}

KString& KString::operator=(int i) { 
	ReleaseBuffer(0);
	char buf[20];
	m_s.assign(konga::itoa(i,buf,10));
	return *this; 
}

KString& KString::operator=(char c) { 
	ReleaseBuffer(0);
	m_s=c;
	return *this; 
}

bool KString::operator==(const KString&s) const {
	return strcmp((const char*)s, (const char*)(*this)) == 0;
}

bool KString::operator==(const char*s) const {
	if(s==0)return false;
	return strcmp(s,(const char*)(*this)) == 0;
}

bool KString::operator!=(const KString&s) const {
	return !(*this==s);
}

bool KString::operator==(const std::string&s) const {
	return (*this==s.c_str());
}

bool KString::operator!=(const char*s) const {
	return !(*this==s);
}

bool KString::operator!=(const std::string&s) const {
	return !(*this==s);
}

bool operator<(const KString& lhs, const KString& rhs) {
	return strcmp(lhs.getValue(),rhs.getValue())<0;
}

size_t KString::GetLength() const {
	if(m_lock)return strlen(m_buf);
	return m_s.length();
}

char KString::GetAt(size_t nIndex ) const {
	if(m_lock&&m_buf!=0)return m_buf[nIndex];
	return m_s.at(nIndex);
}

void KString::SetAt(size_t nIndex, char ch ){
	ReleaseBuffer();
	m_s.replace(nIndex,1,1,ch);
}

size_t KString::Insert(size_t iIndex, char ch )
{
	ReleaseBuffer();
	if( iIndex < 0 )iIndex = 0;
	if( iIndex > m_s.length() )iIndex = m_s.length();
	m_s.insert(iIndex, &ch, 1);

	return GetLength();
}

                                            
size_t KString::Insert(size_t iIndex, const char* psz )
{
	ReleaseBuffer();
	if(psz!=0){
		if( iIndex < 0 )iIndex = 0;
		if( iIndex > m_s.length() )iIndex = m_s.length();
		m_s.insert(iIndex, psz);
	}

	return m_s.length();
}

                                                           
size_t KString::Replace( char c0, char c )
{
	ReleaseBuffer();
	size_t n = 0;
	if( c0 != c )
	{
		const char* buffer = m_s.c_str();

		for(size_t i=0, l=m_s.size(); i<l; i++)
		{
			if( buffer[i] == c0 )
			{
				m_s.replace(i,1,1,c);
				n++;
			}
		}
	}
	return n;
}

size_t KString::Replace(const char* strOld, const char* strNew )
{
	if( strOld==0 || *strOld == 0 )return 0;
	ReleaseBuffer();

    size_t len0=strlen(strOld);

    const char* strStart = m_s.c_str();
	size_t n_replace = 0;
    size_t i=0, n=m_s.size();
    while((i=m_s.find(strOld,i))<n)
    {
        n_replace++;
        i+=len0;
    }
	if( n_replace > 0 )
	{
        size_t len1 = strlen(strNew);
        size_t len_new=m_s.size()+ (len1-len0)*n_replace;
		char* buf=new char[len_new+ 1];
        size_t k,i2=0;
        i=0;
        while((k=m_s.find(strOld,i))<n )
        {
            if(k>i)strncpy(&buf[i2],&strStart[i],k-i);
            i2+=k-i;
            if(len1)strcpy(&buf[i2],strNew);
            i2+=len1;
            i=k+len0;
        }
        if(i<n)strncpy(&buf[i2],&strStart[i],n-i);
        buf[len_new]=0;

        m_s=buf;
		delete[]buf;
	}
	return n_replace;
}

struct CharMatchPred {
	CharMatchPred(char c) : m_c(c) {}
	bool operator()(char c) { return c == m_c; }
	char m_c;
};

int KString::Remove(char c){
	ReleaseBuffer();
	CharMatchPred pred(c);

	                                                                                   
	const int i = std::count_if(m_s.begin(), m_s.end(), pred);
	if ( i == 0 )
		return 0;

	const std::string::size_type nSize = m_s.size();
	std::remove_if(m_s.begin(), m_s.end(), pred);
	m_s=m_s.substr(0,nSize-i);
	return i;
}

size_t KString::Delete(size_t iIndex, size_t nCount         )
{
	ReleaseBuffer();
	m_s.erase(iIndex, nCount);
	return GetLength();
}

int KString::Compare(const char* pc) const
{
	if(pc==0) return -1;
	return strcmp((const char*)(*this),pc);
}
int KString::CompareNoCase(const char*pc)const
{
	if(pc==0) return -1;
	return strcasecmp((const char*)(*this), pc);
}

KString KString::Left(size_t n) const {
	if(m_lock&&m_buf!=0)return KString(m_buf, n);
	return KString(m_s.substr(0, n).c_str());
}

KString KString::Right(size_t n) const {
	if(m_lock&&m_buf!=0){
		size_t k=strlen(m_buf);
		if(n>k)return *this;
		return KString(&m_buf[k-n]);
	} else {
		size_t k=m_s.size();
		if(n>= k)return *this;
		return KString(m_s.substr(k-n,k).c_str());
	}
}

KString KString::Mid(size_t i) const {
	if(m_lock&&m_buf!=0){
		return KString(&m_buf[i]);
	}
	return Mid(i,m_s.size()-i);
}

KString KString::Mid(size_t i, size_t n) const {
	if(m_lock&&m_buf!=0){
		return KString(&m_buf[i],n);
	}
	size_t n0=m_s.size();
	if(i>n0)return KString("");
	return KString(m_s.substr(i,n));
}

int KString::Find( char c, int iStart         ) const throw()
{
	if(m_lock&&m_buf){
		int n=strlen(m_buf);
		if(iStart>n)return -1;
		char*s0=&m_buf[iStart];
		char*s=strchr(s0,c);
		if(s==0)return -1;
		return (int)(s-s0)+iStart;
	}
	std::string::size_type pos = m_s.find(c, iStart);
	return ( pos != std::string::npos ) ? static_cast<int>(pos) : -1;
}

int KString::Find(const char* pszSub, int iStart         ) const throw()
{
	if(m_lock&&m_buf){
		int n=strlen(m_buf);
		if(iStart>n)return -1;
		char*s0=&m_buf[iStart];
		char*s=strstr(s0,pszSub);
		if(s==0)return -1;
		return (int)(s-s0);
	}
	std::string::size_type pos = m_s.find(pszSub, iStart);
	return ( pos != std::string::npos ) ? static_cast<int>(pos) : -1;
}

int KString::FindOneOf( const char* lpszCharSet ) const {
	if(m_lock&&m_buf!=0){
		int k=strcspn(m_buf,lpszCharSet);
		if(m_buf[k]==0)return -1;
		return k;
	}
	std::string::size_type pos = m_s.find_first_of(lpszCharSet);
	return ( pos != std::string::npos ) ? static_cast<int>(pos) : -1;
}

int KString::ReverseFind(const char ch) const {
	if(m_lock&&m_buf!=0){
		char*s=strrchr(m_buf,ch);
		if(s==0)return -1;
		return (int)(s-m_buf);
	}
	std::string::size_type pos = m_s.find_last_of(ch);
	return ( pos != std::string::npos ) ? static_cast<int>(pos) : -1;
}

void KString::Empty(){
	ReleaseBuffer(0);
	m_s.clear();
}

void KString::MakeUpper()
{
	ReleaseBuffer();
	boost::to_upper(m_s);
}

void KString::MakeLower()
{
	ReleaseBuffer();
	boost::to_lower(m_s);
}

void KString::TrimLeft(const char c){
	ReleaseBuffer();
	const char*s0=m_s.c_str();
	int i=0;
	for(;s0[i]==c;i++);
	if(i>0)m_s.erase(0,i);
}

void KString::TrimLeft(const char* s ){
	ReleaseBuffer();
	const char*s0=m_s.c_str();
	size_t n=strspn(s0,s);
	if(n>0)m_s.erase(0,n);
}

void KString::TrimRight(const char c){
	ReleaseBuffer();
	const char*s0=m_s.c_str();
	size_t i=strlen(s0),n=i;
	for(;i>0&&s0[i-1]==c;i--);
	if(i<n)m_s.erase(i,n);
}

void KString::TrimRight(const char* s ){
	ReleaseBuffer();
	const char*s0=m_s.c_str();
	size_t i=strlen(s0),n=i;
	for(;i>0;i--){
		if(strchr(s,s0[i-1])==NULL)break;
	}
	m_s.erase(i,n);
}

KString KString::SpanExcluding(const char*s) {
	ReleaseBuffer();
	int n=(int)::strcspn(m_s.c_str(),s);
	return KString(m_s.c_str(),n);
}

const char*KString::getValue() const {
	if(m_lock)return m_buf;
	else return m_s.c_str();
}

bool operator==(const char*s,const KString&str){
	if(str.m_lock)return strcmp(s,str.m_buf)==0;
	return str.m_s==s;
}

bool operator!=(const char*s,const KString&str){
	return !(s==str);
}

