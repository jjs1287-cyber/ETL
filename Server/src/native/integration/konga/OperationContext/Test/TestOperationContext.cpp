/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <string>
#include <ostream>

#include <konga/OperationContext/DefaultOperationDataElementHolder.h>
#include <konga/OperationContext/DataElementHandler.h>

#include <konga/DataElement/DataElement.h>

using namespace konga;
using namespace std;

namespace {

	const string TEST_KEY("TestDeName");

	class TestDataElementHandler : public DataElementHandler {
	public:
		TestDataElementHandler(void) {}
		~TestDataElementHandler(void) {}

		DataElement* getDataElement(const std::string& deName) const {
			if ( deName == TEST_KEY ) {
				return new DataElement(DataElement::t_string, "handled data element");
			}
			return 0;
		}
	};

	void testHolder(OperationDataElementHolder& deHolder) {
		string sKey1("x");
		string sKey2("yy");
		string sKey2Upper("YY");

		double d = 13.456;
		string stValue("My String");

		DataElement* de = deHolder.getOperationDataElement(sKey1);
		de->setType(DataElement::t_double);
		de->setTypedValue(d);

		de = deHolder.getOperationDataElement(sKey2);
		de->setTypedValue(stValue.c_str(), "string");

		de = deHolder.getOperationDataElement(sKey2Upper);
		if ( de->getType() == DataElement::t_unknown ) {
			cout << "how come?? " << endl;
		}

		string stNewValue;
		stValue="x[x]x";
		stNewValue = deHolder.replaceOperationDataElementInString(stValue);
		cout << stValue << endl;
		cout << stNewValue << endl;

		stValue = "ab[x] \\[yy]def[x][Yy]ghij [x[x]x] [] \\\\[x]";
		stNewValue = deHolder.replaceOperationDataElementInString(stValue);
		cout << stValue << endl;
		cout << stNewValue << endl;

		stValue = "[x\\[x]x]";
		stNewValue = deHolder.replaceOperationDataElementInString(stValue);
		cout << stValue << endl;
		cout << stNewValue << endl;

		de = deHolder.getOperationDataElement("x[x]x");
		de->setTypedValue("MM", "string");
		stNewValue = deHolder.replaceOperationDataElementInString(stValue);
		cout << stValue << endl;
		cout << stNewValue << endl;

		de = deHolder.getOperationDataElement(TEST_KEY);

		int err = 0;
		cout << (const char*)de->GetString(err) << endl;

		de = deHolder.getOperationDataElement(TEST_KEY);

		stValue = "abc[xy";
		stNewValue = deHolder.replaceOperationDataElementInString(stValue);
		if ( stValue != stNewValue )
			cerr << "Error!" << endl;
		cout << stValue << endl;
		cout << stNewValue << endl;

		stValue = "ab[doesnotexist]cd";
		stNewValue = deHolder.replaceOperationDataElementInString(stValue);
		if ( stValue != stNewValue )
			cerr << "Error!" << endl;
		cout << stValue << endl;
		cout << stNewValue << endl;

		stValue = "ab[$doesnotexist]cd";
		stNewValue = deHolder.replaceOperationDataElementInString(stValue);
		if ( stValue != stNewValue )
			cerr << "Error!" << endl;
		cout << stValue << endl;
		cout << stNewValue << endl;

		stValue = "a[$x]b";
		stNewValue = deHolder.replaceOperationDataElementInString(stValue);
		stValue = "a[x]b";
		string stNewOtherValue = deHolder.replaceOperationDataElementInString(stValue);
		if ( stNewValue != stNewOtherValue )
			cerr << "Error!" << endl;
		cout << stValue << endl;
		cout << stNewValue << endl;
		cout << stNewOtherValue << endl;
	}

}

int main(int argc, char* argv[])
{
	DefaultOperationDataElementHolder deHolder;

	TestDataElementHandler testDataElementHandler;
	deHolder.registerDataElementHandler(TEST_KEY, &testDataElementHandler);

	testHolder(deHolder);

	std::auto_ptr<OperationDataElementHolder> clone( deHolder.clone() );
	testHolder(*clone);

	const map<string, DataElement*>& m = deHolder.getOperationDataElements();
	
	map<string, DataElement*>::const_iterator cit = m.begin();
	int err = 0;
	for( ; cit != m.end(); ++cit ) {
		const string& key = cit->first;
		const DataElement* const de = cit->second;
		DataElement* clonedDe = clone->getOperationDataElement(key);
		if ( de->getType() != clonedDe->getType() ||
			de->GetString(err) != clonedDe->GetString(err) ) {
			cerr << "Should be the same!!" << endl;
		}
	}

	DataElement* de = clone->getOperationDataElement("new key");
	de->setTypedValue(123.65);

	deHolder.merge(*clone);

	return 0;
}

