package edu.pjatk.app.timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TimestampService {

    private final TimestampRepository timestampRepository;

    @Autowired
    public TimestampService(TimestampRepository timestampRepository) {
        this.timestampRepository = timestampRepository;
    }

    public Optional<Timestamp> getTimestampById(Long id) {
        return timestampRepository.getTimestampById(id);
    }


}
