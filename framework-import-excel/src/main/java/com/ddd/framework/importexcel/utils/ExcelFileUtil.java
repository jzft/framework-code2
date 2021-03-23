package com.ddd.framework.importexcel.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ddd.framework.importexcel.vo.ResultVo;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;



public class ExcelFileUtil {
	
	
	private static final String MSG_3 = "读取文件失败";
	private static final String MAX_ROW_NUM_EXCEPTION_MSG = "Excel行数不能超过50000行。。";
	private static final int MAX_ROW_NUM = 50000;
	private static final String FILE_SAVE_FAIL_3 = "文件格式不正确，请选择Excel文件.xls";
	private static final String MSG_FILE_NO_EXISTS = "文件不存在！";
	private static final String MSG_2 = "endJ不能小于startJ";
	private static final String MSG_1 = "endI不能小于startI";
	private static final String FILE_SAVE_FAIL_2 = "文件保存失败。error：IOException";
	private static final String FILE_SAVE_FAIL_1 = "文件保存失败。error：FileNotFoundException";
	private static final String TEMPLATE_ERROR="上传模板不对";
	
	private static final String MSG_UPLOAD_SUCCESS = "上传成功";
	private static final String SIGN_1 = "\\";
	private static final String MSG_UPLOAD_FAIL = "上传失败";
	
	/**
	 * 保存文件到指定目录dir，定义文件名为fileName author lyq 2012-7-6
	 * 
	 * @param file
	 * @param dir
	 * @param fileName
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	public static boolean uploadFile(String dir,File file,  String fileName)
			throws IOException {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(file);
			File uploadFile = new File(dir);
			out = new FileOutputStream(uploadFile);
			byte[] buffer = new byte[1024 * 1024];
			int length;
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
			return true;
		} catch (FileNotFoundException ex) {
			new RuntimeException(FILE_SAVE_FAIL_1);
			return false;
		} catch (IOException ex) {
			new RuntimeException(FILE_SAVE_FAIL_2);
			return false;
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}
	
	/**
	 * lyq
	 * @param file 上传文件流
	 * @param clz clz对象的属性对应excel各列，注意属性顺序一一对应
	 * @param titleList titleList列表对应excel第一行title，注意顺序一一对应
	 * @return
	 */
	public static <T> ResultVo<T> importExcel(MultipartFile file ,Class<T> clz,List<String> titleList)  {
		try {
			return importExcel(file.getInputStream(),clz, titleList);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * lyq
	 * @param file 上传文件流
	 * @param clz clz对象的属性对应excel各列，注意属性顺序一一对应
	 * @param titleList titleList列表对应excel第一行title，注意顺序一一对应
	 * @return
	 */
	public static <T> ResultVo<T> importExcel(File file ,Class<T> clz,List<String> titleList)  {
		
		InputStream is = null;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e1);
		}finally{
			file.delete();
		}
		return importExcel(is,clz, titleList);
	}
	
	/**
	 *  把 Map 转为字符串类型
	 *  @author Zhuang Shao Bin
	 *  @version 2012-8-9
	 *  @param errorMap
	 *  @return
	 *  @see
	 *  @since
	 */
	private static String ftmMapToString(Map<String,List<String>> errorMap){
		if(!MapUtils.isEmpty(errorMap)){
			StringBuffer errorBuffer=new StringBuffer();
			for(Map.Entry<String,List<String>> entry:errorMap.entrySet()){
				String key=entry.getKey();
				List<String> value=entry.getValue();
				String strValue=ExcelDataConverter.listToString(value);
				errorBuffer.append(key).append(strValue).append(Constants.ENTER_SIGN);
			}
			return errorBuffer.toString();
		}
		return Constants.EMPTY_STR;
	}
	


