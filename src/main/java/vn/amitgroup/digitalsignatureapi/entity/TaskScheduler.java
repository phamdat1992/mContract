package vn.amitgroup.digitalsignatureapi.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "taskscheduler")
public class TaskScheduler {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "taskscheduler_id")
    private Integer id;
    @Column(name = "contract_id")
    public String contractId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time")
    private Date time;
}