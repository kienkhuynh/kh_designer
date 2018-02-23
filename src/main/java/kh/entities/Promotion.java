package kh.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the promotion database table.
 * 
 */
@Entity
@Table(name="promotion")
@NamedQuery(name="Promotion.findAll", query="SELECT p FROM Promotion p")
public class Promotion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="promotion_id", unique=true, nullable=false)
	private Integer promotionId;

	@Column(nullable=false, length=32)
	private String code;

	@Column(nullable=false)
	private double percentage;

	public Promotion() {
	}

	public Integer getPromotionId() {
		return this.promotionId;
	}

	public void setPromotionId(Integer promotionId) {
		this.promotionId = promotionId;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public double getPercentage() {
		return this.percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

}