package com.POC.batch.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="person")
public class DataEntity {

	@Id
	Long id;
	String string1;
	String string2;
	String string3;
	String string4;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getString1() {
		return string1;
	}
	public void setString1(String string1) {
		this.string1 = string1;
	}
	public String getString2() {
		return string2;
	}
	public void setString2(String string2) {
		this.string2 = string2;
	}
	public String getString3() {
		return string3;
	}
	public void setString3(String string3) {
		this.string3 = string3;
	}
	public String getString4() {
		return string4;
	}
	public void setString4(String string4) {
		this.string4 = string4;
	}
	@Override
	public String toString() {
		return "Person [id=" + id + ", string1=" + string1 + ", string2=" + string2 + ", string3=" + string3
				+ ", string4=" + string4 + "]";
	}
	public DataEntity(Long id, String string1, String string2, String string3, String string4) {
		this.id = id;
		this.string1 = string1;
		this.string2 = string2;
		this.string3 = string3;
		this.string4 = string4;
	}
	public DataEntity() {}
}
