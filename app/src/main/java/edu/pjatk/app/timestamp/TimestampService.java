package edu.pjatk.app.timestamp;

import edu.pjatk.app.project.participant.Participant;
import edu.pjatk.app.project.participant.ParticipantService;
import edu.pjatk.app.request.TimestampRequest;
import edu.pjatk.app.user.User;
import edu.pjatk.app.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TimestampService {

    private final TimestampRepository timestampRepository;
    private final ParticipantService participantService;
    private final UserService userService;

    @Autowired
    public TimestampService(TimestampRepository timestampRepository, ParticipantService participantService,
                            UserService userService) {
        this.timestampRepository = timestampRepository;
        this.participantService = participantService;
        this.userService = userService;
    }

    public Optional<Timestamp> getTimestampById(Long id) {
        return timestampRepository.getTimestampById(id);
    }

    public void addTimestamp(TimestampRequest timestampRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userService.findUserByUsername(username);
        if (user.isEmpty()) { return; }

        Optional<Participant> participant = participantService.getParticipantByUserAndProject(user.get().getId(),
                timestampRequest.getProjectId());
        if (participant.isPresent()) {
            Timestamp timestamp = new Timestamp(timestampRequest.getDescription(), timestampRequest.getTimeStart(),
                    timestampRequest.getTimeEnd(), timestampRequest.getProjectName(), participant.get());
            timestampRepository.addTimestamp(timestamp);
        }
    }

    public void deleteTimestampById(Long id) {
        Optional<Timestamp> timestamp = getTimestampById(id);
        if (timestamp.isPresent()) {
            timestampRepository.deleteTimestamp(timestamp.get());
        }
    }
}
