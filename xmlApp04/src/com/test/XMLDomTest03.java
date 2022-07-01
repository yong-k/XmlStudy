/*==============================================
 	XMLDomTest03.java
 	- 콘솔 기반 자바 프로그램
 	- XML DOM 활용 → 로컬(local) XML 읽어내기
 	  (breakfast_menu.xml)
===============================================*/

// breakfast_menu.xml 파일을 대상으로 
/*
	■ [Belgian Waffles]	$5.95	650칼로리	
	- Two of our famous Belgian Waffles with plenty of real maple syrup
	---------------------------------------------------------------------
	■ [Strawberry Belgian Waffles]	$7.95	900칼로리	
	- Light Belgian waffles covered with strawberries and whipped cream
	---------------------------------------------------------------------
	■ [Berry-Berry Belgian Waffles]	$8.95	900칼로리	
	- Light Belgian waffles covered with an assortment of fresh berries and whipped cream
	---------------------------------------------------------------------
							:
*/
// 이와 같이 결과 출력이 이루어질 수 있도록 프로그램을 작성한다.

package com.test;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLDomTest03
{
	public static void main(String[] args)
	{
		try
		{
			// XML DOM 형성을 위해 필요한 리소스 구성/준비
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document xmlObj = null;
			
			// XML 파일을 메모르에 로드 → XML DOM 형성
			String url = "breakfast_menu.xml";
			xmlObj = builder.parse(url);
			
			// 루트 엘리먼트 접근
			Element root = xmlObj.getDocumentElement();
			
			// 테스트
			//System.out.println(root.getNodeName());
			//--==> breakfast_menu
			
			// 얻어낸 루트 엘리먼트를 활용하여 특정 하위 엘리먼트에 접근
			NodeList foodNodeList = root.getElementsByTagName("food");
			
			// 테스트
			//System.out.println(breakfast_menuNodeList.getLength());
			//--==>> 5
			
			for (int i = 0; i < foodNodeList.getLength(); i++)
			{
				Node foodNode = foodNodeList.item(i);
				
				//  캐스팅
				Element foodElement = (Element)foodNode;
				
				System.out.printf("■ [%s]   %s   %s칼로리\n - %s\n"
								 , getText(foodElement, "name")
								 , getText(foodElement, "price")
								 , getText(foodElement, "calories")
								 , getText(foodElement, "description"));
				System.out.println("-------------------------------------------------------------------");
			}
			
		} catch (Exception e)
		{
			System.out.println(e.toString());
		}
	}
	
	private static String getText(Element parent, String tagName)
	{
		String result = "";
		
		// 대상 태그(tagName) 객체의 첫 번째 자식 노드 얻어오개
		Node node = parent.getElementsByTagName(tagName).item(0);
		Element element = (Element)node;
		
		// 대상 엘리먼트(element)의 자식 노드(텍스트 노드)의 값 얻어오기
		result = element.getChildNodes().item(0).getNodeValue();
		
		return result;
		
	}
}
