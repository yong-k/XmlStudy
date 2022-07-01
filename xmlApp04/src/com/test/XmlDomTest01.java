/*==============================================
 	XMLDomTest01.java
 	- 콘솔 기반 자바 프로그램
 	- XML DOM 활용 → 로컬(local) XML 읽어내기
 	  (VEHICLES.xml)
===============================================*/

package com.test;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlDomTest01
{
	public static void main(String[] args)
	{
		// 1. XML 파일을 메모리에 로드 → XML DOM 형성
		// 2. 루트 엘리먼트 접근
		// 3. 특정 하위 엘리먼트 접근 → 위치, 이름 등을 기준으로 접근
		// 4. 텍스트 노드(속성 노드) 접근 → 데이터 획득
		// 5. 결과 처리(출력)
		
		/*
		 xml 객체 얻어내서 그 객체 시작으로 root element 얻어내고
		 각각 하위 element 에 접근하는 방식 
		*/
		
		try
		{
			// XML 파일을 메모리에 로드시킬 준비
			// → XML DOM 형성을 위한 준비
			//    (이를 위해 필요한 리소스 구성)
			
			// 제일 먼저 구성하는 건, DocumentBuilderFacotory
			// → 문서 객체를 얻어내기 위해서 문서를 builder하는, 생성하는 만들어내는 곳			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			//--> 이런 방식으로 썼다는 건, DocumentBuilderFactory는 static 이라는 걸 알 수 있다.
			
			DocumentBuilder builder = factory.newDocumentBuilder();
			//--> 공장에서 Builder라는 녀석을 찍어냄
			// 이걸로 Document를 핸들링하게 된다. (org.w3c.dom)
			
			Document xmlObj = null;
			
			//--==>> 여기까지 해서
			//       xml 관련 document object랑 builder랑 path 얻어냄
			
			
			// XML 파일을 메모리에 로드 → XML DOM 형성
			String url = "VEHICLES.xml";
			xmlObj = builder.parse(url);
			//-- builder.parse() 에 xml path를 넘겨주면,
			//   그 문서에 대한 dom 을 build한다.
			//   xml 객체 넘겨받을 수 있음
			
			// 루트 엘리먼트 접근 (org.w3c.dom)
			Element root = xmlObj.getDocumentElement();
			
			// 테스트
			//System.out.println(root.getNodeName());
			//--==>> VEHICLES
			
			// 특정 하위 엘리먼트 접근 → 위치, 이름을 기준으로 접근
			// 『getElementsByTagName()』 메소드는 태그의 이름을 가지고
			// 자식(자손) 노드에 접근할 수 있도록 해주는 메소드
			NodeList vehicleNodeList = root.getElementsByTagName("VEHICLE");
			//-- Elements 인거 s 빼먹지 않게 주의!
			
			// ※ NodeList 객체에 들어있는 Node 의 개수를
			//	  『getLength()』 메소드를 통해 확인할 수 있다.
			
			// 테스트
			//System.out.println(vehicleNodeList.getLength());
			//--==>> 10
			//       (vehicle이 10개라는 뜻)
			
			for (int i = 0; i < vehicleNodeList.getLength(); i++)	// 10번 반복
			{
				// Node 도 org.w3c.dom 으로 import 하기
				Node vehicleNode = vehicleNodeList.item(i);		// i → 0 ~ 9
				//-- 『item()』 메소드는 파라미터에 해당하는 인덱스에 위치한 노드 접근 메소드
				
				// 캐스트 연산자를 이용하면 
				// Node 객체를 Element 객체로 변환하는 것이 가능하다.
				// Node 는 상위 자료형, Element 는 하위 자료형으로 다루고 있기 때문이다.
				 Element vehicleElement = (Element)vehicleNode;
				
				 // 특정 element 로 nodelist 얻어내고
				 // item이라는 메소드 사용해서 index 기반으로 접근해서 node 얻어내고
				 // 그 node를 Element로 캐스팅함
				 /*
				 NodeList makeNodeList = vehicleElement.getElementsByTagName("MAKE");
				 Node makeNode = makeNodeList.item(0);
				 Element makeElement = (Element)makeNode;
				 System.out.printf("%s : %s\n"
						 		 , makeElement.getNodeName()
						 		 , makeElement.getTextContent());
				 
				 NodeList modelNodeList = vehicleElement.getElementsByTagName("MODEL");
				 Node modelNode = modelNodeList.item(0);
				 Element modelElement = (Element)modelNode;
				 System.out.printf("%s : %s\n\n"
						 		 , modelElement.getNodeName()
						 		 , modelElement.getTextContent());
				*/
				 
				 // 일일히 이렇게 만드는 거 귀찮으니까 
				 // 아래에 아예 메소드로 만들어두자
				 
				 // 특정 엘리먼트의 텍스트 데이터를 얻는
				 // 사용자 정의 메소드 『getText()』 호출
				 System.out.printf("%s %s %s %s %s\n"
						 		 , getText(vehicleElement, "MAKE")
						 		 , getText(vehicleElement, "MODEL")
						 		 , getText(vehicleElement, "YEAR")
						 		 , getText(vehicleElement, "PICTURE")
						 		 , getText(vehicleElement, "STYLE"));
				 
				 // 이런식으로 final project에서 사용해야되는 경우 많을 거임
			}
			
		} catch (Exception e)
		{
			System.out.println(e.toString());
		}
		
	}// end main()
	
	// 위에 있는 구조를 메소드로 뽑아냄
	private static String getText(Element parent, String tagName)
	{
		// 반환할 결과값
		String result = "";
		
		// 특정 태그 이름을 가진 객체의 첫 번째 자식 노드를 얻어온 다음
		Node node = parent.getElementsByTagName(tagName).item(0);
		Element element = (Element)node;
		
		// 특정 엘리먼트의 자식 노드(Text Node)의 값(nodeValue)를 얻어올 수 있도록 처리한 다음 
		result = element.getChildNodes().item(0).getNodeValue();
		
		// 결과값 반환
		return result;
		
	}
}






