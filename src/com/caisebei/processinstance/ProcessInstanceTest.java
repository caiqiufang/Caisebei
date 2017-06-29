package com.caisebei.processinstance;

import java.util.List;

import org.jbpm.api.Configuration;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.task.Task;
import org.junit.Test;

/**
* @author caiqiufang:
* @create date：2017年6月28日 下午2:04:38
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
	
	
	//启动流程实例
	//`jbpm4_execution`
	@Test
	public void testStartProcessInstance() throws Exception{
		ProcessInstance pi = processEngine.getExecutionService().startProcessInstanceByKey("helloworld");
		System.out.println("流程实例启动成功 id="+pi.getId()
			+ ", state= " + pi.getState() 
			+ ", processDefinitionId= " +pi.getProcessDefinitionId()	);
	}
	//查询任务列表
	@Test
	public void testFindMyPersonTaskList() throws Exception{
		String userId = "张三";
		//查询
//		List<Task> tasks = processEngine.getTaskService().findPersonalTasks(userId);
		List<Task> tasks = processEngine.getTaskService()
							.createTaskQuery()
							//.page(firstResult, maxResult)
							.list();
		
		//显示
		for(Task task : tasks){
			System.out.println("id=" + task.getId()
					+ " ,name=" + task.getName()
					+ " ,assignee=" + task.getAssignee()
					+ " ,createTime=" + task.getCreateTime()
					+ " ,executionId=" +task.getExecutionId());
		}
	}
	//办理任务
	@Test
	public void testCompleteTask(){
		String taskId = "130002";
		processEngine.getTaskService().completeTask(taskId);
		
	}
	//向后执行一步
	@Test
	public void testSignal(){
		String executionId = "helloworld.130001";
		processEngine.getExecutionService().signalExecutionById(executionId);
	}
}
