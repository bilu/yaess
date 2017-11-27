package pl.biltec.yaess.core.adapters.store.jpa;

import org.springframework.data.repository.CrudRepository;


/**
 * Spring Data
 */
public interface EventRecordRepository extends CrudRepository<EventRecord, Long> {

}
