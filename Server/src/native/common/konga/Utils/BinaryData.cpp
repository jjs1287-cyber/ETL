/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <cassert>

#include "BinaryData.h"
#include "AutoPtrCleanup.h"	                    
#include "CrException.h"
#include "StringUtils.h"

using std::string;
using std::stringstream;

                    
namespace {
	const unsigned long nMaxAlloc = INT_MAX;

	                                                                                            
	                                             
	inline char* AllocMem(unsigned long nSize)
	{
		char* pTmp = 0;
		if ( (pTmp = new char[nSize]) == 0 )
		{
			stringstream ssErr;
			ssErr << "Failed to allocate memory for " << nSize << " unsigned chars in BinaryData object.";
			throw konga::CrException(-1, ssErr.str());
		}
		return pTmp;
	}
}

using namespace konga;

const unsigned long konga::BinaryData::NotFound = INT_MAX;

konga::BinaryData::BinaryData(void) : m_pData(0), m_nSize(0), m_nAllocSize(0) { }

konga::BinaryData::BinaryData(const char* pData, unsigned long nSize, bool bCopy) : m_pData(0), m_nSize(0), m_nAllocSize(0)
{
	assign(pData, nSize, bCopy);
}

konga::BinaryData::BinaryData(const BinaryData& Data) : m_pData(0), m_nSize(0), m_nAllocSize(0) { assign(Data); }

konga::BinaryData::BinaryData(const string& stData) : m_pData(0), m_nSize(0), m_nAllocSize(0) { assign(stData); }

konga::BinaryData::~BinaryData(void)
{
	Clear();
}

konga::BinaryData& konga::BinaryData::assign(const char* pData, unsigned long nSize, bool bCopy)
{
	if ( pData == 0 || nSize == 0 )
	{
		                      
		Clear();
		return *this;
	}

	if ( bCopy )
	{
		                                             
		                                                                                               
		if ( nSize > m_nAllocSize || m_pData == 0 )
		{
			Clear();
			m_pData = AllocMem(nSize);
			m_nAllocSize = m_nSize = nSize;

		}
		else
			m_nSize = nSize;

		assert(m_pData);
		assert(m_nAllocSize >= m_nSize);

		::memcpy(m_pData, pData, m_nSize);
	}
	else                         
	{
		Clear();
		m_pData = const_cast<char*>(pData);
		m_nAllocSize = m_nSize = nSize;
	}
	return *this;
}

konga::BinaryData& konga::BinaryData::assign(const konga::BinaryData& Data)
{
	if ( this == &Data )                                   
		return *this;
	else if ( Data.empty() )                       
	{
		Clear();
		return *this;
	}
	else
		return assign(Data.GetPointerToData(), Data.Size(), true);
}

konga::BinaryData& konga::BinaryData::assign(const string& stData)
{
	if ( stData.empty() )                        
	{
		Clear();
		return *this;
	}
	else
		return assign(stData.c_str(), stData.size(), true);
}

konga::BinaryData& konga::BinaryData::append(const char* pData, unsigned long nSize, bool bReverse           )
{
	if ( pData == 0 || nSize == 0 )                      
		return *this;
	else if ( bReverse )
	{
		AutoArrayDelete<char> pReversed( new char[nSize] );
		for (unsigned long i=0,j=nSize-1; i<nSize; ++i,--j) { pReversed[i] = pData[j]; }
		return append(pReversed.Get(), nSize, false);
	}

	if ( empty() )                                                         
		return assign(pData, nSize, true);
	else
	{
		if ( m_nSize + nSize > m_nAllocSize )
			Reserve( 2*(m_nSize + nSize) );
		::memcpy(&m_pData[m_nSize], pData, nSize);
		m_nSize += nSize;

		assert(m_nAllocSize >= m_nSize);
		return *this;
	}
}

konga::BinaryData& konga::BinaryData::append(const konga::BinaryData& Data)
{
	if ( this == &Data )                                                                        
	{
		konga::BinaryData Tmp(Data);
		return append(Tmp);
	}
	else if ( !Data.empty() )
		return append(Data.GetPointerToData(), Data.Size());
	else
		return *this;
}

konga::BinaryData& konga::BinaryData::append(const string& stData)
{
	if ( !stData.empty() )
		return append( stData.c_str(), stData.size() );
	else
		return *this;
}

konga::BinaryData& konga::BinaryData::append(unsigned long l)
{
	return append( reinterpret_cast<char*>(&l), sizeof l );
}

const char* konga::BinaryData::GetPointerToData(unsigned long &nSize) const
{
	nSize = m_nSize;
	return m_pData;
}

const char* konga::BinaryData::GetPointerToData() const { return m_pData; }

string konga::BinaryData::GetStringRep() const {
	return string(m_pData, m_nSize);
}

string konga::BinaryData::GetDebugString() const
{
	if ( m_nSize == 0 || m_pData == 0 )
		return "";

	string st;
	char buf[10];
	for ( unsigned long i = 0; i<m_nSize; ++i )
	{
		if( ::isalnum(*reinterpret_cast<unsigned char*>(m_pData+i)) )
			::sprintf(buf, " %c", *(m_pData+i));
		else
			::sprintf(buf, "%02X", *(m_pData+i));
		st += buf;
	}
	return st;
}

string konga::BinaryData::GetHexRep() const
{
	if ( m_nSize == 0 || m_pData == 0 )
		return "";

	return konga::getHexString((const char*)m_pData, (size_t)m_nSize);
}

