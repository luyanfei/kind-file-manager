kind-file-manager
=================

KindEditor(http://kindeditor.org/) is a lightweight, Open Source(LGPL), cross browser, web based WYSIWYG HTML editor. kind-file-manager is the upload manager and file manager for kindeditor, it use Servlet 3.0 techonology, and can be deployed as a single web fragment jar. 

## Requirement
1. Java 1.6.0+
2. Servlet 3.0+

## Maven
```
  	<dependency>
  		<groupId>net.kindeditor</groupId>
  		<artifactId>filemanager</artifactId>
  		<version>0.0.1</version>
  	</dependency>
```
## Configuration file
Put a property file named "kindmanager.properties" in classpath of you web project, here is the sample code:
```
upload_root=/tmp
upload_size_limit=5242880
```
Check "/src/main/resources/kindmanager_default.properties" to see all the configuration options.

## Test JSP file
You do not need to import kindeditor files to your web project any more, because all these files are already included in filemanger.jar. All you need to do is to include css/javascript link to your web page. Sample code in jsp listed below, (this file in root of the web content directory):
```
	<link rel="stylesheet" href="kindeditor/themes/default/default.css" />
	<link rel="stylesheet" href="kindeditor/plugins/code/prettify.css" />
	<script charset="utf-8" src="kindeditor/kindeditor.js"></script>
	<script charset="utf-8" src="kindeditor/lang/zh_CN.js"></script>
	<script charset="utf-8" src="kindeditor/plugins/code/prettify.js"></script>
```
Then kind editor should initialized with correct uploadJson and fileManagerJson url, sample code like these:
```
	<script>
		KindEditor.ready(function(K) {
			var editor1 = K.create('textarea[name="content1"]', {
				cssPath : './kindeditor/plugins/code/prettify.css',
				uploadJson : './kindeditor/upload.do',
				fileManagerJson : './kindeditor/filemanager.do',
				allowFileManager : true,
				afterCreate : function() {
					var self = this;
					K.ctrl(document, 13, function() {
						self.sync();
						document.forms['example'].submit();
					});
					K.ctrl(self.edit.doc, 13, function() {
						self.sync();
						document.forms['example'].submit();
					});
				}
			});
			prettyPrint();
		});
	</script>
```
"/kindeditor/upload.do" is used for accpet file upload, and "/kindeditor/filemanager.do" is used for support client file manager. These two url can be changed by kindmanager.properties. Caution: All kindeditor's static javascript or css file can only be accessed through url beginned with "kindeditor/", this cann't be changed by configuration file.

At last, you should add textarea element in your page:
```
	<form name="example" method="post" action="demo.jsp">
		<textarea name="content1" cols="100" rows="8" style="width:700px;height:200px;visibility:hidden;"><%=htmlspecialchars(htmlData)%></textarea>
		<br />
		<input type="submit" name="button" value="Submit" /> (Keyboard shortcut: Ctrl + Enter)
	</form>
```

## PathGenerator
If user upload a file to server, the server should generate a depository path for this file. PathGenerator interface is used for this path generation. You can provide your own PathGenerator implementation.

