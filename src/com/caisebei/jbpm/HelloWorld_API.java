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
 * @create date：2017年6月4日 下午9:51:06
 */
public class HelloWorld_API {

	//建表
	@Test
	public void  testSchema(){
		new org.hibernate.cfg.Configuration()//
			.configure("jbpm.hibernate.cfg.xml")
			.buildSessionFactory();
	}
	
	private static ProcessEngine processEngine = new Configuration()
			.setResource("jbpm.cfg.xml")
			.buildProcessEngine();
	// 1、部署流程定义
	@Test
	public void deployProcessDefintion() throws Exception {
		
		//存入到数据库中
		processEngine.getRepositoryService()//
			.createDeployment()  //部署一个流程定义
			.addResourceFromClasspath("helloworld/helloworld.jpdl.xml")//
			.addResourceFromClasspath("helloworld/helloworld.png")
			.deploy();
		
	}
	//（部署zip）
	@Test
	public void testDeploy_zip() throws Exception {
		//找到zip资源，同时部署两个文件
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("first.zip");
		ZipInputStream zipInputStream = new ZipInputStream(in);
		//存入到数据库中
		String deploymentId = processEngine.getRepositoryService()//
		.createDeployment()  //部署一个流程定义
		.addResourcesFromZipInputStream(zipInputStream)
		.deploy();
		System.out.println("部署成功：deploymentId = " +deploymentId);
	}

	// 2.启动流程实例
	@Test
	public void startProcessInstance() throws Exception {
		//startProcessInstanceByKey 总是根据最新的版本创建一个实例
		processEngine.getExecutionService().startProcessInstanceByKey("helloworld");
		
	}

	// 3.查询我的个人任务列表
	@Test
	public void findMyPersonalTaskList() throws Exception {
		String userId1 = "员工";
		String userId2 = "部门经理";
		String userId3 = "总经理";
		
		//查询
		List<Task> taskList = processEngine.getTaskService().findPersonalTasks(userId1);
		//显示
		System.out.println("------------【"+userId1+"】的个人任务列表--------------------");
		for (Task task : taskList) {
			System.out.println("id=" + task.getId() 
					+ " ,name=" + task.getName()
					+",assignee="+task.getAssignee());
		}
	}
	
	
	

	// 4.办理任务
	@Test
	public void completeTask() throws Exception {
		String taskId = "30001";
		processEngine.getTaskService().completeTask(taskId);
	}

	
	//查询所有的流程定义
	@Test
	public void findAll() throws Exception{
		List<ProcessDefinition> list = processEngine.getRepositoryService()
			.createProcessDefinitionQuery()
			.list();
		
		for(ProcessDefinition pd : list){
			System.out.println("id=" +pd.getId() // 格式为（key）-（version），用于唯一的标识一个
				+ " ,name="+pd.getName()	// 流程定义的名称，jpdl.xml 中元素的name 属性值;
				+" key=" +pd.getKey()	//流程定义的key，jpdl.xml 中的根元素的key 属性的值，默认
				+ " version=" + pd.getVersion()	// 自动生成的，同一个名称的第一个为1，以后的
				+ " deploymentId = " + pd.getDeploymentId());// 所属的部署对象
		}
	}
	
	//查询所有的最新的流程定义
	@Test
	public void findAllLatestVersions() throws Exception{
		//查询所有,让所有的最大的版本都排到最后面
		List<ProcessDefinition> all = processEngine.getRepositoryService()	//
		.createProcessDefinitionQuery()
		.orderAsc(ProcessDefinitionQuery.PROPERTY_VERSION)
		.list();
		//过滤出所有版本最新的流程定义，利用map 同样的key 后者会覆盖前者的性质
		Map<String, ProcessDefinition> map = new HashMap<String, ProcessDefinition>();
		for (ProcessDefinition pd : all) {
			map.put(pd.getKey(), pd);
		}
		
		//显示
		for(ProcessDefinition pd : map.values()){
			System.out.println("id=" +pd.getId() // 格式为（key）-（version），用于唯一的标识一个
				+ " ,name="+pd.getName()	// 流程定义的名称，jpdl.xml 中元素的name 属性值;
				+" key=" +pd.getKey()	//流程定义的key，jpdl.xml 中的根元素的key 属性的值，默认
				+ " version=" + pd.getVersion()	// 自动生成的，同一个名称的第一个为1，以后的
				+ " deploymentId = " + pd.getDeploymentId());// 所属的部署对象
		}
	}
	
	
	//根据di 删除对应的流程的定义
	@Test
	public void deleteById() throws Exception {
		String deloymentId = "40001";
		
		//删除指定的部署对象（流程定义），如果有关联的执行信息，就会报错
		//processEngine.getRepositoryService().deleteDeployment(deloymentId);
		// 删除指定的部署对象（流程定义），如果有关联的执行信息，会被同时删除
		processEngine.getRepositoryService().deleteDeploymentCascade(deloymentId);
	}
	
	//删除指定key的所有的版本的流程的定义
		@Test
		public void deleteByKey() throws Exception {
			//没有删除多个的方法，所以要自己实现
			//查询所有的流程定义
			List<ProcessDefinition> list = processEngine.getRepositoryService()
			.createProcessDefinitionQuery()
			.processDefinitionKey("helloworld")
			.list();
			//删除
			for (ProcessDefinition pd : list) {
				processEngine.getRepositoryService().deleteDeploymentCascade(pd.getDeploymentId());
			}
		}
}
