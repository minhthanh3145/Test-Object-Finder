# Test-Object-Manager

Currently the search functionality of Katalon Studio only supports for name, id, tag, description. This Katalon plug-in that allows you to search for Test Objects according to attributes as specified within the .rs file.


Example query: 
```
	tag=input and name=repository
	servicetype = restful
```

Note that currently, only 'and' operator is supported.

Supported properties:
	- name
	- tag
	- elementGuidId
	- selectorMethod
	- xpath
	- css
	- basic
	- useRalativeImagePath
	- followRedirects
	- httpBody
	- httpBodyContent
	- httpBodyType
	- restRequestMethod
	- restUrl
	- serviceType
	- soapBody
	- soapHeader
	- soapRequestMethod
	- soapServiceFunction
	- wsdlAddress



How to build to JAR file:

```
mvm clean install
```

<a href="https://icons8.com/icon/119112/search-more">Search More icon by Icons8</a>