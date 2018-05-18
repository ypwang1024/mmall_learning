<html>
<%@page contentType="text/html; charset=UTF-8" %>
<body>
<h2>Hello Ypwang1024 !</h2>
上传文件测试:

<br>

<form name="form1" action="/manage/product/uploadFile.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="springMvc上传文件"/>
</form>
<br>
<form name="form2" action="/manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="富文本文件上传"/>
</form>

</body>
</html>
