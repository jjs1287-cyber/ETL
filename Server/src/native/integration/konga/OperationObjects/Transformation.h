/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef OPERATION_TRANSFORMATION_H
#define OPERATION_TRANSFORMATION_H

#include <memory>
#include <string>

#include <boost/noncopyable.hpp>

namespace konga {

	class SourceDataStructure;
	class TargetDataStructure;

	class Transformation : boost::noncopyable {
	public:
		static const long PASS_THROUGH_TRANSFORMATION_ID = -99;
	public:
		Transformation(void);
		virtual ~Transformation(void);

		long getTransformationId() const {
			return m_lID;
		}
		const SourceDataStructure* getSourceStructure() const {
			return m_pSourceStructure.get();
		}

		const TargetDataStructure* getTargetStructure() const {
			return m_pTargetStructure.get();
		}

		long getTargetJmsMessageId() const {
			return m_iTargetJmsMessageId;
		}

	protected:
		friend class LocationCreatorDb;

	private:
		long m_lID;
		long m_iTargetJmsMessageId;
		std::string m_stName;
		bool m_bIsValid;
		std::auto_ptr<SourceDataStructure> m_pSourceStructure;
		std::auto_ptr<TargetDataStructure> m_pTargetStructure;
	};
}
#endif                              

