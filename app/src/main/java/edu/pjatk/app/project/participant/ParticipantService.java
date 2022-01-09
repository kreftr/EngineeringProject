package edu.pjatk.app.project.participant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    @Autowired
    public ParticipantService(ParticipantRepository participantRepository){
        this.participantRepository = participantRepository;
    }

    public Optional<Participant> getParticipantByUserAndProject(Long userid, Long projectId){
        return participantRepository.getByUserAndProjectIds(userid, projectId);
    }

    public Optional<List<Participant>> getAllWhereUserIsMember(Long userId){
        return participantRepository.getAllWhereUserIsMemberByUserId(userId);
    }

}
