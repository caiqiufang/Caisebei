package com.caisebei.jbpm;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.jbpm.api.Configuration;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessDefinitionQuery;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.task.Task;
import org.junit.Test;

import antlr.Version;

/**
 * @author caiqiufang:
 * @create date��2017��6��4�� ����9:51:06
 */
public class HelloWorld_API {

	//����
	@Test
	public void  testSchema(){
		new org.hibernate.cfg.Configuration()//
			.configure("jbpm.hibernate.cfg.xml")
			.buildSessionFactory();
	}
	
	private static ProcessEngine processEngine = new Configuration()
			.setResource("jbpm.cfg.xml")
			.buildProcessEngine();
	// 1���������̶���
	@Test
	public void deployProcessDefintion() throws Exception {
		
		//���뵽���ݿ���
		processEngine.getRepositoryService()//
			.createDeployment()  //����һ�����̶���
			.addResourceFromClasspath("helloworld/helloworld.jpdl.xml")//
			.addResourceFromClasspath("helloworld/helloworld.png")
			.deploy();
		
	}
	//������zip��
	@Test
	public void testDeploy_zip() throws Exception {
		//�ҵ�zip��Դ��ͬʱ���������ļ�
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("first.zip");
		ZipInputStream zipInputStream = new ZipInputStream(in);
		//���뵽���ݿ���
		String deploymentId = processEngine.getRepositoryService()//
		.createDeployment()  //����һ�����̶���
		.addResourcesFromZipInputStream(zipInputStream)
		.deploy();
		System.out.println("����ɹ���deploymentId = " +deploymentId);
	}

	// 2.��������ʵ��
	@Test
	public void startProcessInstance() throws Exception {
		//startProcessInstanceByKey ���Ǹ������µİ汾����һ��ʵ��
		processEngine.getExecutionService().startProcessInstanceByKey("helloworld");
		
	}

	// 3.��ѯ�ҵĸ��������б�
	@Test
	public void findMyPersonalTaskList() throws Exception {
		String userId1 = "Ա��";
		String userId2 = "���ž���";
		String userId3 = "�ܾ���";
		
		//��ѯ
		List<Task> taskList = processEngine.getTaskService().findPersonalTasks(userId1);
		//��ʾ
		System.out.println("------------��"+userId1+"���ĸ��������б�--------------------");
		for (Task task : taskList) {
			System.out.println("id=" + task.getId() 
					+ " ,name=" + task.getName()
					+",assignee="+task.getAssignee());
		}
	}
	
	
	

	// 4.��������
	@Test
	public void completeTask() throws Exception {
		String taskId = "30001";
		processEngine.getTaskService().completeTask(taskId);
	}

	
	//��ѯ���е����̶���
	@Test
	public void findAll() throws Exception{
		List<ProcessDefinition> list = processEngine.getRepositoryService()
			.createProcessDefinitionQuery()
			.list();
		
		for(ProcessDefinition pd : list){
			System.out.println("id=" +pd.getId() // ��ʽΪ��key��-��version��������Ψһ�ı�ʶһ��
				+ " ,name="+pd.getName()	// ���̶�������ƣ�jpdl.xml ��Ԫ�ص�name ����ֵ;
				+" key=" +pd.getKey()	//���̶����key��jpdl.xml �еĸ�Ԫ�ص�key ���Ե�ֵ��Ĭ��
				+ " version=" + pd.getVersion()	// �Զ����ɵģ�ͬһ�����Ƶĵ�һ��Ϊ1���Ժ��
				+ " deploymentId = " + pd.getDeploymentId());// �����Ĳ������
		}
	}
	
	//��ѯ���е����µ����̶���
	@Test
	public void findAllLatestVersions() throws Exception{
		//��ѯ����,�����е����İ汾���ŵ������
		List<ProcessDefinition> all = processEngine.getRepositoryService()	//
		.createProcessDefinitionQuery()
		.orderAsc(ProcessDefinitionQuery.PROPERTY_VERSION)
		.list();
		//���˳����а汾���µ����̶��壬����map ͬ����key ���߻Ḳ��ǰ�ߵ�����
		Map<String, ProcessDefinition> map = new HashMap<String, ProcessDefinition>();
		for (ProcessDefinition pd : all) {
			map.put(pd.getKey(), pd);
		}
		
		//��ʾ
		for(ProcessDefinition pd : map.values()){
			System.out.println("id=" +pd.getId() // ��ʽΪ��key��-��version��������Ψһ�ı�ʶһ��
				+ " ,name="+pd.getName()	// ���̶�������ƣ�jpdl.xml ��Ԫ�ص�name ����ֵ;
				+" key=" +pd.getKey()	//���̶����key��jpdl.xml �еĸ�Ԫ�ص�key ���Ե�ֵ��Ĭ��
				+ " version=" + pd.getVersion()	// �Զ����ɵģ�ͬһ�����Ƶĵ�һ��Ϊ1���Ժ��
				+ " deploymentId = " + pd.getDeploymentId());// �����Ĳ������
		}
	}
	
	
	//����di ɾ����Ӧ�����̵Ķ���
	@Test
	public void deleteById() throws Exception {
		String deloymentId = "40001";
		
		//ɾ��ָ���Ĳ���������̶��壩������й�����ִ����Ϣ���ͻᱨ��
		//processEngine.getRepositoryService().deleteDeployment(deloymentId);
		// ɾ��ָ���Ĳ���������̶��壩������й�����ִ����Ϣ���ᱻͬʱɾ��
		processEngine.getRepositoryService().deleteDeploymentCascade(deloymentId);
	}
	
	//ɾ��ָ��key�����еİ汾�����̵Ķ���
		@Test
		public void deleteByKey() throws Exception {
			//û��ɾ������ķ���������Ҫ�Լ�ʵ��
			//��ѯ���е����̶���
			List<ProcessDefinition> list = processEngine.getRepositoryService()
			.createProcessDefinitionQuery()
			.processDefinitionKey("helloworld")
			.list();
			//ɾ��
			for (ProcessDefinition pd : list) {
				processEngine.getRepositoryService().deleteDeploymentCascade(pd.getDeploymentId());
			}
		}
}
