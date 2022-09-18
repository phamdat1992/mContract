package vn.amitgroup.digitalsignatureapi.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
@Table(name = "contract_tag")
public class ContractTag {
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "contract_tag_id")
	private Integer id;
    @Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;
    @ManyToOne
    @JoinColumn(name = "contract_id",referencedColumnName = "contract_id")
    private Contract contract;
    @ManyToOne
    @JoinColumn(name = "tag_id",referencedColumnName = "tag_id")
    private Tag tag;
}
