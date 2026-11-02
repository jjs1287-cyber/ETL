/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_UTILS_NULLABLE_H
#define KONGA_UTILS_NULLABLE_H

#include "CrException.h"

namespace konga {

	class NullableException : public CrException {
	public:
		NullableException() : CrException("A NullableException occurred. This is most likely a bug.") {}
		virtual ~NullableException() {}
	};

	namespace {
		                                                                                      
		                              
		                                  
		const void* null = 0;
	}

	                         
                                                                                           
                                                          
    
	template<typename T>
	class Nullable {

	public:
		                                   
		Nullable(void) : m_bIsNull(false) {}

		                      
		                         
		Nullable(const void*) : m_bIsNull(true) { }

		                                           
		Nullable(const T& t) : m_bIsNull(false), m_t(t) {}

		                   
		Nullable(const Nullable<T>& nullable) : m_bIsNull(nullable.m_bIsNull), m_t(nullable.m_t) {}

		virtual ~Nullable(void) {}

		                                             
		                        
		                  
		Nullable<T>& operator=(const T* pt) {
			m_bIsNull = (pt==0);
			if ( pt )
				m_t = *pt;
			return *this;
		}

		                       
		Nullable<T>& operator=(const T& t) {
			m_bIsNull = false;
			m_t = t;
			return *this;
		}

		                  
		Nullable<T>& operator=(const Nullable<T>& nullable) {
			m_bIsNull = nullable.m_bIsNull;
			m_t = nullable.m_t;
			return *this;
		}

		                  
		operator T() const {
			return const_ref();
		}

		                                   
		                                                              
		T& operator*() {
			return ref();
		}

		                                                                          
		bool operator==(const void* p) const {
			return (p!=0) ? false : isNull();
		}

		bool operator!=(const void* p) const {
			return !(*this==p);
		}

		                                 
		bool operator==(const Nullable<T>& nullable) const {
			if ( isNull() ) {
				if ( nullable.isNull() )
					return true;
			}
			else {
				if ( nullable.isNull() )
					return false;
			}
			return m_t == nullable.m_t;
		}

		bool operator!=(const Nullable<T>& nullable) const {
			return !(*this==nullable);
		}

		                                                   
		                                                     
		T& ref() {
			if ( isNull() )
				throw NullableException();
			return m_t;
		}

		                                                            
		                                                     
		const T& const_ref() const {
			if ( isNull() )
				throw NullableException();
			return m_t;
		}

	protected:
		bool isNull() const {
			return m_bIsNull;
		}

		void setNull(bool bIsNull) {
			m_bIsNull = bIsNull;
		}

		                                                                         
		                                                                                    
		T& getT() {
			return m_t;
		}

	private:
		bool m_bIsNull;
		T m_t;
	};
}

#endif                          