	private static <T> ResultVo<T> importExcel(InputStream is , Class <T> clz, List<String> titleList) {
		
		StringBuffer importMessage = new StringBuffer();
		ResultVo<T> vo = new ResultVo<>();
		final String METHODNAME="batchInsert";
		final String N="N";
		boolean canSave=false;
		//错误信息保存
		Map<String,List<String>> errorMap=new HashMap<String, List<String>>();
		
		String msg=Constants.EMPTY_STR;
		
		String dir = null;
		try {
			List list = ExcelFileUtil.loadExcel(is, clz,errorMap,titleList);
			canSave=DataValidater.isMapEmpty(errorMap); // 是否没有错误信息  如果是 不影响下面保存
			if(list != null){ // 只能为 empty 但不能为 null 
//						initExcel(list);
//						ExcelBeanUtil.saveExcelDB(getService(),METHODNAME,	list,canSave);
				vo.setMsg( MSG_UPLOAD_SUCCESS);
				vo.setList(list);
			}
			
		} catch (Exception e) {
			msg = e.getMessage();
			msg = msg == null ? (e.getCause().getMessage() == null ?N: e.getCause().getMessage()): msg;
			if (msg.equals(N)) {				
				throw new RuntimeException(e);
			}
	    } finally {
			
		}
		StringBuffer messageBuffer=new StringBuffer();
		messageBuffer.append(ExcelFileUtil.ftmMapToString(errorMap));
		messageBuffer.append(msg);
		String strMessage=messageBuffer.toString();
		if(!StringUtils.isBlank(strMessage)){
			importMessage.append(messageBuffer);
			vo.setMsg(importMessage.toString());
			vo.setStatus(0);
			return vo;
		}
		vo.setStatus(1);
		return vo;
	}
	
//	/**
//	 * 当第一行为标题时
//	 * author lyq
//	 * 2012-7-9
//	 * @param dir
//	 * @param clz
//	 * @param importMesssage 导入信息提示
//	 * @return
//	 * @throws Exception
//	 */
//	public static List loadExcel(String dir, Class clz,Map<String,List<String>> errorMap) throws Exception {
//		return loadExcel(dir,clz,1,null,null,null,errorMap);
//	}
	
	/**
	 * 当第一行为标题时
	 * author lyq
	 * 2012-7-9
	 * @param dir
	 * @param clz
	 * @param importMesssage 导入信息提示
	 * @return
	 * @throws Exception
	 */
	private static List loadExcel(String dir, Class clz,Map<String,List<String>> errorMap,List<String> titleList) throws Exception {
		return loadExcel(dir,clz,1,null,null,null,errorMap,titleList);
	}
	
	private static List loadExcel(File file, Class clz,Map<String,List<String>> errorMap,List<String> titleList) throws Exception {
		
		return loadExcel(file,clz,1,null,null,null,errorMap,titleList);
	}
	
