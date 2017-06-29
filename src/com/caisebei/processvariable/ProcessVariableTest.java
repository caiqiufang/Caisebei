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
* @create date��2017��6��28�� ����3:22:54
*/
public class ProcessVariableTest {

	
	private ProcessEngine processEngine = Configuration.getProcessEngine();
	
	
	
	@Test
	public void testStartProcessInstance() throws Exception{
		ProcessInstance pi = processEngine.getExecutionService().startProcessInstanceByKey("helloworld");
		System.out.println("����ʵ�������ɹ� id="+pi.getId());
	}
	
	
	
	//�������̵ı���
	@Test
	public void setProcessVariable() throws Exception {
		/* ���ñ���ʱ���������õı���������
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
		processEngine.getExecutionService().setVariable(executionId, "�������", 15);
	}
	
	//�õ����̱���
	@Test
	public void getProcessVariable() throws Exception {
		String executionId ="helloworld.150001";
		Integer days = (Integer) processEngine.getExecutionService().getVariable(executionId, "�������");
		System.out.println("������� =" + days);
	}
	
	
	/*{        --------------���·�����Ϊ�˽�------------
		ExecutionService executionService = processEngine.getExecutionService();
		TaskService taskService = processEngine.getTaskService();
		
		//*********���ñ���************************
		executionService.setVariables(executionId, name,value); //����һ������
		executionService.setVariable(executionId, variablesMap); //���ö������
		taskService.setVariables(taskId, varibles); //���ö������
		
		executionService.startProcessInstanceByKey(processDefinitionKey,variableMap); //��������ʵ��ʱ��������һЩ����
		taskService.completeTask(taskId,variablesMap); //��������������ǰ������һЩ����
		//*********��ȡ����************************
		executionService.getVariable(executionId, variableName); //��ȡһ������
		executionService.getVariableName(executionId); //����Set<String> �����б��������Ƶļ���
		executionService.getVariable(executionId, variableNames); //����һ��Map<String,Object> ��ʾָ�����Ƶı���ֵ��ֵ
		
		taskService.getVariable(taskId, variableName);
		taskService.getVariable(taskId);
		taskService.getVariable(taskId, variableName);
	}*/
	
	
}
