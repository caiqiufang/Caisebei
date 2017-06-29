package com.caisebei.processdefintion;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;

import org.jbpm.api.Configuration;
import org.jbpm.api.NewDeployment;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessDefinitionQuery;
import org.jbpm.api.ProcessEngine;
import org.junit.Test;

import javassist.compiler.ast.NewExpr;

/**
* @author caiqiufang:
* @create date：2017年6月28日 上午9:13:14
*/
public class ProcessDefintionTest {

	//使用指定的配置文件生成 processEngine
	//private ProcessEngine processEngine = new Configuration().setResource("jbpm.cfg.xml").buildProcessEngine();
	
	//使用默认位置的配置文件创建 processEngine
	//private ProcessEngine processEngine = new Configuration().buildProcessEngine();
	//获取单例的ProcessEngine 对象(使用默认的 jbpm.cfg.xml) 以后使用此种创建方法
	private ProcessEngine processEngine = Configuration.getProcessEngine();
	
	
	//	 * 部署
	//jbpm4_deployment
	//jbpm4_lob
	//jbpm4_deployprop
	@Test
	public void testDeploy(){
		/**
		 * JBPM4 中的所有的xxId 都是String 型的
		 * deployeementId
		 * processDefinitionId
		 * processInstanceId
		 * ExecutionId
		 * taskId
		 * ...
		 */
		String deploymentId =  processEngine.getRepositoryService()
				.createDeployment()
				.addResourceFromClasspath("helloworld/helloworld.jpdl.xml")	
				.addResourceFromClasspath("helloworld/helloworld.png")
				.deploy();	//调用此方法进行部署
		System.out.println("部署成功：deploymentId = " + deploymentId );
		
		
	}
	
	
	//	 * 查询
	@Test
	public void findAll() throws Exception{
		List<ProcessDefinition> list = processEngine.getRepositoryService()//
				.createProcessDefinitionQuery()	//
				//添加过滤条件
				//.processDefinitionKey("")
				//.processDefinitionNameLike("%xx%")//
				//排序条件
				//.orderAsc(ProcessDefinitionQuery.PROPERTY_KEY)
				//.orderDesc(ProcessDefinitionQuery.PROPERTY_VERSION)
				//执行查询
				//.uniqueResult() 根据筛选条件筛选出指定的唯一的结果
				//.count()
				//.page(firstResult, maxResult)
				.list();
		//显示信息
		for (ProcessDefinition pd : list) {
			System.out.println("id= " +pd.getId() //格式为 (key)(version)用于表示衣蛾流程定义
					+ " name= " + pd.getName() //流程定义的民称
					+ " key= " + pd.getKey() //流程定义的key，jpdl.xml 中元素key属性的值默认是name 的值
					+ " version= " + pd.getVersion()  //版本是自动生成，同一个名称的第一个为1，以后的自动加1
					+ " deploymentId= " + pd.getDeploymentId()); //所属的部署对象
		}
				
	}
	//	 * 删除
	@Test
	public void deleteById(){
		String deploymentId = "100001";
		//删除指定的部署对象（流程定义），如果有级联的关联信息的话会报错
		processEngine.getRepositoryService().deleteDeployment(deploymentId);
		//删除指定的部署对象（流程定义） 如果有关联的执行的信息的话，也会级联删除
		processEngine.getRepositoryService().deleteDeploymentCascade(deploymentId);
		
	}
	//	 * 查看流程图（xxx.png）
	@Test
	public void getImageResource() throws Exception{
		String deploymentId ="100001";
		String resourceName ="helloworld.png";
		//获取指定部署对象中的所有资源的名称
		Set<String> names = processEngine.getRepositoryService().getResourceNames(deploymentId);
		System.out.println("所有的资源的名称");
		for(String name : names){
			System.out.println(name);
		}
		//获取指定部署对象中的指定资源的内容
		InputStream in = processEngine.getRepositoryService().getResourceAsStream(deploymentId,resourceName);
		OutputStream outputStream = new FileOutputStream("e:/process.png");
		System.out.println(in);
		System.out.println(outputStream);
		for(int b = -1; (b=in.read()) != -1;){
			outputStream.write(b);
		}
		
		in.close();
		outputStream.close();
	}
	
	
	
	
	
}
