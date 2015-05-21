package db.pojo;
/**
 * 回写的bean 属性必须有get，set方法才能将对应的属性回写
 * @author Administrator
 *
 */
public class Pojo {
	
	
	public Pojo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Pojo(Long owner, Long id) {
		super();
		this.owner = owner;
		this.id = id;
	}
	/**
	 * 拥有者id
	 */
	protected Long owner;
	/**
	 * 唯一的识别号
	 */
	protected Long id;
	public Long getOwner() {
		return owner;
	}
	public void setOwner(Long owner) {
		this.owner = owner;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
}