unsigned long konga::BinaryData::GetUL(unsigned long index) const {
	const int ulSize = sizeof(unsigned long);
	if ( index + ulSize > m_nSize )
		throw CrException("An attempt was made to read an unsigned long from a" \
		" BinaryData outside of the memory allocated.");
	char buf[ulSize];
	memcpy(buf, m_pData+index, ulSize);
	return *reinterpret_cast<const unsigned long*>(buf);
}

bool konga::BinaryData::HasNulls() const
{
	if ( empty() )
		return false;

	return ( ::memchr(m_pData, 0, m_nSize) != 0 );
}

char* konga::BinaryData::Release(unsigned long& nSize)
{
	OptimizeMemory();
	nSize = m_nSize;
	char* pRet = m_pData;
	m_pData = 0;
	m_nSize = 0;
	m_nAllocSize = 0;
	return pRet;
}

unsigned long konga::BinaryData::Size() const { return m_nSize; }

void konga::BinaryData::Size(unsigned long nNewSize)
{
	if ( nNewSize == m_nSize )
		return;

	if ( nNewSize < m_nSize )
	{
		m_nSize = nNewSize;
	}		
	else
	{
		Reserve(nNewSize);
		::memset(m_pData+m_nSize, 0, nNewSize-m_nSize);
		m_nSize = nNewSize;
	}
	assert(m_nAllocSize>=m_nSize);
}

unsigned long konga::BinaryData::Capacity() const { return m_nAllocSize; }

void konga::BinaryData::Reserve(unsigned long nSize)
{
	if ( nSize > m_nAllocSize )
	{
		unsigned long nBytesToAlloc = (nSize > nMaxAlloc) ? nMaxAlloc : nSize;
		AutoArrayDelete<char> pTmp( AllocMem(nBytesToAlloc) );

		                                                                               
		::memcpy(pTmp.Get(), m_pData, m_nSize);
		delete [] m_pData;

		                     
		m_pData = pTmp.Release();
		m_nAllocSize = nBytesToAlloc;
	}
	assert(m_nAllocSize>=m_nSize);
}

void konga::BinaryData::OptimizeMemory()
{
	if ( m_nAllocSize > m_nSize ) {
		                                               
		AutoArrayDelete<char> pTmp( AllocMem(m_nSize) );

		::memcpy(pTmp.Get(), m_pData, m_nSize);
		delete [] m_pData;
		m_pData = pTmp.Release();
		m_nAllocSize = m_nSize;
	}

	assert(m_nAllocSize==m_nSize);
}

void konga::BinaryData::Clear()
{
	if ( m_pData ) {
		delete[] m_pData;
		m_pData = 0;
	}

	m_nSize = 0;
	m_nAllocSize = 0;
}

bool konga::BinaryData::empty() const { return ( m_pData == 0 || m_nSize == 0 ); }

unsigned long konga::BinaryData::Find(const char* pData, unsigned long nSize, unsigned long nPos) const
{
	if ( nSize > m_nSize || nPos > m_nSize-nSize )
		return NotFound;
	for ( unsigned long i = nPos; i <= m_nSize-nSize; ++i )
	{
		if ( ::memcmp(m_pData+i, pData, nSize) == 0 )
			return i;
	}
	return NotFound;
}

unsigned long konga::BinaryData::Find(const BinaryData& Data, unsigned long nPos) const
{
	return Find(Data.GetPointerToData(), Data.Size(), nPos);
}

unsigned long konga::BinaryData::Find(const string& stSeq, unsigned long nPos) const
{
	return Find((const char*)stSeq.c_str(), stSeq.size(), nPos);
}

BinaryData konga::BinaryData::SubSeq(unsigned long nPos, unsigned long iCount) const
{
	if ( iCount == 0 || nPos > m_nSize-1 )
		return BinaryData();

	if ( nPos+iCount > m_nSize )
		iCount = m_nSize-nPos;

	return BinaryData(&m_pData[nPos], iCount, true);
}

BinaryData& konga::BinaryData::operator=(const BinaryData& rhs) { return assign(rhs); }

BinaryData& konga::BinaryData::operator=(const string& rhs) { return assign(rhs); }

BinaryData& konga::BinaryData::operator+=(const BinaryData& rhs) { return append(rhs); }

BinaryData& konga::BinaryData::operator+=(const string& rhs) { return append(rhs); }

BinaryData& konga::BinaryData::operator+=(unsigned long rhs) { return append(rhs); }

bool konga::BinaryData::operator==(const BinaryData& rhs) const
{
	if ( this == &rhs )
		return true;
	if ( m_nSize != rhs.Size() )
		return false;
	return ( ::memcmp(GetPointerToData(), rhs.GetPointerToData(), m_nSize) == 0 );
}

bool konga::BinaryData::operator!=(const BinaryData& rhs) const { return !( *this == rhs ); }

void konga::BinaryData::CheckPos(unsigned long nPos) const {
	assert(m_nAllocSize>=m_nSize);
	if ( empty() )
		throw konga::CrException("An attempt was made to access an empty BinaryData object.");

	if ( nPos > m_nSize-1 )
	{
		string sErr("An attempt was made to read from or write to a position outside of the space allocated " \
			"by this BinaryData object. The last position is " + toString(m_nSize-1)
			+ ". A reference to a char at position " + toString(nPos)  + " was requested.");
		throw konga::CrException(sErr);
	}
}

char& konga::BinaryData::operator[](unsigned long nPos)
{
	CheckPos(nPos);
	return m_pData[nPos];
}

const char& konga::BinaryData::operator[](unsigned long nPos) const {
	CheckPos(nPos);
	return m_pData[nPos];
}

                                          
  
const konga::BinaryData konga::operator+(const BinaryData& lhs, const BinaryData& rhs)
{
	BinaryData ret(lhs);
	ret.append(rhs);
	return ret;
}
