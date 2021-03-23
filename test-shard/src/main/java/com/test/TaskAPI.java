/**
 * 
 */
package com.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.pojo.AbcEntity;
import com.test.service.TaskService;

/**
 * @author lyq
 * @date 2020年8月25日 下午12:32:05 
 */
@RequestMapping(value = "test")
@Controller
public class TaskAPI {
	
	@Autowired
	TaskService taskService;
	@ResponseBody
	@RequestMapping(value = "test")
	public String test(){
//		  taskService.testParamPosition("01_1aaa");
		  AbcEntity abc = new AbcEntity();
		  abc.setCol2("2221");
		  abc.setCol1("04_3333");
		  abc.setCol2("2221");
		 taskService.testSplitPPName(abc );
		 abc = new AbcEntity();
		  abc.setCol2("2221");
		 abc.setCol1("03_3333");
		taskService.testSplitPPName(abc );
		abc = new AbcEntity();
		  abc.setCol2("2221");
		abc.setCol1("02_3242");
		taskService.testSplitPPName(abc );
		abc = new AbcEntity();
		  abc.setCol2("2221");
		abc.setCol1("01_4erf");
		taskService.testSplitPPName(abc );
		  return "1111";
	}

}