	private static List loadExcel(InputStream is, Class clz,Map<String,List<String>> errorMap,List<String> titleList) throws Exception {
		
		return loadExcel(is,clz,1,null,null,null,errorMap,titleList);
	}
	
//	/**
//	 * 
//	 * author lyq
//	 * 2012-7-9
//	 * @param dir
//	 * @param clz
//	 * @param startI excel行数据加载的开始
//	 * @param endI excel行数据加载的结尾
//	 * @param startJ 列数据加载的开始
//	 * @param endJ 列数据加载的结尾
//	 * @param importMesssage 导入信息提示
//	 * @return
//	 * @throws Exception
//	 */
//	@SuppressWarnings("unchecked")
//	public static List loadExcel(String dir,Class clz,Integer startI,Integer endI,Integer startJ, Integer initEndJ,Map<String,List<String>> errorMap) throws Exception {
//		
//		List list = new LinkedList();
//		if (endI != null && startI != null && endI > startI) {
//			throw new Exception(MSG_1);
//		}
//		if (initEndJ != null && startJ != null && initEndJ > startJ) {
//			throw new Exception(MSG_2);
//		}
//		File excel = getExcel(dir);
//		Sheet sheet = getExcelSheets(excel, 0);// 第一个sheet
//		Integer rowNum = sheet.getRows();
//		if (rowNum > MAX_ROW_NUM) {
//			throw new Exception(MAX_ROW_NUM_EXCEPTION_MSG);
//		}
//		endI = (endI == null || endI > rowNum) ? rowNum : endI;
//		Integer endJ = null;
//		Cell[] aliasArray = startI == 1 ? sheet.getRow(0) : null;
//		startI = startI == null ? 0 : startI;
//
//		for (int i = startI; i < endI; i++) {
//			Cell[] cells = sheet.getRow(i); // 读取一列
//			if (cells != null && cells.length > 0) {
//				Object bean = clz.newInstance();
//				boolean isNull = true;
//				endJ = (initEndJ == null || initEndJ > cells.length) ? cells.length
//						: endJ;
//				startJ = startJ == null ? 0 : startJ;
//				for (int j = startJ; j < endJ; j++) {
//					String val = cells[j].getContents();
//					val = val == null ? val : val.trim();
//					if (StringUtils.isBlank(val) && isNull) {
//						isNull = true;// //如果全部被!isNull拦截了，isNull为true
//						continue;
//					}
//					isNull = false;
//					String alias = aliasArray == null ? null : aliasArray[j].getContents();
//					PraiseBeanUtil.setProperty(bean, j, val, alias,errorMap);
//				}
//				if (!isNull) {
//					list.add(bean);
//				}
//			}
//		}
//		return list;
//	}

	
	/**
	 * 
	 * author lyq
	 * 2012-7-9
	 * @param dir
	 * @param clz
	 * @param startI excel行数据加载的开始
	 * @param endI excel行数据加载的结尾
	 * @param startJ 列数据加载的开始
	 * @param endJ 列数据加载的结尾
	 * @param importMesssage 导入信息提示
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private static List loadExcel(String dir,Class clz,Integer startI,Integer endI,Integer startJ, Integer initEndJ,Map<String,List<String>> errorMap,List<String> titleList) throws Exception {
		File excel = getExcel(dir);
		return loadExcel(excel,clz, startI, endI, startJ, initEndJ, errorMap, titleList);
	}

	private static List loadExcel(File excel,Class clz, Integer startI, Integer endI, Integer startJ, Integer initEndJ,
		Map<String, List<String>> errorMap, List<String> titleList)
		throws Exception, InstantiationException, IllegalAccessException {
		FileInputStream is = new FileInputStream(excel);	
		return loadExcel(is, clz, startI, endI, startJ, initEndJ, errorMap, titleList);
	}

	private static List loadExcel(InputStream is, Class clz, Integer startI, Integer endI, Integer startJ,
			Integer initEndJ, Map<String, List<String>> errorMap, List<String> titleList)
			throws Exception, InstantiationException, IllegalAccessException {
		List list = new LinkedList();
		if (endI != null && startI != null && endI > startI) {
			throw new Exception(MSG_1);
		}
		if (initEndJ != null && startJ != null && initEndJ > startJ) {
			throw new Exception(MSG_2);
		}
		
		Sheet sheet = getExcelSheets(is, 0);// 第一个sheet
		Integer rowNum = sheet.getRows();
		if (rowNum > MAX_ROW_NUM) {
			throw new Exception(MAX_ROW_NUM_EXCEPTION_MSG);
		}
		endI = (endI == null || endI > rowNum) ? rowNum : endI;
		Integer endJ = null;
		Cell[] aliasArray = startI == 1 ? sheet.getRow(0) : null;
		startI = startI == null ? 0 : startI;
		
		// 验查上传的文件是否为指定的模板
		boolean templateFlag=checkkExcelTitle(aliasArray, errorMap,titleList);
		if(!templateFlag){
			return null;
		}
		
		for (int i = startI; i < endI; i++) {
			Cell[] cells = sheet.getRow(i); // 读取一 行
			if (cells != null && cells.length > 0) {
				Object bean = clz.newInstance();
				boolean isNull = true;
				endJ = (initEndJ == null || initEndJ > cells.length) ? cells.length
						: endJ;
				startJ = startJ == null ? 0 : startJ;
				for (int j = startJ; j < endJ; j++) {
					String val = cells[j].getContents();
					val = val == null ? val : val.trim();
					if (StringUtils.isBlank(val) && isNull) {
						isNull = true;// //如果全部被!isNull拦截了，isNull为true
						continue;
					}
					isNull = false;
					String alias = aliasArray == null ? null : aliasArray[j]
							.getContents();
					ExcelBeanUtil.setProperty(bean, j, val, alias, errorMap);
				}
				if (!isNull) {
					list.add(bean);
				}
			}
		}

		return list;
	}
	
	private static boolean checkkExcelTitle(Cell[] titleCell,Map<String,List<String>> errorMap,List titleList) throws Exception{
		boolean flag=true;
		if(titleCell != null && 
				!DataValidater.isCollectionEmpty(titleList)){	
			int cellLength=titleCell.length;
			int titleListLength=titleList.size();
			
			// 上传模板列比 titleList 少时 提示 上传模板不对
			if(cellLength<titleListLength){
				flag=false;
			}
			
			if(flag){
				for(int i=0;i<titleListLength;i++){
					Cell cell=titleCell[i];
					// 上传模板列为空时  提示 上传模板不对
					if(cell == null){
						flag=false;
						break;
					}
					String contents=cell.getContents();
					String titleValue=titleList.get(i).toString();
					if(!titleValue.equals(contents)){
						flag=false;
						break;
					}
				}
			}
		}
		
		if(!flag){
			List<String> list=new ArrayList<String>();
			list.add("请查看");
			errorMap.put(TEMPLATE_ERROR,list);
		}
		return flag;
	}
	
	
	/**
	 * 根据路径删除文件
	 * author lyq
	 * 2012-7-11
	 * @param dir
	 */
	private static void deleteFile(String dir){
		File file = new File(dir);
		if(file.exists()){
			file.delete();
		}
	}
	
	
	
	
	/**
	 * 根据路径获取文件
	 * author lyq 2012-7-6
	 * 
	 * @param dir
	 * @return
	 * @throws Exception 
	 * @throws IOException
	 */
	private static File getExcel(String dir) throws Exception {
		File file = new File(dir);
		if (file == null) {
			throw new Exception(MSG_FILE_NO_EXISTS);
		}
		if (isNeedFile(file,Constants.XLS)) {
			return file;
		}else{
			throw new Exception(FILE_SAVE_FAIL_3);
		}
	}
	/**
	 * 判断文件格式是否正确
	 * author lyq
	 * 2012-7-10
	 * @param file
	 * @param format
	 */
	private static boolean isNeedFile(File file,String format){
		boolean flag=false;
		if (file == null) {
			return flag;
		}
		String fileName = file.getName();
		if (fileName.endsWith(Constants.DOT_SIGN+format)) {
			flag=true;
		}
		return flag;
	}
	
	

