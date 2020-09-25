package co.kr.cafego.core.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import co.kr.istarbucks.common.exception.ApiException;
import co.kr.istarbucks.common.util.ResultCode;
import co.kr.istarbucks.common.validation.ValidateVO;
import co.kr.istarbucks.common.validation.ValidationUtils;

public class ReqParameterValidator {

	private static final Logger logger = LoggerFactory.getLogger("INFO");
	
	private static final int ARRAY_TYPE        = 0;
	private static final int INT_TYPE          = 1;
	private static final int CELLPHONE_NO_TYPE = 2;
	private static final int DATE_TYPE         = 3; // YYYYMMDD
	private static final int FLOAT_TYPE        = 4;
	private static final int STRING_TYPE       = 5;
	
	private static final int SPLIT_LIMIT       = 5;
	
	private static final String FIELD_SEPERATOR = ";";
	private static final String RULE_SEPERATOR  = "\\|"; 
	
	private final Environment env;

	// 결과 값
	private final String requestPath;
	private String requestKey  = "";
	private String resultCode  = "";
	private String resultMsg   = "";
	
	/**
	 * Constructor 
	 * @param requestPath
	 */
	public ReqParameterValidator(String requestPath, Environment env) {
		this.requestPath = requestPath;
		this.env = env;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	/**
	 * 
	 * @param requestURL
	 * @param paramMap
	 * @param isParamCheck
	 * @return
	 */
	public boolean check(Map<String, Object> paramMap) {

		try {
			//======================================================================================
			// 1. request URI Validation 
			//======================================================================================
			
			boolean isParamCheck = true;
			
			requestKey = "{" + requestPath + "}";
			logger.info("requestKey: {}",requestKey.replaceAll("\n|\r", ""));

			String requestValue = env.getProperty(requestKey + ".ENABLE","N");
			
			logger.info("requestValue: {}",requestValue.replaceAll("\n|\r", ""));
			
			if(StringUtils.isBlank(requestValue)) {
				throw new ApiException(ResultCode.INVALID_URL);
			}
			
			if ("N".equals(requestValue)) {
				isParamCheck = false;
			}
			//======================================================================================
			// 2. request parameters Validation 
			//======================================================================================
			if (isParamCheck) {
				String rules = env.getProperty(requestKey + ".PARAMETER");
				checkParameters(rules, paramMap);
			}
			
		} catch (ApiException e) {
			this.resultCode = e.getResultCode();
			this.resultMsg  = e.getResultMessage();
			return false;
		} catch (Exception e) {
			this.resultCode = ResultCode.SERVER_ERROR;
			this.resultMsg  = e.getMessage();
			logger.error(e.getMessage().replaceAll("\n|\r", ""), e);
			return false;
		}

		return true;
	}

	/**
	 * 요청파라메터의 기본 유효성을 검사한다.
	 * @param requestURL
	 * @param ruleExpression
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean checkParameters(String ruleExpression, Map<String, Object> paramMap)
			throws Exception {
		
		boolean isValid = false;
		
		try {
			
			logger.info("ruleExpression : {}",ruleExpression.replaceAll("\n|\r", ""));
			logger.info("paramMap: {}",paramMap.toString().replaceAll("\n|\r", ""));
			
			if (StringUtils.isBlank(ruleExpression)) {
				logger.warn("VALIDATION-RULE IS UNDEFINED! : URL=" + requestKey.replaceAll("\n|\r", ""));
				isValid = true;
				throw new Exception("VALIDATION-RULE IS UNDEFINED");
			} 
			
			String[] rules = ruleExpression.split(FIELD_SEPERATOR);
			if (rules == null || rules.length == 0) {
				logger.warn("VALIDATION-RULE IS NOT SET! : URL=" + requestKey.replaceAll("\n|\r", ""));
				isValid = true;
				throw new Exception("VALIDATION-RULE IS NOT SET");
			}
			
			ValidateVO vo           = new ValidateVO();
			String[] checkCase      = new String[SPLIT_LIMIT];
			String   parameterKey   = "";
			String   parameterValue = "";
			boolean  isRequired     = false;
			int      parameterType  = 0;
			int      minLength      = 0;
			int      maxLength      = 0;
			
			for (String rule : rules) {
				checkCase    = rule.split(RULE_SEPERATOR, SPLIT_LIMIT);

				parameterKey  = StringUtils.defaultString(checkCase[0]);
				isRequired    = StringUtils.equals(checkCase[1], "Y") ? true : false;
				parameterType = Integer.parseInt(StringUtils.defaultIfEmpty(checkCase[2], "0")); 
				minLength     = Integer.parseInt(StringUtils.defaultIfEmpty(checkCase[3], "0"));
				maxLength     = Integer.parseInt(StringUtils.defaultIfEmpty(checkCase[4], "0"));

				logger.info("parameter :{}",parameterKey.replaceAll("\n|\r", ""));
				logger.info("isRequired :{}",isRequired);
				logger.info("parameterType :{}",parameterType);
				logger.info("minLength :{}",minLength);
				logger.info("maxLength :{}",maxLength);
				
				switch (parameterType) {
					case ARRAY_TYPE:
						// populate
						List<Map<String, Object>> childArray = new ArrayList<Map<String,Object>>();
						
						if (isRequired && paramMap.get(parameterKey) == null) {
							throw new ApiException(ResultCode.INVALID_PARAMETER,
									"paramerter is required[" + parameterKey + "]");
						}
						
						if (paramMap.get(parameterKey) instanceof List) {
							childArray = (List<Map<String, Object>>) paramMap.get(parameterKey);
						} else if (paramMap.get(parameterKey) instanceof Map) {
							childArray = new ArrayList<Map<String, Object>>();
							childArray.add((Map<String, Object>) paramMap.get(parameterKey));
						}
						
						// validate
						for (Map<String, Object> map : childArray) {
							String subRules = env.getProperty(requestKey + ".PARAMETER." + parameterKey);
							logger.info("parameterKey: {}",subRules.replaceAll("\n|\r", ""));
							checkParameters(subRules, map);
						}
						break;
					case INT_TYPE:
						// validate
						parameterValue = StringUtils.defaultString(String.valueOf(paramMap.get(parameterKey)));
						
						if (isRequired && StringUtils.isBlank(parameterValue)) {
							isValid = false;
							throw new ApiException(ResultCode.INVALID_PARAMETER,
									"paramerter is required[" + parameterKey + "]");
						}
						
						if (StringUtils.isNotBlank(parameterValue)) {
							vo.setParamName(parameterKey);
							vo.setParamValue(parameterValue);
							vo.setParamType(Integer.toString(parameterType));
							vo.setMinLength(minLength);
							vo.setMaxLength(maxLength);
							
							if (! ValidationUtils.isValidated(vo)) {
								throw new ApiException(ResultCode.INVALID_PARAMETER, vo.getMessage());
							}
						}
						
						vo.clear();

						break;
					case CELLPHONE_NO_TYPE:
						// validate
						parameterValue = StringUtils.defaultString(String.valueOf(paramMap.get(parameterKey)));
						
						if (isRequired && StringUtils.isBlank(parameterValue)) {
							isValid = false;
							throw new ApiException(ResultCode.INVALID_PARAMETER,
									"paramerter is required[" + parameterKey + "]");
						}
						
						if (StringUtils.isNotBlank(parameterValue)) {
							vo.setParamName(parameterKey);
							vo.setParamValue(parameterValue);
							vo.setParamType(Integer.toString(parameterType));
							vo.setMinLength(minLength);
							vo.setMaxLength(maxLength);
							
							if (! ValidationUtils.isValidated(vo)) {
								throw new ApiException(ResultCode.INVALID_PARAMETER, vo.getMessage());
							}
						}
						
						vo.clear();
						
						break;
					case DATE_TYPE:
						// validate
						parameterValue = StringUtils.defaultString(String.valueOf(paramMap.get(parameterKey)));
						
						if (isRequired && StringUtils.isBlank(parameterValue)) {
							isValid = false;
							throw new ApiException(ResultCode.INVALID_PARAMETER,
									"paramerter is required[" + parameterKey + "]");
						}
						
						if (StringUtils.isNotBlank(parameterValue)) {
							vo.setParamName(parameterKey);
							vo.setParamValue(parameterValue);
							vo.setParamType(Integer.toString(parameterType));
							vo.setMinLength(minLength);
							vo.setMaxLength(maxLength);
							
							if (! ValidationUtils.isValidated(vo)) {
								throw new ApiException(ResultCode.INVALID_PARAMETER, vo.getMessage());
							}
						}
						
						vo.clear();
						
						break;
					case FLOAT_TYPE:
						// validate
						parameterValue = StringUtils.defaultString(String.valueOf(paramMap.get(parameterKey)));
						
						if (isRequired && StringUtils.isBlank(parameterValue)) {
							isValid = false;
							throw new ApiException(ResultCode.INVALID_PARAMETER,
									"paramerter is required[" + parameterKey + "]");
						}
						
						if (StringUtils.isNotBlank(parameterValue)) {
							vo.setParamName(parameterKey);
							vo.setParamValue(parameterValue);
							vo.setParamType(Integer.toString(parameterType));
							vo.setMinLength(minLength);
							vo.setMaxLength(maxLength);
							
							if (! ValidationUtils.isValidated(vo)) {
								throw new ApiException(ResultCode.INVALID_PARAMETER, vo.getMessage());
							}
						}
						
						vo.clear();
						
						break;
					case STRING_TYPE:
						// validate
						parameterValue = StringUtils.defaultString(String.valueOf(paramMap.get(parameterKey)));
						
						if (isRequired && StringUtils.isBlank(parameterValue)) {
							isValid = false;
							throw new ApiException(ResultCode.INVALID_PARAMETER,
									"paramerter is required[" + parameterKey + "]");
						}
						
						if (StringUtils.isNotBlank(parameterValue)) {
							vo.setParamName(parameterKey);
							vo.setParamValue(parameterValue);
							vo.setParamType(Integer.toString(parameterType));
							vo.setMinLength(minLength);
							vo.setMaxLength(maxLength);
							
							if (! ValidationUtils.isValidated(vo)) {
								throw new ApiException(ResultCode.INVALID_PARAMETER, vo.getMessage());
							}
						}
						
						vo.clear();
						
						break;						
					default:
						logger.error("VALIDATION-RULE IS NOT VALID! : parameterType=" + parameterType);
						isValid = false;
						throw new Exception();
				}
			}
			
			isValid = true;

		} catch (ApiException ae) {
			logger.error(ae.getMessage().replaceAll("\n|\r", ""),ae);
			throw ae;
		} catch (Exception e) {
			logger.error(e.getMessage().replaceAll("\n|\r", ""),e);
			throw e;
		}

		return isValid;
	}
}