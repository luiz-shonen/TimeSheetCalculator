package net.rogerplayer.timesheetcalculator.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.rogerplayer.timesheetcalculator.model.WorkedDay;

public interface WorkedDayRepository extends JpaRepository<WorkedDay, Long> {

}
