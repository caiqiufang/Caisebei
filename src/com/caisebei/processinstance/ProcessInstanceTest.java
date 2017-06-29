package com.caisebei.processinstance;

import java.util.List;

import org.jbpm.api.Configuration;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.task.Task;
import org.junit.Test;

/**
* @author caiqiufang:
* @create date��2017��6��28�� ����2:04:38
*/
public class ProcessInstanceTest {

	private ProcessEngine processEngine = Configuration.getProcessEngine();
	
	@Test
	public void deployProcessDefinition(){
		processEngine.getRepositoryService()
			.createDeployment()
			.addResourceFromClasspath("com/caisebei/processinstance/helloworld.jpdl.xml")
			.addResourceFromClasspath("com/caisebei/processinstance/helloworld.png")
			.deploy();
	}
	
	
	//��������ʵ��
	//`jbpm4_execution`
	@Test
	public void testStartProcessInstance() throws Exception{
		ProcessInstance pi = processEngine.getExecutionService().startProcessInstanceByKey("helloworld");
		System.out.println("����ʵ�������ɹ� id="+pi.getId()
			+ ", state= " + pi.getState() 
			+ ", processDefinitionId= " +pi.getProcessDefinitionId()	);
	}
	//��ѯ�����б�
	@Test
	public void testFindMyPersonTaskList() throws Exception{
		String userId = "����";
		//��ѯ
//		List<Task> tasks = processEngine.getTaskService().findPersonalTasks(userId);
		List<Task> tasks = processEngine.getTaskService()
							.createTaskQuery()
							//.page(firstResult, maxResult)
							.list();
		
		//��ʾ
		for(Task task : tasks){
			System.out.println("id=" + task.getId()
					+ " ,name=" + task.getName()
					+ " ,assignee=" + task.getAssignee()
					+ " ,createTime=" + task.getCreateTime()
					+ " ,executionId=" +task.getExecutionId());
		}
	}
	//��������
	@Test
	public void testCompleteTask(){
		String taskId = "130002";
		processEngine.getTaskService().completeTask(taskId);
		
	}
	//���ִ��һ��
	@Test
	public void testSignal(){
		String executionId = "helloworld.130001";
		processEngine.getExecutionService().signalExecutionById(executionId);
	}
}
