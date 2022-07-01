/*==============================================
 	XMLDomTest04.java
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

public class XMLDomTest04
{
	public static void main(String[] args)
	{
		/*
		-------------------------------------------------------------
		NO		MAKE	MODEL		YEAR	STYLE			PRICE
		-------------------------------------------------------------
		1		Dodge	Durango		1998	Sport Utility	18000
		Options -----------------------------------------------------
			Power_Locks : Yes
			Power_Window : Yes
			Stereo : Radio/Cassette/CD
			Air_Conditioning : Yes
			Automatic : Yes
			Four_Wheel_Drive : Full/Partial
			Note : Very clean
		-------------------------------------------------------------
		2		Honda	Civic		1997	Sedan			8000
		Options -----------------------------------------------------
			Power_Locks: Yes
			Power_Window : Yes
			Stereo : Radio/Cassette
			Automatic : Yes
			Note : Like New
		-------------------------------------------------------------
								:
		*/
		
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			String url = "VEHICLES.xml";
			Document xmlObj = builder.parse(url);
			
			Element rootElement = xmlObj.getDocumentElement();
			
			//-----------------------------------↑ 여기까지는 다른 생각하면서도 타이핑 가능하도록
			//-----------------------------------   많이 타이핑해보면서 익숙해지기
			
			NodeList vehicleNodeList = rootElement.getElementsByTagName("VEHICLE");
			
			System.out.println("-------------------------------------------------------------------------------------------------");
			System.out.println(" NO		   MAKE	     MODEL		 YEAR	        STYLE			  PRICE");
			System.out.println("-------------------------------------------------------------------------------------------------");
			
			for (int i = 0; i < vehicleNodeList.getLength(); i++)
			{
				// 노드들에서 인덱스 넘겨줄테니 특정 노드를 달라는 의미
				Node vehicleNode = vehicleNodeList.item(i);
				
				// NodeList에서 Node로 넘어오면 자연스럽게 Element로 parsing 하는 거!
				Element vehicleElement = (Element)vehicleNode;
				//-- 이러면 기본적으로
				//   1		Dodge	Durango		1998	Sport Utility	18000
				//   얘네들 찍을 준비는 다 끝난거
				
				System.out.printf("%2s		%7s	%10s		%5s	%13s			%7s\n"
								 , vehicleElement.getElementsByTagName("INVENTORY_NUMBER").item(0).getTextContent()
								 , vehicleElement.getElementsByTagName("MAKE").item(0).getTextContent()
								 , vehicleElement.getElementsByTagName("MODEL").item(0).getTextContent()
								 , vehicleElement.getElementsByTagName("YEAR").item(0).getTextContent()
								 , vehicleElement.getElementsByTagName("STYLE").item(0).getTextContent()
								 , vehicleElement.getElementsByTagName("PRICE").item(0).getTextContent());
								//, (Element)vehicleNode.getElementsByTagName("STYLE").item(0).getTextContent()
								// 실무에서 이렇게 많이 쓰는데 이렇게 쓰려면 꼭 아래와 같이 괄호 해야된다.
								// Element로 변환해야될게 vehicleNode 인거지, 그 뒤에까지 다 변환해야되는거 아니기 때문에
								//, ((Element)vehicleNode).getElementsByTagName("STYLE").item(0).getTextContent()
				
				
				// Option 추가
				System.out.println("Options -----------------------------------------------------------------------------------------");
				
				NodeList options = vehicleElement.getElementsByTagName("OPTIONS");
				Node option = options.item(0);
				Element optionElement = (Element)option;
				
				/*
					options 안에 있는 elements 값들이 다 다름
					option 들끼리의 값도 다르고, vehicle이 갖고 있는 option 개수도 다르다.
					이럴 때 쓰는게 XMLDomTest02.java에서의 2번째 방법
				*/
				NodeList childNodes = optionElement.getChildNodes();	//-- check~!!!
				for (int j = 0; j < childNodes.getLength(); j++)
				{
					// childNodes는 NodeList이기 때문에 items 로 특정 노드의 값을 얻어낸다.
					Node childNode = childNodes.item(j);
					
					// node 얻어내고 element로 parsing 했는데, 이번에는 element 값이 다 다름
					
					// 그렇게 얻어낸 노드가 element node 라면, (1번: ELEMENT NODE)
					if (childNode.getNodeType() == 1)	// ELEMENT_NODE //-- check~!!!
					{
						System.out.printf("         %s : %s\n"
										 , childNode.getNodeName()		//-- check~!!!
										 , childNode.getTextContent());
					}
				}
				
				System.out.println("-------------------------------------------------------------------------------------------------");
			}
			
			
			
		} catch (Exception e)
		{
			System.out.println(e.toString());
		}
	}
}

