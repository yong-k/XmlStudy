/*=========================================================================
	WeatherDAO.java
	- DAO 구성
	- XML DOM 활용 → 원격 XML 읽어내기
	(http://www.kma.go.kr/weather/forecast/mid-term-rss3.jsp?stnId=108)
==========================================================================*/

package com.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;



public class WeatherDAO 
{
	// 공통 멤버 구성 → 멤버 변수 → 초기화 → 생성자
	private Document xmlObj;
	private XPath xPath;
	private HashMap<String, String> map;
	
	// 생성자 정의 → 기본 생성자
	public WeatherDAO() throws ParserConfigurationException, IOException, SAXException 
	{
		this("108");		// 전국 기준
		/*
		stnId=108   전국
		stnId=109   서울, 경기
		stnId=105   강원
		stnId=131   충북
		stnId=133   충남
		stnId=146   전북
		stnId=156   전남
		stnId=143   경북
		stnId=159   경남
		stnId=184   제주특별자치도
		*/
		
	}
	
	// 생성자 정의 → 매개변수 있는 생성자 
	public WeatherDAO(String stnId) throws ParserConfigurationException, IOException, SAXException 
	{
		map = new HashMap<String, String>();
		/*
		맑음, 구름조금, 구름많음, 구름많고 비, 구름많고 비/눈, 구름많고 눈/비, 구름많고 눈
		흐림, 흐리고 비, 흐리고 비/눈, 흐리고 눈/비, 흐리고 눈
		*/
		map.put("맑음", "W_DB01.png");
		map.put("흐림", "W_DB04.png");
		map.put("비", "W_DB05.png");
		map.put("구름조금", "W_NB02.png");
		map.put("구름많음", "W_NB03.png");
		map.put("흐리고 비", "W_NB08.png");
		map.put("구름많고 비", "W_NB20.png");
		
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		// Line 67, 68 한줄로 하면
		// DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		
		String str = String.format
				("http://www.kma.go.kr/weather/forecast/mid-term-rss3.jsp?stnId=%s"
						, stnId);
		URL url = new URL(str);
		
		InputSource is = new InputSource(url.openStream());
		
		xmlObj = builder.parse(is);
		xPath = XPathFactory.newInstance().newXPath();			//-- check~!!!
																//   여태까지와 다른 건 이거 한 줄!!!!
		// XPath = XPathFactory.newInstance().newXPath();
		//  └--→ XPathFactory xFactory = XPathFactory.newInstance();
		//         XPath = xFactory.newPath
		// 		   와 동일한 구문
		
		/*
		○ XPath 생성
		   - XPathFactory 의 정적 메소드(static) 『newInstance()』 호출을 통해
		     XPath 를 생성해주는 XPathFactory 를 생성하고
		   - 이 XPathFactory 의 정적 메소드(static) 『newXPath()』 호출을 통해
		     XPath 객체를 생성한다. 
		     
		 ○ 노드 선택(Selecting Nodes)
		    - 브라우저마다 XPath 를 처리하는 방법에서 차이를 보인다.
		    - Chrome, Firefox, Edge, Opera, Safari 등은
		      『evaluation()』 메소드를 사용하여 노드를 처리한다.
		      → xmlDoc.evaluation(xpath, xmlDoc, null, xPathResult.ANY_TYPE, null)
		      	 → 사이사이에 null 넣은 건 1도 신경안써도 됨
		    - IE 는 『selectNodes()』 메소드를 사용하여 노드를 선택한다.
		    
		 ○ XPath 의 『compile(XPath 경로 표현식)』
		    - XML 데이터 Parsing
		      1. XML 이 제공되는 URL 로 접속하여 데이터를 수신한다.
		      2. DocumentBuilderFactory ... newInstance() 로 factory 를 생성한다.
		      3. DocumentBuilder ... newDocumentBuilder() 로 builder 를 생성한다.
		      4. InputSource is ... new InputSource() 로 InputSource 를 생성한다.
		         이 때, 파일로 수신한 경우라면 File 객체를 넘겨준다.
		      5. Document xmlObj = builder.parse(is) 로 XML 을 파싱(Parsing)한다.
		      6. XPath xPath = XPathFactory.newInstance().newXPath() 로
		         XPath 객체를 생성하고
		      7. XPathExpression expr = XPath.compile( XPath 경로 표현식 ) 으로
		         가져올 Element 를 선택하게 된다.
		      8. 해당 노드(Element)에 접근하여 필요한 데이터를 추출한다.
		*/
		
	}//-- 생성자 끝
	
