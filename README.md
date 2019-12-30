# Test Object Fimder


### Demo
Because a GIF is worth a thousand words.
![demo1](https://user-images.githubusercontent.com/16775806/71560165-d7138e00-2a98-11ea-80e1-afeb11aa8b39.gif)

### Problem to be solved
Currently the search functionality of Katalon Studio only supports for **name, id, tag, comment, description**. This Katalon plug-in that allows you to search for Test Objects according to attributes within the ```.rs``` file in folder **Object Repository**.

### Features
- [X] Search for Test Objects with one or more phrases
- [X] History of index and search
- [X] Copy Test Object name into Clipboard

This plug-in uses [Lucene](https://lucene.apache.org/) for index and search. Please visit [Github repo](https://github.com/minhthanh3145/Test-Object-Manager) for more information.

### Example query: 

#### Find an input element with name containing ```repository``` (e.g ```input_username repository```)
```
tag=input and name=repository
```
#### Find a Web Sevice element with REST method
```
servicetype = restful
```
#### Find a Web Service element that has a URL containing ```api.imgur.com``` (e.g ```https://api.imgur.com/v2/update```)
```
resturl=api.imgur.com
````
#### Find a Test Object with variable ```defaultVariable```
```
name=defaultVariable
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
