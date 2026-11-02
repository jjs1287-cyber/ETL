/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DOM_NODE_LIST_BASE
#define KONGA_DOM_NODE_LIST_BASE

#include "DOMNodePtr.h"
#include "RCDocPtr.h"

namespace konga {
    namespace xml {

                                                                                                                 
        template<class C>
        class DOMNodeListBase {
        protected:
            DOMNodeListBase(C* p, const RCDocPtr& doc) : item(p, doc) { }
			DOMNodeListBase(int dummy = 0) { }

		public:
            virtual ~DOMNodeListBase() { };
    
            virtual unsigned long getLength() const {
                return ( item.m_pList != 0 ) ? item.m_pList->getLength() : 0;
            }

            virtual DOMNodePtr getItem(unsigned long index) const {
				return item[index];
            }

			DOMNodePtr nextNode(){
				if ( item.m_currentIndex < getLength() )
					return getItem(item.m_currentIndex++);
				else
					return 0;
			}

			void reset() {
				item.m_currentIndex = 0;
			}

			                                                    
            class ListItem {
            public:
				ListItem(C* p, const RCDocPtr& doc) : m_pList(p), m_currentIndex(0), m_pDoc(doc) {}
				ListItem() : m_pList(0), m_currentIndex(0) {}
                ~ListItem(void) {}
    
                                                                                                                
                                                                         
                                             
                DOMNodePtr operator[](unsigned long index) const {
					return ( m_pList != 0 ) ? DOMNodePtr(m_pList->item(index), m_pDoc) : 0;
                }

				                                                    
				friend class DOMNodeListBase<C>;
				friend class DOMNodeListPtr;
				friend class DOMNamedNodeMapPtr;

			private:
				C* m_pList;
				unsigned long m_currentIndex;
				RCDocPtr m_pDoc;	                                
            };

		public:
			ListItem item;
        };
    }
}
#endif                            

