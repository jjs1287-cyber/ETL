/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_UTILS_KSTRING_H
#define ETLFORGE_UTILS_KSTRING_H

#include <stdarg.h>	          

#include <string>
#include <map>
#include <functional>

                                  
#ifdef LoadString
#undef LoadString
#endif


class KString {
private:
	std::string m_s;                                                          

public:

	const char* getValue() const;

	inline KString() : m_buf(0), m_lock(false) {}
	inline KString(const char* pc) : m_s(pc!=0?pc:""), m_buf(0), m_lock(false) {}
	inline KString(const char* pc, size_t nCount) : m_s(pc!=0?pc:"",pc!=0?nCount:0), m_buf(0), m_lock(false) {}
	KString(const KString& cs);
	inline KString(const std::string& s) : m_s(s), m_buf(0), m_lock(false) {}
	inline KString(const char c) : m_buf(0), m_lock(false) {m_s+=c;}

	KString(int i);
	KString(size_t n);

	~KString() {
		if(m_buf!=0) delete[] m_buf;
	}

	void Format( const char* pszFormat, ... );

	void FormatV( const char* pszFormat, va_list args );

	char* GetBuffer(size_t nMinBufLength);
	char* GetBufferSetLength(size_t nNewLength );

	                                                                                   
	                                                      
	                                 
	                                            
	void ReleaseBuffer(int n=-1);

	                                                                 
	bool LoadString( unsigned int nID );

	                                                
	inline operator const char*() const {
		if(m_lock)return m_buf;
		return m_s.c_str();
	}

	KString& operator+=(const KString& s);
	KString& operator+=(const char* s);
	KString& operator+=(const char c);
	KString& operator+=(const std::string& s);

	KString& operator=(const KString& s);
	KString& operator=(const char*s);
	KString& operator=(const std::string& s);
	KString& operator=(char c); 
	KString& operator=(int i);

	bool operator==(const KString&s) const;
	bool operator==(const char*s) const;
	bool operator!=(const KString&s) const;
	bool operator!=(const char*s) const;
	bool operator==(const std::string&s) const;
	bool operator!=(const std::string&s) const;

	size_t GetLength() const;
	char GetAt(size_t nIndex ) const;
	void SetAt(size_t nIndex, char ch );
	                                              
	size_t Insert(size_t iIndex, char ch );
	                                            
	size_t Insert(size_t iIndex, const char* psz );
	                                                           
	size_t Replace( char c0, char c );
	                                                                  
	size_t Replace(const char* strOld, const char* strNew );
	size_t Replace(const std::string& stOld, const std::string& stNew )
    {
		return Replace(stOld.c_str(),stNew.c_str()); 
	}


	                                                         
	size_t Delete(size_t iIndex, size_t nCount = 1 );

	                                                 
	inline bool IsEmpty() const
	{
		if(m_lock)return m_buf==0||m_buf[0]==0;
		return m_s.empty();
	}

	int Compare(const char* pc) const;
	int CompareNoCase(const char*pc)const;

	KString Left(size_t n) const ;
	KString Right(size_t n) const;
	KString Mid(size_t i) const ;
	KString Mid(size_t i, size_t n) const ;

	int Find( char c, int iStart = 0 ) const throw();
	int Find(const char* pszSub, int iStart = 0 ) const throw();
	int FindOneOf( const char* lpszCharSet ) const ;
	int ReverseFind(const char ch) const ;

	void Empty();
	void MakeUpper();
	void MakeLower();
	KString SpanExcluding(const char*s);

	int Remove(char c);
	void TrimLeft(const char c=' ' );
	void TrimLeft(const char* s );
	void TrimRight(const char c=' ' );
	void TrimRight(const char* s );
	
private:

	friend KString operator+( const KString& str1, const KString& str2 );
	friend KString operator+( const KString& str1, const char* str2 );
	friend KString operator+( const KString& str1, char c );
	friend KString operator+( const KString& str1, int i );
	friend KString operator+( const KString& str, long i );
	friend KString operator+( const KString& str1, bool b );
	friend KString operator+( const char* str1, const KString& str2 );
	friend KString operator+( char c, const KString& str );
	friend KString operator+( int i, const KString& str );
	friend KString operator+( long i, const KString& str );
	friend KString operator+( bool b, const KString& str );
	friend bool operator!=(const char*s, const KString&str);
	friend bool operator==(const char*s, const KString&str);

private:
	char* m_buf;
	bool m_lock;

	                                                 
    static std::map<int,std::string> m_mapStringTable;
    static bool m_bInitialized;
};

KString operator+( const KString& str1, const KString& str2 );
KString operator+( const KString& str1, const char* str2 );
KString operator+( const KString& str, char c );
KString operator+( const KString& str, int i );
KString operator+( const KString& str, long i );
KString operator+( const KString& str, bool b );
KString operator+( const char* str1, const KString& str2 );
KString operator+( char c, const KString& str );
KString operator+( int i, const KString& str );
KString operator+( long i, const KString& str );
KString operator+( bool b, const KString& str );

bool operator!=(const char*s,const KString&str);
bool operator==(const char*s,const KString&str);

bool operator<(const KString& lhs, const KString& rhs);

namespace std {

	                                                
	template <>
	struct less<const KString> : binary_function<const KString, const KString, bool> {
		bool operator () (const KString& rhs, const KString& lhs) const {
			return strcmp((const char*)rhs,(const char*)lhs) < 0;
		} 
	};

}                 

#endif                            


