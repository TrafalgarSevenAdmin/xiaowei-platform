/**
 * YuLinTu Property Right ContractLand（鱼鳞图农村土地承包经营权登记颁证系统）
 * (C) 2016 鱼鳞图公司版权所有，保留所有权利
 * http://www.yulintu.com
 */
package com.xiaowei.commondict.region;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author chenjie
 * @date 2016年9月21日下午2:57:47
 * @since jdk 1.8
 * 
 * @version 1.0
 */
public enum RegionLevelEnum {

	COUNTRY(7, "国家", 2), PROVINCE(6, "省", 2), CITY(5, "市", 4), COUNTY(4, "县", 6), TOWN(3, "镇", 9), VILLAGE(2, "村", 12), GROUP(1, "组", 14);

	/**
	 * constractor
	 */
	RegionLevelEnum(Integer level, String label, Integer length) {
		this.level = level;
		this.label = label;
		this.length = length;
	}

	private Integer level;

	private String label;
	
	private Integer length;
	
	public static final Map<Integer, RegionLevelEnum> SEQ_MAP = new HashMap<>();
	
	static {
		for (RegionLevelEnum levelEnum : RegionLevelEnum.values()) {
			SEQ_MAP.put(levelEnum.getLevel(), levelEnum);
		}
	}
	
	public static RegionLevelEnum valueOfSeq(Integer seq) {
		return SEQ_MAP.get(seq);
	}

	/**
	 * @return the level
	 */
	public Integer getLevel() {
		return level;
	}

	/**
	 * @param level
	 *            the level to set
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the length
	 */
	public Integer getLength() {
		return length;
	}

	/**
	 * @param length the length to set
	 */
	public void setLength(Integer length) {
		this.length = length;
	}

}
