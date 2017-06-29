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
* @create date��2017��6��28�� ����9:13:14
*/
public class ProcessDefintionTest {

	//ʹ��ָ���������ļ����� processEngine
	//private ProcessEngine processEngine = new Configuration().setResource("jbpm.cfg.xml").buildProcessEngine();
	
	//ʹ��Ĭ��λ�õ������ļ����� processEngine
	//private ProcessEngine processEngine = new Configuration().buildProcessEngine();
	//��ȡ������ProcessEngine ����(ʹ��Ĭ�ϵ� jbpm.cfg.xml) �Ժ�ʹ�ô��ִ�������
	private ProcessEngine processEngine = Configuration.getProcessEngine();
	
	
	//	 * ����
	//jbpm4_deployment
	//jbpm4_lob
	//jbpm4_deployprop
	@Test
	public void testDeploy(){
		/**
		 * JBPM4 �е����е�xxId ����String �͵�
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
				.deploy();	//���ô˷������в���
		System.out.println("����ɹ���deploymentId = " + deploymentId );
		
		
	}
	
	
	//	 * ��ѯ
	@Test
	public void findAll() throws Exception{
		List<ProcessDefinition> list = processEngine.getRepositoryService()//
				.createProcessDefinitionQuery()	//
				//��ӹ�������
				//.processDefinitionKey("")
				//.processDefinitionNameLike("%xx%")//
				//��������
				//.orderAsc(ProcessDefinitionQuery.PROPERTY_KEY)
				//.orderDesc(ProcessDefinitionQuery.PROPERTY_VERSION)
				//ִ�в�ѯ
				//.uniqueResult() ����ɸѡ����ɸѡ��ָ����Ψһ�Ľ��
				//.count()
				//.page(firstResult, maxResult)
				.list();
		//��ʾ��Ϣ
		for (ProcessDefinition pd : list) {
			System.out.println("id= " +pd.getId() //��ʽΪ (key)(version)���ڱ�ʾ�¶����̶���
					+ " name= " + pd.getName() //���̶�������
					+ " key= " + pd.getKey() //���̶����key��jpdl.xml ��Ԫ��key���Ե�ֵĬ����name ��ֵ
					+ " version= " + pd.getVersion()  //�汾���Զ����ɣ�ͬһ�����Ƶĵ�һ��Ϊ1���Ժ���Զ���1
					+ " deploymentId= " + pd.getDeploymentId()); //�����Ĳ������
		}
				
	}
	//	 * ɾ��
	@Test
	public void deleteById(){
		String deploymentId = "100001";
		//ɾ��ָ���Ĳ���������̶��壩������м����Ĺ�����Ϣ�Ļ��ᱨ��
		processEngine.getRepositoryService().deleteDeployment(deploymentId);
		//ɾ��ָ���Ĳ���������̶��壩 ����й�����ִ�е���Ϣ�Ļ���Ҳ�ἶ��ɾ��
		processEngine.getRepositoryService().deleteDeploymentCascade(deploymentId);
		
	}
	//	 * �鿴����ͼ��xxx.png��
	@Test
	public void getImageResource() throws Exception{
		String deploymentId ="100001";
		String resourceName ="helloworld.png";
		//��ȡָ����������е�������Դ������
		Set<String> names = processEngine.getRepositoryService().getResourceNames(deploymentId);
		System.out.println("���е���Դ������");
		for(String name : names){
			System.out.println(name);
		}
		//��ȡָ����������е�ָ����Դ������
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
