# Test-Object-Manager


### Demo
Because a GIF is worth a thousand words.
![demo1](https://user-images.githubusercontent.com/16775806/71560165-d7138e00-2a98-11ea-80e1-afeb11aa8b39.gif)

### Problem to be solved
Currently the search functionality of Katalon Studio only supports for **name, id, tag, comment, description**. This Katalon plug-in that allows you to search for Test Objects according to attributes within the ```.rs``` file in folder **Object Repository**.

### Features
- [X] Search for Test Objects with one or more phrases
- [X] History of index and search
- [X] Copy Test Object name into Clipboard

It uses [Lucene](https://lucene.apache.org/) for index and search.


### Example query: 

Find an input element with name having **repository**
```
tag=input and name=repository
```
Find a web element with REST method
```
servicetype = restful
```

Note that currently, only 'and' operator is supported.


### Sample ```.rs``` file:
```
<?xml version="1.0" encoding="UTF-8"?>
<WebServiceRequestEntity>
   <description></description>
   <name>New Request</name>
   <tag></tag>
   <elementGuidId>afc82ac6-d80e-42bc-9cce-411349012564</elementGuidId>
   <selectorMethod>BASIC</selectorMethod>
   <useRalativeImagePath>false</useRalativeImagePath>
   <followRedirects>false</followRedirects>
   <httpBody></httpBody>
   <httpBodyContent></httpBodyContent>
   <httpBodyType></httpBodyType>
   <migratedVersion>5.4.1</migratedVersion>
   <restRequestMethod>GET</restRequestMethod>
   <restUrl></restUrl>
   <serviceType>RESTful</serviceType>
   <soapBody></soapBody>
   <soapHeader></soapHeader>
   <soapRequestMethod></soapRequestMethod>
   <soapServiceFunction></soapServiceFunction>
   <wsdlAddress></wsdlAddress>
</WebServiceRequestEntity>

```

The above Test Object would match the query:
```
name=new request
```

or
```
restrequestmethod=get and selectormethod=basic
```

### Supported properties:
* name
* tag
* elementGuidId
* selectorMethod
* xpath
* css
* basic
* useRalativeImagePath
* followRedirects
* httpBody
* httpBodyContent
* httpBodyType
* restRequestMethod
* restUrl
* serviceType
* soapBody
* soapHeader
* soapRequestMethod
* soapServiceFunction
* wsdlAddress
**Note**: You can search for a variable by ```name=<variableName>```
### How to use
1/ Build the JAR file:

```
mvm clean install
```
2/ JAR file will be available in target folder.
3/ Open Katalon Studio (7.0 or later).
4/ **Tools > Plugin > Install Plugin** and choose the JAR File


<a href="https://icons8.com/icon/119112/search-more">Search More icon by Icons8</a>
