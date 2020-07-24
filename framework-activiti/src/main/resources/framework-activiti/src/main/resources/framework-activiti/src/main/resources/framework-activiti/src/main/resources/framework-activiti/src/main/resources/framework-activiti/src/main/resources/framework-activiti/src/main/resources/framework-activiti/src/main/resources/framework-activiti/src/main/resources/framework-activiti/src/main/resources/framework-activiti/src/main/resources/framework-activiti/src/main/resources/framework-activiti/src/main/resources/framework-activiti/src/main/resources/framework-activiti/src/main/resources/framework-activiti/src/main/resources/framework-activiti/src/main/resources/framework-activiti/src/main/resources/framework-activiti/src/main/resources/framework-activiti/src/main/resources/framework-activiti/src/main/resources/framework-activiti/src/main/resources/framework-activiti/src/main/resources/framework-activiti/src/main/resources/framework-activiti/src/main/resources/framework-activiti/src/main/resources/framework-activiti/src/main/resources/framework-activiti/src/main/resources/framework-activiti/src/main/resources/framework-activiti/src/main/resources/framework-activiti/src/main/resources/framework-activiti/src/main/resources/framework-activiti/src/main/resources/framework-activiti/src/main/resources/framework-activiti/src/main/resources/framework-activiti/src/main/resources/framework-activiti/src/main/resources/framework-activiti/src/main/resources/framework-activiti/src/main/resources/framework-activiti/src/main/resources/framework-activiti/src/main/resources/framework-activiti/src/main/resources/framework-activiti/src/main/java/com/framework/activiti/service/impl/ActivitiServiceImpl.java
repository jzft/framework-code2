package com.framework.activiti.service.impl;

import org.springframework.stereotype.Service;

import com.framework.activiti.service.ActivitiService;

@Service
public class ActivitiServiceImpl implements ActivitiService {

	
	public String step1(){
		
		return "step1";
	}
	public String step2(){
		return "step2";
	}
	public String step3(){
		return "step3";
	}
	public String step4(){
		return "step4";
	}
	
}
