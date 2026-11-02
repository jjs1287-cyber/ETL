/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

  
                                                            
                                                                
  
                              
                                            
                                       
                   
  
                                                       
                           
                                            
                                   
           
    
  
                                                                         
                                                                       
                                                                         
                                                                             
                         
  
                                 
  

#ifndef KONGA_AUTO_PTR_CLEANUP
#define KONGA_AUTO_PTR_CLEANUP

#include <cassert>
#include <vector>
#include <stack>

#include <boost/noncopyable.hpp>

namespace konga {

	                                                                                                     
	                                                                               
	                                                                  
	template<typename T>
	class AutoPtrDelete : boost::noncopyable {
	public:
		explicit AutoPtrDelete(T* p = 0) : m_p(p) {}
		~AutoPtrDelete() {
			Reset(0);
		}

		T* Release() {
			T* pt = m_p;
			m_p = 0;
			return pt;
		}

		T* Get() const {
			return m_p;
		}

		void Reset(T* pt) {
			if ( m_p != pt )
				delete m_p;
			m_p = pt;
		}

	private:
		T* m_p;
	};

	                                                                            
	                                                                                               
	template<typename T>
	class AutoPtrDeleteAndNull : boost::noncopyable {
	public:
		AutoPtrDeleteAndNull(T*& p) : m_pref(p) {}
		~AutoPtrDeleteAndNull(void) {
			if ( m_pref ) {
				delete m_pref;
				m_pref = 0;
			}
		}

	private:
		T*& m_pref;
	};

	                                                                                    
	                                                                                
	                                                                   
	                                        
	template<typename T>
	class AutoArrayDelete : boost::noncopyable {
	public:
		explicit AutoArrayDelete(T* p) : m_ptr_to_array(p) {}
		~AutoArrayDelete() {
			Reset(0);
		}

		T* Release() {
			T* pt = m_ptr_to_array;
			m_ptr_to_array = 0;
			return pt;
		}

		T* Get() const {
			return m_ptr_to_array;
		}

		void Reset(T* pt) {
			if ( pt != m_ptr_to_array )
				delete[] m_ptr_to_array;
			m_ptr_to_array=pt;
		}

		T& operator[](unsigned long i) const {
			assert(m_ptr_to_array);
			return m_ptr_to_array[i];
		}

	private:
		T* m_ptr_to_array;
	};

	                                                                                     
	                                                                                 
	                                 
	template<typename T>
	class AutoArrayOfArrayDelete : boost::noncopyable {
	public:
		AutoArrayOfArrayDelete(T** p, size_t size) : m_arr_of_arr(p), m_size(size) {}
		~AutoArrayOfArrayDelete(void) {
			if ( m_arr_of_arr ) {
				for( size_t i = 0; i < m_size; ++i ) {
					delete[] m_arr_of_arr[i];
				}
				delete[] m_arr_of_arr;
				m_arr_of_arr = 0;
				m_size = 0;
			}
		}

	private:
		T** m_arr_of_arr;
		size_t m_size;
	};

	                                                                                           
	                                                                                                          
	template<typename T>
	class AutoArrayOfPointerDelete : boost::noncopyable {
	public:
		AutoArrayOfPointerDelete(T** p) : m_arr_of_pointers(p) {}
		~AutoArrayOfPointerDelete(void) {
			if(m_arr_of_pointers!=0){
				for(int i=0;m_arr_of_pointers[i];++i)delete m_arr_of_pointers[i];
				delete[] m_arr_of_pointers;
				m_arr_of_pointers = 0;
			}
		}

	private:
		T** m_arr_of_pointers;
	};

	                                                                             
	                                                                     
	                                    
	template<typename T>
	class AutoPtrDeleteInReversedOrder : boost::noncopyable
	{
	public:
		AutoPtrDeleteInReversedOrder() {};
		~AutoPtrDeleteInReversedOrder() { Delete(); }
		void Attach(T* pObj) { m_vpObjects.push_back(pObj); }
		void ReleaseAll() { m_vpObjects.clear(); }

	private:
		std::vector<T*>	m_vpObjects;
		void Delete()
		{
			for( int i = m_vpObjects.size()-1; i >= 0; --i )
			{
				delete m_vpObjects[i];
			}
			m_vpObjects.clear();
		}
	};

	                                    
	                                                                   
	                                                    
	template<typename T>
	class AutoPtrDeleteVector : boost::noncopyable {
	public:
		AutoPtrDeleteVector(std::vector<T*>& v) : m_vPointers(v) {};
		~AutoPtrDeleteVector(void) {
			clear();
		}
		void clear() {
			typename std::vector<T*>::iterator it = m_vPointers.begin();
			for( ; it != m_vPointers.end(); ++it ) {
				if ( *it ) {
					delete *it;
					*it = 0;
				}
			}
			m_vPointers.clear();
		}

	private:
		std::vector<T*>& m_vPointers;
	};

	                                                                                                  
	template<typename T>
	class AutoArrayDeleteVector : boost::noncopyable {
	public:
		AutoArrayDeleteVector(std::vector<T*>& v) : m_vPointers(v) {};
		~AutoArrayDeleteVector(void) {
			clear();
		}
		void clear() {
			typename std::vector<T*>::iterator it = m_vPointers.begin();
			for( ; it != m_vPointers.end(); ++it ) {
				if ( *it ) {
					delete[] *it;
					*it = 0;
				}
			}
			m_vPointers.clear();
		}

	private:
		std::vector<T*>& m_vPointers;
	};

	template<class T>
	class AutoDeleteStack {
	public:
		AutoDeleteStack(){}
		~AutoDeleteStack(){
			while(m_stack.size()>0){
				T* t=m_stack.top();
				m_stack.pop();
				delete t;
			}
		}

		void push(T*t){m_stack.push(t);}

	private:
		std::stack<T*> m_stack;
	};


}                   
#endif                          

