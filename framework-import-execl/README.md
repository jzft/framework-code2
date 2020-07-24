# 权限管理基本功能模块

## 能力

快速实现excel导入功能

## 使用
参照ExcelFileUtil类方法

引用ExcelFileUtil如下：

	@RequestMapping(value="keywords", method =RequestMethod.POST,consumes = "multipart/form-data")
	public ResultVo importKeywords(HttpServletRequest request){
		MultipartHttpServletRequest params =((MultipartHttpServletRequest) request);  
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file"); 
        String [] titles = {"列1","列2","姓名","电话"};
		ResultVo<KeywordDto> vo = ExcelFileUtil.importExcel(file, KeywordDto.class, Arrays.asList(titles));
		vo.setList(null);
		return vo;
	}