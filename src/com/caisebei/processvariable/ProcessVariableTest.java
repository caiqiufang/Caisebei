package com.caisebei.processvariable;

import static org.junit.Assert.*;

import org.jbpm.api.Configuration;
import org.jbpm.api.ExecutionService;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.TaskService;
import org.junit.Test;

/**
* @author caiqiufang:
* @create date：2017年6月28日 下午3:22:54
*/
public class ProcessVariableTest {

	
	private ProcessEngine processEngine = Configuration.getProcessEngine();
	
	
	
	@Test
	public void testStartProcessInstance() throws Exception{
		ProcessInstance pi = processEngine.getExecutionService().startProcessInstanceByKey("helloworld");
		System.out.println("流程实例启动成功 id="+pi.getId());
	}
	
	
	
	//设置流程的变量
	@Test
	public void setProcessVariable() throws Exception {
		/* 设置变量时，可以设置的变量的类型
		 * java.lang.String
		 * java.lang.Long
		 * java.lang.Double
		 * java.util.Date
		 * java.lang.Boolean
		 * java.lang.Characcter
		 * java.lang.Byte
		 * java.lang.Short
		 * java.lang.Integer
		 * java.lang.Float
		 * byte[] (byte array)
		 * char[] (char array)
		 * hibernate entity with a long id
		 * hibernate entity with a string id
		 * serializable
		 */
		String executionId ="helloworld.150001";
		processEngine.getExecutionService().setVariable(executionId, "请假天数", 15);
	}
	
	//得到流程变量
	@Test
	public void getProcessVariable() throws Exception {
		String executionId ="helloworld.150001";
		Integer days = (Integer) processEngine.getExecutionService().getVariable(executionId, "请假天数");
		System.out.println("请假天数 =" + days);
	}
	
	
	/*{        --------------以下方法作为了解------------
		ExecutionService executionService = processEngine.getExecutionService();
		TaskService taskService = processEngine.getTaskService();
		
		//*********设置变量************************
		executionService.setVariables(executionId, name,value); //设置一个变量
		executionService.setVariable(executionId, variablesMap); //设置多个变量
		taskService.setVariables(taskId, varibles); //设置多个变量
		
		executionService.startProcessInstanceByKey(processDefinitionKey,variableMap); //启动流程实例时，先设置一些变量
		taskService.completeTask(taskId,variablesMap); //真正办理完任务前先设置一些变量
		//*********获取变量************************
		executionService.getVariable(executionId, variableName); //获取一个变量
		executionService.getVariableName(executionId); //返回Set<String> 是所有变量的名称的集合
		executionService.getVariable(executionId, variableNames); //返回一个Map<String,Object> 表示指定名称的变量值和值
		
		taskService.getVariable(taskId, variableName);
		taskService.getVariable(taskId);
		taskService.getVariable(taskId, variableName);
	}*/
	
	
}