	// 얻어내려는 거
	// <title>전국 육상 중기예보 - 2022년 06월 17일 (금)요일 06:00 발표</title>
	// → xPath 로 얻어와보자
	//    xPath 변수 선언해놨고, 생성자에서 XPath 만들어놨으니 사용하면 됨
	public String weatherTitle() throws XPathExpressionException
	{
		String result = "";
		
		// rss 부터 시작해서, 그 하위에 있는 channel로 접근하고,
		// 그 하위에서는 item 으로 접근하고,
		// 그리고 그 아래있는 title
		result = xPath.compile("/rss/channel/item/title").evaluate(xmlObj);
		//-- 이게 XML 의 구조적인 경로
		
		//-- 이게 끝!!
		
		return result;
	}
	//-- XML 파일 당 1개 뽑음
	
	
	/*
	 이거 표현하자
	 <wf>
		<![CDATA[ ○ (강수) 20일(월) 오후 제주도, 21일(화)~22일(수) 오전 남부지방과 제주도에 비가 오겠고, 제주도는 23일(목) 오전까지 이어지겠습니다. <br /> 24일(금)~25일(토) 충청권과 남부지방에 비가 오겠고, 26일(일)~27일(월)은 제주도에 비가 오겠습니다. <br />○ (기온) 아침 기온은 19~23도, 낮 기온은 25~33도로 어제(16일, 아침최저기온 16~20도, 낮최고기온 22~29도)보다 높겠습니다.<br />○ (해상) 21일(화)~23일(목)과 24일(금)~26일(일)은 서해남부해상과 제주도해상, 남해상, 동해남부해상을 중심으로 바람이 매우 강하게 불고 물결이 2.0~4.0m로 높게 일겠습니다. <br /><br />* 북태평양고기압의 확장 정도와 북서쪽에서 남쪽으로 내려오는 찬 공기의 강도에 따라 20일(월) 이후 강수 변동성이 크겠으니, 앞으로 발표되는 예보와 기상정보를 참고하기 바랍니다. ]]>
	</wf> 
	*/
	public String weatherInfo() throws XPathExpressionException
	{
		String result = "";
		
		result = xPath.compile("/rss/channel/item/description/header/wf").evaluate(xmlObj);
				
		return result;
	}
	//-- XML 파일 당 1개 뽑음
	
	
	/*
	              ┌→ 파라미터 2개 갖는 evaluate() 있음
	○ XPath 의 『evaluate()』 메소드 두 번째 파라미터
	   - XPathConstants.NODESET : nodeset으로 넘겨줌
	   - XPathConstants.NODE	: 단일노드 넘겨줌
	   - XPathConstants.BOOLEAN : 속성에대한 결과값을 넘겨줌
	   - XPathConstants.NUMBER  : 거기에대한 값을 숫자형태로 넘겨줌
	   - XPathConstants.STRING  : 문자열로 넘겨준다
	*/
	
	
	// check~!!!
	// XML 은 대게 이 방식을 사용하기 때문에 되게 중요함!!
	// 도시 이름 배열 구성
	public ArrayList<String> weatherCityList() throws XPathExpressionException
	{
		ArrayList<String> result = new ArrayList<String>();
		
		NodeList cityNodeList = (NodeList)xPath
				.compile("/rss/channel/item/description/body/location/city")
				.evaluate(xmlObj, XPathConstants.NODESET);
		//-- NODESET을 return 하고 있음 
		//   NODESET 은 NODE들로 구성된거니까 type → NodeList
		//   그런데 넘길 때 객체형태로 넘기고 있으니까 casting 해줘야함
		//-- evaluate(파라미터1, 파라미터2) 로 넘겨주면
		//   내가 너한테 넘기는건 단일값 아니라, 여러 개야! 라는 의미 전달할 수 있음
		
		for (int i = 0; i < cityNodeList.getLength(); i++)
		{
			Node cityNode = cityNodeList.item(i);
			result.add(cityNode.getTextContent());
			
		}
		return result;
	}
	//-- XML 파일 당 여러 개 뽑음 → 자료구조 arrayList 사용	
	
	
	// check~!!!
	// 날씨 정보 리스트
	public ArrayList<WeatherDTO> weatherList(String idx) throws XPathExpressionException
	{
		ArrayList<WeatherDTO> result = new ArrayList<WeatherDTO>();
		
		NodeList dataNodeList = (NodeList)xPath
				.compile(String.format("/rss/channel/item/description/body/location[%s]/data", idx))
				.evaluate(xmlObj, XPathConstants.NODESET);
		
		// check~!!! → 『i=1』
		//for (int i = 0; i <= dataNodeList.getLength(); i++)
		//→ i=0부터 해서 td 맨위에 하나 더 나옴..ㅎㅎ
		for (int i = 1; i <= dataNodeList.getLength(); i++)
		{
			// tmEf (단일값이니 이렇게 처리)
			String tmEf = xPath
					.compile(String.format("/rss/channel/item/description/body/location[%s]/data[%s]/tmEf"
							, idx, i)).evaluate(xmlObj);
			
			// wf
			String wf = xPath
					.compile(String.format("/rss/channel/item/description/body/location[%s]/data[%s]/wf"
							, idx, i)).evaluate(xmlObj);
			
			// tmn
			String tmn = xPath
					.compile(String.format("/rss/channel/item/description/body/location[%s]/data[%s]/tmn"
							, idx, i)).evaluate(xmlObj);
			
			// tmx
			String tmx = xPath
					.compile(String.format("/rss/channel/item/description/body/location[%s]/data[%s]/tmx"
							, idx, i)).evaluate(xmlObj);
			
			// rnSt
			String rnSt = xPath
					.compile(String.format("/rss/channel/item/description/body/location[%s]/data[%s]/rnSt"
							, idx, i)).evaluate(xmlObj);
			
			WeatherDTO w = new WeatherDTO();
			w.setTmEf(tmEf);
			w.setWf(wf);
			w.setTmn(tmn);
			w.setTmx(tmx);
			w.setRnSt(rnSt);
			w.setImg(map.get(wf));
			
			result.add(w);
		}
		
		return result;
	}
}






