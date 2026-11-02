"""Apply the reviewed v5 optional-field bridge to the v4 deployment DTOs."""
import json
import pathlib
import re

root = pathlib.Path('C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData')
contracts = json.loads(pathlib.Path('C:/ETL/Reports/deployment-contract-comparison.json').read_text(encoding='utf-8-sig'))
for name in ('InterchangeIdHolder', 'InterchangeDataHolder'):
    header = root / (name + '.hpp')
    text = header.read_text()
    assert 'unsupportedDeploymentField' not in text
    text = text.replace('#include <axis/AxisUserAPI.hpp>', '#include <string>\n#include <axis/AxisUserAPI.hpp>', 1)
    text = text.replace('public:', 'public:\n\t// Transient validation state, never serialized as a wire field.\n\tstd::string unsupportedDeploymentField;', 1)
    header.write_text(text)
    source = root / (name + '.cpp')
    text = source.read_text()
    text = text.replace('#include <axis/Axis.hpp>', '#include <axis/Axis.hpp>\n#include <memory>', 1)
    start = text.index('int Axis_DeSerialize_' + name + '(')
    start = text.index('{', start) + 1
    end = text.index('\treturn pIWSDZ->getStatus();', start)
    body = text[start:end]
    blocks = dict(re.findall(r'(\tparam->(\w+) = .*?;)', body, re.S))
    # Re-key the exact generated assignments without changing existing field handling.
    blocks = {field: block for block, field in blocks.items()}
    assert list(blocks) == contracts[name]['native']
    data = name == 'InterchangeDataHolder'
    kind = 'InterchangeDataTable' if data else 'ArrayOf_xsd_string'
    rewritten = '\n\tparam->unsupportedDeploymentField.clear();\n'
    for field in contracts[name]['studio']:
        if field in blocks:
            rewritten += blocks[field] + '\n'
            continue
        # Consume optional v5-only fields so the ordered Axis parser reaches the
        # shared fields that follow them. Reject real unsupported data at the
        # wrapper's normal SOAP fault boundary, before any database writes.
        collection = 'value->rows->item' if data else 'value->item'
        present = 'value.get() && value->rows && value->rows->item' if data else 'value.get() && value->item'
        rewritten += f'''\t{{ // v5 optional field: {field}
\t\tstd::auto_ptr<{kind}> value(({kind}*)pIWSDZ->getCmplxObject(
\t\t\t(void*)Axis_DeSerialize_{kind}, (void*)Axis_Create_{kind},
\t\t\t(void*)Axis_Delete_{kind}, "{field}", Axis_URI_{kind}));
\t\tint count = 0;
\t\tif ({present}) {collection}->get(count);
\t\tif (count > 0) param->unsupportedDeploymentField = "{field}";
\t}}
'''
    text = text[:start] + rewritten + text[end:]
    source.write_text(text)

wrapper = root / 'DeployInterchangeDataWrapper.cpp'
text = wrapper.read_text()
text = '#include <stdexcept>\n' + text
needle = '\t\tCDeployResults * ret = pWs->deployInterchangeData(v0,v1,v2,v3);'
assert needle in text
text = text.replace(needle, '''\t\tif (v2 && !v2->unsupportedDeploymentField.empty())
\t\t\tthrow std::runtime_error("ETLForge does not yet support deployment field " + v2->unsupportedDeploymentField + ". No deployment changes were applied.");
\t\tif (v3 && !v3->unsupportedDeploymentField.empty())
\t\t\tthrow std::runtime_error("ETLForge does not yet support deployment field " + v3->unsupportedDeploymentField + ". No deployment changes were applied.");
''' + needle, 1)
wrapper.write_text(text)
print('Updated both deployment DTO readers; nonempty unsupported fields fail before deployment.')
