package co.kr.cafego.api.payment.gateway;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;

/**
 * 카카오페이 연동
 * @author Hsooooo
 *
 */
public class KakaoPayGateway {

	
	/**
	 * <결제준비>에 해당하는 메서드
	 * @param payMap
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> payReady(HashMap<String, Object> payMap) throws UnsupportedEncodingException, IOException{
		
		String cid 			  = "TC0ONETIME";
		String partnerOrderId = (String)payMap.get("cartNo");
		String partnerUserId  = (String)payMap.get("memberEmail");
		String itemName 	  = (String)payMap.get("cartName");
		
		//상품 수량
		int totalAmount = (Integer)payMap.get("totalAmt");
		
		String approvalUrl = "www.naver.com";		//성공시
		String cancelUrl   = "www.naver.com";		//취소시
		String failUrl 	   = "www.naver.com";		//실패시
		
		HashMap<String, String> payParamMap = new HashMap<String, String>();
		
		payParamMap.put("cid", cid);
		payParamMap.put("partner_order_id", partnerOrderId);
		payParamMap.put("partner_user_id", partnerUserId);
		payParamMap.put("item_name",itemName);
		payParamMap.put("quantity","1");
		payParamMap.put("total_amount", Integer.toString(totalAmount));
		payParamMap.put("tax_free_amount", "0");
		payParamMap.put("approval_url", approvalUrl);
		payParamMap.put("cancel_url", cancelUrl);
		payParamMap.put("fail_url", failUrl);
		
		
		CloseableHttpClient client = HttpClients.createDefault();
		
		String postUrl = "https://kapi.kakao.com/v1/payment/ready";
		HttpPost post = new HttpPost(postUrl);
		
		post.addHeader("Authorization", "KakaoAK 8457b163a3812e915b0cf42d29645aff");
		post.addHeader("Content-type", "application/x-www-urlencoded;charset=utf-8");
		
		List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
		
		for(Map.Entry<String, String> entry : payParamMap.entrySet()) {
			valuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		
		post.setEntity(new UrlEncodedFormEntity(valuePairs));
		
		System.out.println("Excuting request" + post.getRequestLine());
		
		Gson gson = new Gson();
		
		CloseableHttpResponse resp = client.execute(post);
		
		Map<String, Object> respMap = (Map<String, Object>)gson.fromJson(gson.toJson(resp.getEntity()), Map.class);
		
		return respMap;
	}
	
	/**
	 * <결제준비>에 해당하는 메서드
	 * @param payMap
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
//	@SuppressWarnings("unchecked")
//	public static Map<String, Object> payReady(HashMap<String, Object> payMap) throws UnsupportedEncodingException, IOException{
//		
//		String cid 			  = "TC0ONETIME";
//		String partnerOrderId = (String)payMap.get("cartNo");
//		String partnerUserId  = (String)payMap.get("memberEmail");
//		String itemName 	  = (String)payMap.get("cartName");
//		
//		//상품 수량
//		int totalAmount = (Integer)payMap.get("totalAmt");
//		
//		String approvalUrl = "";		//성공시
//		String cancelUrl   = "";		//취소시
//		String failUrl 	   = "";		//실패시
//		
//		HashMap<String, String> payParamMap = new HashMap<String, String>();
//		
//		payParamMap.put("cid", cid);
//		payParamMap.put("partner_order_id", partnerOrderId);
//		payParamMap.put("partner_user_id", partnerUserId);
//		payParamMap.put("item_name",itemName);
//		payParamMap.put("quantity","1");
//		payParamMap.put("total_amount", Integer.toString(totalAmount));
//		payParamMap.put("tax_free_amount", "0");
//		Map<String, Object> map = new HashMap<String, Object>();
//		
//		try {
//			String pURL = "https://kapi.kakao.com/v1/payment/ready";
//            //   URL 설정하고 접속하기 
//            URL url = new URL(pURL); // URL 설정 
//
//            HttpURLConnection http = (HttpURLConnection) url.openConnection(); // 접속 
//            //-------------------------- 
//            //   전송 모드 설정 - 기본적인 설정 
//            //-------------------------- 
//            http.setDefaultUseCaches(false);
//            http.setDoInput(true); // 서버에서 읽기 모드 지정 
//            http.setDoOutput(true); // 서버로 쓰기 모드 지정  
//            http.setRequestMethod("POST"); // 전송 방식은 POST
//
//
//
//            //--------------------------
//            // 헤더 세팅
//            //--------------------------
//            // 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다 
//            http.setRequestProperty("Content-type", "application/x-www-urlencoded;charset=utf-8");
//            http.addRequestProperty("Authorization", "KakaoAK 8457b163a3812e915b0cf42d29645aff");
//
//
//            //-------------------------- 
//            //   서버로 값 전송 
//            //-------------------------- 
//            StringBuffer buffer = new StringBuffer();
//
//            //HashMap으로 전달받은 파라미터가 null이 아닌경우 버퍼에 넣어준다
//            if (payParamMap != null) {
//
//                Set<String> key = payParamMap.keySet();
//
//                for (Iterator iterator = key.iterator(); iterator.hasNext();) {
//                    String keyName = (String) iterator.next();
//                    String valueName = payParamMap.get(keyName);
//                    buffer.append(keyName).append("=").append(valueName);
//                }
//            }
//
//            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
//            PrintWriter writer = new PrintWriter(outStream);
//            writer.write(buffer.toString());
//            writer.flush();
//
//
//            //--------------------------
//            //   Response Code
//            //--------------------------
//            //http.getResponseCode();
//
//
//            //-------------------------- 
//            //   서버에서 전송받기 
//            //-------------------------- 
//            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
//            BufferedReader reader = new BufferedReader(tmp);
//            StringBuilder builder = new StringBuilder();
//            String str;
//            while ((str = reader.readLine()) != null) {
//                builder.append(str + "\n");
//            }
//            String myResult = builder.toString();
//            
//            map = new ObjectMapper().readValue(myResult, Map.class);
//            return map;
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return map;
//    
//	}
	
	public static void main(String args[]) {
		try {
			KakaoPayGateway kpg = new KakaoPayGateway();
			HashMap<String, Object> payMap = new HashMap<String, Object>();
			payMap.put("cartNo","123456");
			payMap.put("memberEmail","rkrk6469@nate.com");
			payMap.put("cartName","카페아메리카노 외");
			payMap.put("totalAmt",22000);
			System.out.println(kpg.payReady(payMap).toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
