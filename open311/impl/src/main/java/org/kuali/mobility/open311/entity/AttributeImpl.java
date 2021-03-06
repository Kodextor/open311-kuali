package org.kuali.mobility.open311.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlTransient;

public class AttributeImpl implements Attribute, Serializable {
	
	public AttributeImpl() {}
	private static final long serialVersionUID = 7822282214636842702L;
	
	private String variable;
	private String code;
	private String order;
	private String datatype;
	private String datatypeDescription;
	private String required;
	private String description;
	private List<AttributeValue> values;
	@XmlTransient
	private Map<String,String> valueMap;
	
	public String getVariable() {
		return variable;
	}
	public void setVariable(String variable) {
		this.variable = variable;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	
	public String getDatatype() {
		return datatype;
	}
	
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	
	public String getDatatypeDescription() {
		return datatypeDescription;
	}
	
	public void setDatatypeDescription(String datatypeDescription) {
		this.datatypeDescription = datatypeDescription;
	}
	
	public String getRequired() {
		return required;
	}
	
	public void setRequired(String required) {
		this.required = required;
	}
		
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setValues(List<AttributeValue> values) {
		this.values = values;
	}

	public List<AttributeValue> getValues() {
		return values;
	}
	
	@XmlTransient
	public void setvalueMap(Map<String,String> valueMap) {
		this.valueMap = valueMap;
	}
	
	@XmlTransient
	public Map<String,String> getvalueMap() {
		return valueMap;
	}

}
