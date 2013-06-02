package cn.panshihao.mytaskforandroid;

import org.json.JSONException;
import org.json.JSONObject;

import com.shntec.saf.SAFCache;
import com.shntec.saf.SAFConfig;
import com.shntec.saf.SAFException;
import com.shntec.saf.SAFHTTP;

public class MyTaskForAndroid {

	/**
	 * 请求Action
	 * @param cod   action Code
	 * @param prm   参数
	 * @return
	 * @throws SAFException
	 */
	public static JSONObject request(int cod, JSONObject prm) throws SAFException{
		SAFHTTP http = new SAFHTTP();
		SAFConfig config = SAFConfig.getInstance();
		
		// 取得数据Servlet地址
		String dataServlet = config.getString("DataServlet", "");
		SAFCache cache = SAFCache.getInstance();
		// 获取aut用户令牌
		String aut = cache.getP().getString("aut", null);
		JSONObject action = new JSONObject();
		try {
			action.put("cod", cod);
			if(aut != null){
				action.put("aut", aut);
			}
			action.put("prm", prm != null ? prm : new JSONObject());
		} catch (JSONException e) {
			throw new SAFException(0, e.getMessage(), e);
		}
		
		// 执行请求
		return http.POSTtoJSON(dataServlet, action);
	} 
	
}
