package net.rogerplayer.timesheetcalculator.resource;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.rogerplayer.timesheetcalculator.model.WorkedDay;
import net.rogerplayer.timesheetcalculator.repository.WorkedDayRepository;

@RestController
@RequestMapping("/worked-day")
public class WorkedDayResource {

	@Autowired
	private WorkedDayRepository workedDayRepository;

	@PostMapping
	public WorkedDay add(@Valid @RequestBody WorkedDay workedDay) {
		return workedDayRepository.save(workedDay);
	}

	@GetMapping
	public List<WorkedDay> listar() {
		return workedDayRepository.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<WorkedDay> findId(@PathVariable Long id) {
		WorkedDay contato = workedDayRepository.findById(id).orElse(null);

		if (contato == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(contato);
	}

	@PutMapping("/{id}")
	public ResponseEntity<WorkedDay> update(@PathVariable Long id, @Valid @RequestBody WorkedDay contato) {
		WorkedDay dbWorkedDay = workedDayRepository.findById(id).orElse(null);

		if (dbWorkedDay == null) {
			return ResponseEntity.notFound().build();
		}
		BeanUtils.copyProperties(contato, dbWorkedDay, "id");

		dbWorkedDay = workedDayRepository.save(dbWorkedDay);

		return ResponseEntity.ok(dbWorkedDay);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		WorkedDay contato = workedDayRepository.findById(id).orElse(null);

		if (contato == null) {
			return ResponseEntity.notFound().build();
		}

		workedDayRepository.delete(contato);

		return ResponseEntity.noContent().build();
	}

}
