package db.pojo;

import java.util.List;

public class Person extends Pojo{
	private String name;
	private int age;
	private List<Address> address;
	public List<Address> getAddress() {
		return address;
	}
	public void setAddress(List<Address> address) {
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Person(Long owner, Long id, String name, int age, List<Address> address) {
		super(owner, id);
		this.name = name;
		this.age = age;
		this.address = address;
	}
	public Person() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
