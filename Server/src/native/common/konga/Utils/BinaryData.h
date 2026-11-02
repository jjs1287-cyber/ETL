/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef BINARY_DATA_H
#define BINARY_DATA_H

#include <string>

namespace konga {

                                                                                      
                                                                                                         
class BinaryData
{
public:             
	static const unsigned long NotFound;

public:           
	                             
	BinaryData(void);

	                                                                                                          
	                                                                                                               
	BinaryData(const char* pData, unsigned long nSize, bool bCopy = true);
	BinaryData(const BinaryData&);
	BinaryData(const std::string& stData);

	~BinaryData(void);

	                                                                                                     
	                                                                                                    
	BinaryData& assign(const char* pData, unsigned long nSize, bool bCopy = true);
	BinaryData& assign(const BinaryData& Data);
	BinaryData& assign(const std::string& stData);

	                                                                           
	BinaryData& append(const char* pData, unsigned long nSize, bool bReverse = false);
	BinaryData& append(const BinaryData& Data);
	BinaryData& append(const std::string& stData);
	BinaryData& append(unsigned long l);

	                                                                                                        
	                                                                                                          
	                                                                                           
	const char* GetPointerToData(unsigned long &nSize) const;
	const char* GetPointerToData() const;

	                                                                              
	                                                                        
	std::string GetStringRep() const;

	                                                                                                     
	                                                                                                                    
	                               
	std::string GetDebugString() const;

	                                                           
	std::string GetHexRep() const;

	                                                                      
	                                                   
	unsigned long GetUL(unsigned long index) const;

	                                                   
	bool HasNulls() const;

	                                                                                                                  
	                              
	char* Release(unsigned long& nSize);

	                                                             
	unsigned long Size() const;

	                                                                                                                        
	                                                                                                               
	                                                                                                                                     
	void Size(unsigned long nSize);

	                                                                                                    
	unsigned long Capacity() const;

	                                                                                                                                       
	void Reserve(unsigned long nSize);

	                                                                    
	void OptimizeMemory();

	                          
	void Clear();

	                                           
	bool empty() const;

	                                                                                      
	                                                      
	unsigned long Find(const char* pData, unsigned long nSize, unsigned long nPos = 0) const;
	unsigned long Find(const BinaryData& Data, unsigned long nPos = 0) const;
	unsigned long Find(const std::string& stSeq, unsigned long nPos = 0) const;

	                                                                             
	BinaryData SubSeq(unsigned long nPos, unsigned long iCount) const;

	                      
	  
	             
	BinaryData& operator=(const BinaryData& rhs);
	BinaryData& operator=(const std::string& rhs);

	         
	BinaryData& operator+=(const BinaryData& rhs);
	BinaryData& operator+=(const std::string& rhs);
	BinaryData& operator+=(unsigned long rhs);

	              
	bool operator==(const BinaryData& rhs) const;
	bool operator!=(const BinaryData& rhs) const;

	                                                                                                           
	                                                                                  
	char& operator[](unsigned long nPos);

	                                                                                                                 
	                                                                                  
	const char& operator[](unsigned long nPos) const;

private:
	void CheckPos(unsigned long nPos) const;

private:
	char* m_pData;                                              
	unsigned long m_nSize;                    
	unsigned long m_nAllocSize;                                                                       
};

           
const BinaryData operator+(const BinaryData& lhs, const BinaryData& rhs);

}                   

#endif                 
