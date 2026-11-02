/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_WSALLOCATIONS_H
#define KONGA_WS_WSALLOCATIONS_H

#include "string.h"		                

#include <string>
#include <vector>
#include <cassert>

#include <boost/noncopyable.hpp>

#include <axis/AxisUserAPI.hpp>
#include <axis/AxisUserAPIArrays.hpp>

#include <konga/Utils/NullableString.h>

AXIS_CPP_NAMESPACE_USE

                                                                                         
  
namespace konga {
                 
namespace {
	                                                                                      
	                                                                                 
	                                       
	                                                                                  
	                                   
	xsd__string allocate_xsd__string(const std::string& str) {
		xsd__string retval = new AxisChar[str.size()+1];
		strcpy(retval, str.c_str());
		return retval;
	}

	                                                                                   
	                                                                                               
	                                                                                     
	                                       
	xsd__string allocate_xsd__string(const konga::NullableString& str) {
		if ( str == 0 )
			return 0;
		return allocate_xsd__string(str.const_ref());
	}

	void deallocate_xsd__string(xsd__string& s) {
		delete[] s;
		s = 0;
	}

	                                                                                               
	class AutoDeleteOur_xsd__string {
	public:
		AutoDeleteOur_xsd__string(xsd__string& s) : m_s(s) {}
		~AutoDeleteOur_xsd__string(void) {
			deallocate_xsd__string(m_s);
		}

	private:
		xsd__string& m_s;
	};

	                    
	xsd__long* allocate_xsd__long(long l) {
		xsd__long* res = new xsd__long;
		*res = l;
		return res;
	}

	void deallocate_xsd__long(xsd__long*& pLong) {
		delete pLong;
		pLong = 0;
	}

	                                                                      
	                                                                            
	                                                                           
	                         
	template<typename T>
	T** allocate_pointer_array(int size) {
		assert(size>=0);

		if ( size == 0 )
			return 0;

		T** arr = new T*[size];
		memset(arr, 0, size*sizeof(T*));
		return arr;
	}

	                                                                       
	                                                                  
	template<typename T>
	void deallocate_pointer_array(T**& arr, int size) {
		assert(size>=0);

		for( int i = 0; i < size; ++i ) {
			delete arr[i];
			arr[i] = 0;
		}
		delete[] arr;
		arr = 0;
	}

	                                                      
	                                                                       
	template<typename T>
	class AutoDeletePointerArray : boost::noncopyable {
	public:
		AutoDeletePointerArray(T**& pT, int iSize) : m_pT(pT), m_iSize(iSize) {}
		~AutoDeletePointerArray(void) {
			deallocate_pointer_array<T>(m_pT, m_iSize);
		}
	private:
		T**& m_pT;
		int m_iSize;
	};

	                                                                       
	                                                                    
	template<typename T>
	void deallocate_array_of_arrays(T**& arr, int size) {
		assert(size>=0);

		for( int i = 0; i < size; ++i ) {
			delete[] arr[i];
			arr[i] = 0;
		}
		delete[] arr;
		arr = 0;
	}

	                                                    
	                                                                       
	template<typename T>
	class AutoDeleteArrayOfArrays : boost::noncopyable {
	public:
		AutoDeleteArrayOfArrays(T**& pT, int iSize) : m_pT(pT), m_iSize(iSize) {}
		~AutoDeleteArrayOfArrays(void) {
			deallocate_array_of_arrays<T>(m_pT, m_iSize);
		}
	private:
		T**& m_pT;
		int m_iSize;
	};

	                                                        
	                                                                                                        
	template<typename T>
	void allocate_xsd__string_Array(const std::vector<T>& vOrig, xsd__string_Array& xsdStringArray) {
		const typename std::vector<T>::size_type n = vOrig.size();
		if ( n == 0 ) {
			xsdStringArray.clear();
			return;
		}

		xsd__string* arr = allocate_pointer_array<AxisChar>(n);
		AutoDeleteArrayOfArrays<AxisChar> adopt_arr(arr, n);

		for( typename std::vector<T>::size_type i = 0; i < n; ++i) {
			arr[i] = allocate_xsd__string(vOrig.at(i));
		}
		xsdStringArray.set(arr, n);
	}

	                                                                  
	template<typename T>
	xsd__string_Array* allocate_xsd__string_Array(const std::vector<T>& vOrig) {
		auto_ptr<xsd__string_Array> pRetVal( new xsd__string_Array() );
		allocate_xsd__string_Array(vOrig, *pRetVal);
		return pRetVal.release();
	}

	                                                                
	xsd__string_Array* allocate_xsd__string_Array(const std::vector<std::string>& arr) {
		return allocate_xsd__string_Array<std::string>(arr);
	}

	                                                            
	xsd__long_Array* allocate_xsd__long_Array(const std::vector<long>& vOrig) {
		const std::vector<long>::size_type n = vOrig.size();
		if ( n == 0 )
			return new xsd__long_Array();

		xsd__long** arr = allocate_pointer_array<xsd__long>(n);
		AutoDeletePointerArray<xsd__long> adopt_arr(arr, n);
		for( std::vector<long>::size_type i = 0; i < n; ++i ) {
			arr[i] = allocate_xsd__long(vOrig.at(i));
		}

		xsd__long_Array* res = new xsd__long_Array();
		res->set(arr, n);
		return res;
	}

	                                                                                                         
	                                                                                                                       
	                                  
	                                                   
	                                                                                                          
	                                                              
	  
	                                      
	  
	                                                                        
	  
	                                                                       
	template<class ArrayOf, class Array, class ArrayItem, class ItemCreator>
	ArrayOf* allocateArrayOf(ItemCreator& creator) {
		const int size = creator.size();
		ArrayItem** arr =
			konga::allocate_pointer_array<ArrayItem>(size);
		konga::AutoDeletePointerArray<ArrayItem> adopt_arr(arr, size);
		for( int i = 0; i < size; ++i ) {
			arr[i] = creator.create(i);
		}

		std::auto_ptr<Array> p_Array(new Array());
		p_Array->set(arr, size);	                                  

		std::auto_ptr<ArrayOf> pArrayOf(new ArrayOf());
		pArrayOf->setitem(p_Array.get());	                                  

		return pArrayOf.release();
	}

	                         
	template<class T, class T_Array>
	T_Array* allocate_T_Array(std::vector<T*>& v) {
		std::auto_ptr<T_Array> pT( new T_Array() );
		const int size = v.size();
		if ( size < 1 ) return pT.release();

		T** arr = konga::allocate_pointer_array<T>(size);
		for( int i = 0; i < size; ++i ) {
			arr[i] = v.at(i);
		}
		v.clear();
		konga::AutoDeletePointerArray<T> adopt_arr(arr, size);

		pT->set(arr, size);
		return pT.release();
	}

	                         
	template<class T, class T_Array, class ArrayOfT>
	ArrayOfT* allocate_ArrayOfT(std::vector<T*>& v) {
		std::auto_ptr<ArrayOfT> arr( new ArrayOfT() );
		arr->setitem( allocate_T_Array<T, T_Array>(v) );
		return arr.release();
	}


}                       
}                   

#endif                            

