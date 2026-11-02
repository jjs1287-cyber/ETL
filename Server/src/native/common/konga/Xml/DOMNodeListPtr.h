/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_XML_DOM_NODE_LIST_PTR_H
#define KONGA_XML_DOM_NODE_LIST_PTR_H

                                                                                                                     
#include <xercesc/dom/DOMNodeList.hpp>

#include <xalanc/XercesParserLiaison/XercesDocumentWrapper.hpp>
#include <xalanc/XPath/NodeRefList.hpp>

#include <boost/shared_ptr.hpp>

#include "DOMNodePtr.h"
#include "ComparablePtr.h"

namespace konga {
    namespace xml {

		class DOMNodeListPtr : public ComparablePtr<XERCES_CPP_NAMESPACE::DOMNodeList>
    	{
        public:
            DOMNodeListPtr(XERCES_CPP_NAMESPACE::DOMNodeList*, const RCDocPtr& doc);

			                                                                                 
                                                                                 
                                                                                     
			DOMNodeListPtr(boost::shared_ptr<XALAN_CPP_NAMESPACE::XercesDocumentWrapper> pXercesDocumentWrapper,
						   XALAN_CPP_NAMESPACE::NodeRefList* pXalanNodeRefList, const RCDocPtr& doc);



			                              
            DOMNodeListPtr(int dummy = 0);

            virtual ~DOMNodeListPtr(void);

			const DOMNodeListPtr* operator->() const { return this; }
			DOMNodeListPtr* operator->() { return this; }

			unsigned long getLength() const {
				return item.getLength();
			}

			DOMNodePtr getItem(unsigned long index) const {
				return item[index];
			}

			DOMNodePtr nextNode() {
				return item.nextNode();
			}

			void reset() {
				item.reset();
			}

			                                                    
			                                                       
            class ListItem {
			private:
				ListItem(XERCES_CPP_NAMESPACE::DOMNodeList* p, const RCDocPtr& doc) : m_pxList(p),
					m_currentIndex(0), m_pDoc(doc) {}

				ListItem(boost::shared_ptr<XALAN_CPP_NAMESPACE::XercesDocumentWrapper> pXercesDocumentWrapper,
					XALAN_CPP_NAMESPACE::NodeRefList* pNodeRefList, const RCDocPtr& doc) : m_pxList(0),
					m_pXercesDocumentWrapper(pXercesDocumentWrapper), m_pNodeRefList(pNodeRefList),
					m_currentIndex(0), m_pDoc(doc) {}
				ListItem() : m_pxList(0), m_currentIndex(0) {}
            public:
                ~ListItem(void) { m_pNodeRefList.reset(); m_pXercesDocumentWrapper.reset(); }
    
                                                                                                                
                                                                         
                                             
                DOMNodePtr operator[](unsigned long index) const;

			private:
				unsigned long getLength() const;
				DOMNodePtr nextNode();
				void reset();

				friend class DOMNodeListPtr;

			private:
				XERCES_CPP_NAMESPACE::DOMNodeList* m_pxList;
				boost::shared_ptr<XALAN_CPP_NAMESPACE::XercesDocumentWrapper> m_pXercesDocumentWrapper;
				boost::shared_ptr<XALAN_CPP_NAMESPACE::NodeRefList> m_pNodeRefList;

				unsigned long m_currentIndex;
				RCDocPtr m_pDoc;	                                
            };

			                                                                                                                 
			bool operator!() const;
			bool operator==(const DOMNodeListPtr& rhs) const;
			bool operator==(int rhs) const;

		public:
			ListItem item;
    	};
    }
}
#endif                                 