	/**
	 * 获取excel所有的sheet 
	 * author lyq 2012-7-6
	 * @param excel
	 * @param i
	 * @return
	 * @throws Exception 
	 */
	private static Sheet getExcelSheets(File excel, Integer i) throws Exception {
		FileInputStream is = new FileInputStream(excel);
		return getExcelSheets(is, i);
	}
	
	
	
	private static Sheet getExcelSheets(InputStream excelInput, Integer i) throws Exception {
		Workbook wb = null;
		Sheet[] sheets =null;
		try {
			wb = Workbook.getWorkbook(excelInput);
			sheets = wb.getSheets(); // 获取工作
		} catch (Exception e) {
			throw new Exception(MSG_3);
		}
		return sheets[i];
	}
	
	
	
	public static void main(String[] args) {
//		Class a = PraGradingServiceImpl.class;
//		Method [] methods = a.getDeclaredMethods();
//		for(int i = 0 ;i<methods.length;i++){
//			Method field = methods[i];
//			System.out.println(field.getName());
//			Annotation []annotations = field.getAnnotations();
//			for(int j = 0 ;j<annotations.length;j++){
//				Annotation annotation = annotations[j];
//				
//				System.out.println(annotations[j].toString());
//			}
//		}
		
		
		String str="2012-2-1 00:00:00";
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
//			format.parse(str);
			System.out.println(format.format(new Date()));
//			loadExcel("d:/定级名单导入(模板).xls", TstbPraGradingImport.class);
			System.out.println(format.format(new Date()));
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

}