// 실행결과
/*
-------------------------------------------------------------------------------------------------
 NO		   MAKE	     MODEL		 YEAR	        STYLE			  PRICE
-------------------------------------------------------------------------------------------------
 1		  Dodge	   Durango		 1998	Sport Utility			  18000
Options -----------------------------------------------------------------------------------------
         Power_Locks : Yes
         Power_Window : Yes
         Stereo : Radio/Cassette/CD
         Air_Conditioning : Yes
         Automatic : Yes
         Four_Wheel_Drive : Full/Partial
         Note : Very clean
-------------------------------------------------------------------------------------------------
 2		  Honda	     Civic		 1997	        Sedan			   8000
Options -----------------------------------------------------------------------------------------
         Power_Locks : Yes
         Power_Window : Yes
         Stereo : Radio/Cassette
         Automatic : Yes
         Note : Like New
-------------------------------------------------------------------------------------------------
 3		  Dodge	      Neon		 1996	        Sedan			   7000
Options -----------------------------------------------------------------------------------------
         Stereo : Radio/Cassette
         Automatic : Yes
         Note : Need minor body works
-------------------------------------------------------------------------------------------------
 4		Ferrari	      F355		 1995	        Sport			  45000
Options -----------------------------------------------------------------------------------------
         Power_Locks : Yes
         Power_Window : Yes
         Stereo : Radio/Cassette/CD
         Air_Conditioning : Yes
         Note : Luxury car
-------------------------------------------------------------------------------------------------
 5		    BMW	  3 Series		 1998	        Sedan			  40000
Options -----------------------------------------------------------------------------------------
         Power_Locks : Yes
         Power_Window : Yes
         Interiors : Leather
         Stereo : Radio/Cassette/CD
         Air_Conditioning : Yes
         Note : Pre-owned
-------------------------------------------------------------------------------------------------
 6		    BMW	        Z3		 1998	  Convertible			  33000
Options -----------------------------------------------------------------------------------------
         Cover_Material : Plastic
         Power_Locks : Yes
         Power_Window : Yes
         Alarm : Yes
         Interiors : Fabric
         Stereo : Radio/Cassette/CD
         Air_Conditioning : Yes
         Note : Pre-owned, very clean
-------------------------------------------------------------------------------------------------
 7		  Dodge	       RAM		 1997	        Truck			  22000
Options -----------------------------------------------------------------------------------------
         Stereo : Radio
         Max_Load : 1500
         Note : Heavy duty Vehicle
-------------------------------------------------------------------------------------------------
 8		  Honda	    Accord		 1995	        Sedan			   8500
Options -----------------------------------------------------------------------------------------
         Power_Locks : Yes
         Stereo : Radio/Cassette
         Automatic : Yes
-------------------------------------------------------------------------------------------------
 9		  Dodge	  RAM 2500		 1996	        Truck			  25000
Options -----------------------------------------------------------------------------------------
         Stereo : Radio/Cassette
         Max_Load : 2500
         Note : Heavy duty truck
-------------------------------------------------------------------------------------------------
10		   Ford	  Explorer		 1996	Sport Utility			  18000
Options -----------------------------------------------------------------------------------------
         Power_Locks : Yes
         Stereo : Radio/Cassette
         Automatic : Yes
         Interiors : Fabric
         Air_Conditioning : Yes
         Note : Pre-owned, very clean
-------------------------------------------------------------------------------------------------
 
 
*/
