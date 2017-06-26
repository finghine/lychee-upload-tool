# lychee-upload-tool
lychee 的一键上传工具

# 使用方法
`mvn assembly:assembly`编译出jar包
把target中lycheeupload-0.0.1-jar-with-dependencies.jar 拷贝到指定目录
比如说是`C:\Program_tools\lychee_upload`,
把run.bat也拷贝过去。
把里面的域名，账号，密码填上。
在autohotkey的脚本中加入
```
#F12::
Run "C:/Program_tools/lychee_upload/run.bat"
return
```

这样按win+F12就可以把剪贴板上的内容上传到lychee服务器，并把图片连接复制到剪贴板上了。在markdown文件中就可以直接使用。

# 所需要环境

* 编译要maven环境
* 运行要java环境