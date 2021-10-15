package net.rogerplayer.timesheetcalculator.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
public @Data class WorkedDay {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Date arriveWork;
	private Date leaveWork;
	private Date leaveToLunch;
	private Date arriveFromLunch;

	private Date createTime;
	private Date updateTime;

}
