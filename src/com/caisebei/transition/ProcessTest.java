package com.caisebei.transition;

import java.io.InputStream;

import org.jbpm.api.Configuration;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.task.Task;
import org.junit.Test;

/**
* @author caiqiufang:
* @create date：2017年6月28日 下午7:58:45
*/
public class ProcessTest {

	private ProcessEngine processEngine = new Configuration().getProcessEngine();
	
	@Test
	public void testProcess() throws Exception{
		//部署流程定义
		//从当前的包中获取对应文件的文件输入流
		InputStream in = this.getClass().getResourceAsStream("test,jpdl.xml");
		String deploymentId = processEngine.getRepositoryService()
			.createDeployment()
			.addResourceFromInputStream("test.jpdl.xml", in)
			.deploy();
		System.out.println("部署流程定义完毕  deploymentId = " + deploymentId);
		//启动流程实例
		ProcessInstance pi = processEngine.getExecutionService().startProcessInstanceByKey("test");
		System.out.println("流程实例启动完毕 processInstanceId = " +pi.getId());
		//完成第一步 “提交申请” 的任务，要使用指定的Transition 离开当前的活动
		Task task = processEngine.getTaskService()
				.createTaskQuery()
				.processInstanceId(pi.getId())
				.uniqueResult();
		String transitionName1  = "to 部门经理审批";
		String transitionName2 = "to 总经理审批";
		
		processEngine.getTaskService().completeTask(task.getId(), transitionName1); //使用指定名称的Transition离开当前的活动
		
	}
	
	
}
