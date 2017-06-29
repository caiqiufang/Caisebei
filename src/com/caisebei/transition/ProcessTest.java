package com.caisebei.transition;

import java.io.InputStream;

import org.jbpm.api.Configuration;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.task.Task;
import org.junit.Test;

/**
* @author caiqiufang:
* @create date��2017��6��28�� ����7:58:45
*/
public class ProcessTest {

	private ProcessEngine processEngine = new Configuration().getProcessEngine();
	
	@Test
	public void testProcess() throws Exception{
		//�������̶���
		//�ӵ�ǰ�İ��л�ȡ��Ӧ�ļ����ļ�������
		InputStream in = this.getClass().getResourceAsStream("test,jpdl.xml");
		String deploymentId = processEngine.getRepositoryService()
			.createDeployment()
			.addResourceFromInputStream("test.jpdl.xml", in)
			.deploy();
		System.out.println("�������̶������  deploymentId = " + deploymentId);
		//��������ʵ��
		ProcessInstance pi = processEngine.getExecutionService().startProcessInstanceByKey("test");
		System.out.println("����ʵ��������� processInstanceId = " +pi.getId());
		//��ɵ�һ�� ���ύ���롱 ������Ҫʹ��ָ����Transition �뿪��ǰ�Ļ
		Task task = processEngine.getTaskService()
				.createTaskQuery()
				.processInstanceId(pi.getId())
				.uniqueResult();
		String transitionName1  = "to ���ž�������";
		String transitionName2 = "to �ܾ�������";
		
		processEngine.getTaskService().completeTask(task.getId(), transitionName1); //ʹ��ָ�����Ƶ�Transition�뿪��ǰ�Ļ
		
	}
	
	
}
