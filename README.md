kind-file-manager
=================

KindEditor(http://kindeditor.org/) is a lightweight, Open Source(LGPL), cross browser, web based WYSIWYG HTML editor. kind-file-manager is the upload manager and file manager for kindeditor, it use Servlet 3.0 techonology, and can be deployed as a single web fragment jar. 

## Maven

## Usage
Put a property file in classpath of you web project, like code below:
```
upload_root=/tmp
upload_size_limit=5242880
```
If you have added dependency in pom.xml, then kind editor is ok to be use in jsp file. Add these code in jsp file (in root of the web content):
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
At last, you should add textarea element in page:
```
	<form name="example" method="post" action="demo.jsp">
		<textarea name="content1" cols="100" rows="8" style="width:700px;height:200px;visibility:hidden;"><%=htmlspecialchars(htmlData)%></textarea>
		<br />
		<input type="submit" name="button" value="Submit" /> (Keyboard shortcut: Ctrl + Enter)
	</form>
```
